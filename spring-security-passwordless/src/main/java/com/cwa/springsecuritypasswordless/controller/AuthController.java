package com.cwa.springsecuritypasswordless.controller;

import com.cwa.springsecuritypasswordless.entity.Authenticator;
import com.cwa.springsecuritypasswordless.entity.User;
import com.cwa.springsecuritypasswordless.repository.RegistrationRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.yubico.webauthn.*;
import com.yubico.webauthn.data.*;
import com.yubico.webauthn.exception.AssertionFailedException;
import com.yubico.webauthn.exception.RegistrationFailedException;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.Random;

@Controller
@AllArgsConstructor
public class AuthController {

    private RegistrationRepository registrationRepository;
    private RelyingParty relyingParty;

    @GetMapping("/")
    public String welcome() {
        return "index";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String registerUser(Model model) {
        return "register";
    }

    public String newUserRegistration(@RequestParam String username, @RequestParam String display, HttpSession session) {
        User existingUser = registrationRepository.getUserRepository().findByUsername(username);
        if (existingUser == null) {
            byte[] bytes = new byte[32];
            Random random = new Random();
            random.nextBytes(bytes);
            ByteArray id = new ByteArray(bytes);

            UserIdentity userIdentity = UserIdentity.builder()
                    .name(username)
                    .displayName(display)
                    .id(id)
                    .build();
            User savedUser = new User(userIdentity);
            registrationRepository.getUserRepository().save(savedUser);
            return newAuthRegistration(savedUser, session);
        } else {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username " + username + " already exists. Choose another one.");
        }
    }

    @PostMapping("/registerAuth")
    @ResponseBody
    public String newAuthRegistration(@RequestParam User user, HttpSession session) {
        User existingUser = registrationRepository.getUserRepository().findByHandle(user.getHandle());
        if (existingUser != null) {
            UserIdentity userIdentity = user.toUserIdentity();
            StartRegistrationOptions registrationOptions = StartRegistrationOptions.builder()
                    .user(userIdentity)
                    .build();
            PublicKeyCredentialCreationOptions registration = relyingParty.startRegistration(registrationOptions);
            session.setAttribute(userIdentity.getDisplayName(), registration);
            try {
                return registration.toCredentialsCreateJson();
            } catch (JsonProcessingException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while generating JSON", e);
            }
        } else {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User " + user.getUsername() + " doesn't exists. Please register.");
        }
    }

    @PostMapping("/login")
    @ResponseBody
    public String startLogin(@RequestParam String username, HttpSession session) {
        AssertionRequest request = relyingParty.startAssertion(StartAssertionOptions.builder().username(username).build());
        try {
            session.setAttribute(username, request);
            return request.toCredentialsGetJson();
        } catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/finishAuth")
    @ResponseBody
    public ModelAndView finishRegistration(@RequestParam String credential,
                                           @RequestParam String username,
                                           @RequestParam String credentialName,
                                           HttpSession session) {
        try {
            User user = registrationRepository.getUserRepository().findByUsername(username);
            PublicKeyCredentialCreationOptions requestOptions = (PublicKeyCredentialCreationOptions) session.getAttribute(user.getUsername());
            if (requestOptions != null) {
                PublicKeyCredential<AuthenticatorAttestationResponse, ClientRegistrationExtensionOutputs> publicKeyCredential =
                        PublicKeyCredential.parseRegistrationResponseJson(credential);
                FinishRegistrationOptions options = FinishRegistrationOptions.builder()
                        .request(requestOptions)
                        .response(publicKeyCredential)
                        .build();
                RegistrationResult result = relyingParty.finishRegistration(options);
                Authenticator savedAuth = new Authenticator(result, publicKeyCredential.getResponse(), user, credentialName);
                registrationRepository.getAuthenticatorRepository().save(savedAuth);
                return new ModelAndView("redirect:/login", HttpStatus.SEE_OTHER);
            } else {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Cached request expired. Try to register again.");
            }
        } catch (RegistrationFailedException e) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Registration failed.", e);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to save credential, please try again.", e);
        }
    }

    @PostMapping("/welcome")
    public String finishLogin(@RequestParam String credential,
                              @RequestParam String username,
                              Model model,
                              HttpSession session) {
        try {
            PublicKeyCredential<AuthenticatorAssertionResponse, ClientAssertionExtensionOutputs> publicKeyCredential =
                    PublicKeyCredential.parseAssertionResponseJson(credential);
            AssertionRequest request = (AssertionRequest) session.getAttribute(username);
            AssertionResult result = relyingParty.finishAssertion(FinishAssertionOptions.builder()
                    .request(request)
                    .response(publicKeyCredential)
                    .build());
            if (result.isSuccess()) {
                model.addAttribute("username", username);
                return "welcome";
            } else return "index";
        } catch (IOException e) {
            throw new RuntimeException("Authentication failed", e);
        } catch (AssertionFailedException e) {
            throw new RuntimeException("Authentication failed", e);
        }
    }
}
