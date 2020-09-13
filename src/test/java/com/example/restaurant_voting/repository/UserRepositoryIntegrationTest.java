package com.example.restaurant_voting.repository;

import com.example.restaurant_voting.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.EnumSet;
import java.util.List;

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
    void findByEmailIgnoreCase() {
        Page<User> userPage = userRepository.findByEmailIgnoreCase("AdmiN@GmaiL.com", PageRequest.of(0, 20));
        assertTrue(userPage.hasContent());
        List<User> userList = userPage.getContent();
        assertEquals(1, userList.size());
        User user = userList.get(0);
        assertEquals("admin@gmail.com", user.getEmail());
        assertEquals("{noop}admin", user.getPassword());
        assertTrue(user.getRoles().containsAll(EnumSet.of(ADMIN, USER)));

    }
}
