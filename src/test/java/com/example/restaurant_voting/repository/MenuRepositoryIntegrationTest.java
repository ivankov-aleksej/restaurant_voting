package com.example.restaurant_voting.repository;

import com.example.restaurant_voting.model.Menu;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class MenuRepositoryIntegrationTest {

    private final LocalDate DATE = LocalDate.parse("2020-08-01");

    @Autowired
    private MenuRepository menuRepository;

    @Test
    void delete() {
        assertEquals(1, menuRepository.delete(1));
    }

    @Test
    void deleteNotFound() {
        assertEquals(0, menuRepository.delete(100));
    }

    @Test
    void findByName() {
        Page<Menu> menus = menuRepository.findByDate(DATE, PageRequest.of(0, 20));
        assertEquals(2, menus.getContent().size());
        menus.getContent().forEach(menu -> assertEquals(DATE, menu.getActionDate()));

    }

    @Test
    void findByIdWithFetch() {
        Optional<Menu> menuOptional = menuRepository.findById(1);
        assertTrue(menuOptional.isPresent());
        assertEquals(2, menuOptional.get().getDishes().size());
    }
}
