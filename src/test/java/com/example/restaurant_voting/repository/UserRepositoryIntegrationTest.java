package com.example.restaurant_voting.repository;

import com.example.restaurant_voting.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.EnumSet;
import java.util.Optional;

import static com.example.restaurant_voting.model.Role.ADMIN;
import static com.example.restaurant_voting.model.Role.USER;
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
    void findByEmail() {
        Optional<User> userOptional = userRepository.findByEmail("admin@gmail.com");
        assertTrue(userOptional.isPresent());
        User user = userOptional.get();
        assertEquals("admin@gmail.com", user.getEmail());
        assertEquals("{noop}admin", user.getPassword());
        assertTrue(user.getRoles().containsAll(EnumSet.of(ADMIN, USER)));
    }
}
