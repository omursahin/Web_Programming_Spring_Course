package org.webp.intro.spring.security.framework.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Collections;

@Service
@Transactional
public class UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EntityManager em;

    public boolean createUser(String username, String password) {

        String hashedPassword = passwordEncoder.encode(password);

        if (em.find(UserEntity.class, username) != null) {
            return false;
        }

        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setPassword(hashedPassword);
        user.setRoles(Collections.singleton("USER"));
        user.setEnabled(true);

        em.persist(user);

        return true;
    }
}
