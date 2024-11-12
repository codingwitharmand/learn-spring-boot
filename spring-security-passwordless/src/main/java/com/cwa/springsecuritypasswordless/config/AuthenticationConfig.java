package com.cwa.springsecuritypasswordless.config;

import com.cwa.springsecuritypasswordless.repository.RegistrationRepository;
import com.yubico.webauthn.RelyingParty;
import com.yubico.webauthn.data.RelyingPartyIdentity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthenticationConfig {

    @Bean
    public RelyingParty relyingPartyIdentity(RegistrationRepository registrationRepository, WebAuthnProperties properties) {
        RelyingPartyIdentity relyingPartyIdentity = RelyingPartyIdentity.builder()
                .id(properties.getHostname())
                .name(properties.getDisplay())
                .build();

        return RelyingParty.builder()
                .identity(relyingPartyIdentity)
                .credentialRepository(registrationRepository)
                .origins(properties.getOrigin())
                .build();
    }
}
