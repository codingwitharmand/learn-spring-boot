package com.cwa.springsecuritypasswordless.entity;

import com.yubico.webauthn.RegistrationResult;
import com.yubico.webauthn.data.AttestedCredentialData;
import com.yubico.webauthn.data.AuthenticatorAttestationResponse;
import com.yubico.webauthn.data.ByteArray;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Entity
@Getter
@NoArgsConstructor
public class Authenticator {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @Lob
    @Column(nullable = false)
    private ByteArray credentialId;

    @Lob
    @Column(nullable = false)
    private ByteArray publicKey;

    @ManyToOne
    private User user;

    @Column(nullable = false)
    private Long count;

    @Lob
    @Column(nullable = false)
    private ByteArray aaguid;

    public Authenticator(RegistrationResult result, AuthenticatorAttestationResponse response, User user, String name) {
        Optional<AttestedCredentialData> attestedCredentialData = response.getAttestation().getAuthenticatorData().getAttestedCredentialData();
        this.credentialId = result.getKeyId().getId();
        this.publicKey = result.getPublicKeyCose();
        this.aaguid = attestedCredentialData.get().getAaguid();
        this.count = result.getSignatureCount();
        this.name = name;
        this.user = user;
    }
}
