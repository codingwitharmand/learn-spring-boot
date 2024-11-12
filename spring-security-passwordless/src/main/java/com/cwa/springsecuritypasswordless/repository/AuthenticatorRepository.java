package com.cwa.springsecuritypasswordless.repository;

import com.cwa.springsecuritypasswordless.entity.Authenticator;
import com.cwa.springsecuritypasswordless.entity.User;
import com.yubico.webauthn.data.ByteArray;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface AuthenticatorRepository extends CrudRepository<Authenticator, Long> {
    Optional<Authenticator> findByCredentialId(ByteArray credentialId);
    List<Authenticator> findAllByUser(User user);
    List<Authenticator> findAllByCredentialId(ByteArray credentialId);
}
