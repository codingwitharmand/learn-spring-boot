package com.cwa.springsecuritypasswordless.repository;

import com.cwa.springsecuritypasswordless.entity.User;
import com.yubico.webauthn.data.ByteArray;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String username);
    User findByHandle(ByteArray handle);
}
