package com.example.restaurant_voting.repository;

import com.example.restaurant_voting.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.EnumSet;
import java.util.Optional;

import static com.example.restaurant_voting.model.Role.ROLE_ADMIN;
import static com.example.restaurant_voting.model.Role.ROLE_USER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class UserRepositoryIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void delete() {
        assertEquals(1, userRepository.delete(100));
    }

    @Test
    void deleteNotFound() {
        assertEquals(0, userRepository.delete(1));
    }

    @Test
    void findByName() {
        Optional<User> userOptional = userRepository.findByEmailIgnoreCase("AdmiN@GmaiL.com");
        assertTrue(userOptional.isPresent());
        userOptional.ifPresent(user -> {
            assertEquals("admin@gmail.com", user.getEmail());
            assertEquals("admin", user.getPassword());
            assertTrue(user.getRoles().containsAll(EnumSet.of(ROLE_ADMIN, ROLE_USER)));
        });
    }
}
