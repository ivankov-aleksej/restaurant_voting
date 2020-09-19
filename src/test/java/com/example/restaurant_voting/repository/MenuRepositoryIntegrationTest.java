package com.example.restaurant_voting.repository;

import com.example.restaurant_voting.model.Menu;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class MenuRepositoryIntegrationTest {

    private final static Integer MENU_ID = 105;
    private final LocalDate DATE = LocalDate.parse("2020-08-01");
    @Autowired
    private MenuRepository menuRepository;

    @Test
    void findByName() {
        Page<Menu> menus = menuRepository.findByDateWithJoin(DATE, PageRequest.of(0, 20));
        assertEquals(2, menus.getContent().size());
    }

    @Test
    void findByIdWithJoin() {
        Optional<Menu> menuOptional = menuRepository.findByIdWithJoin(MENU_ID);
        assertTrue(menuOptional.isPresent());
    }

    @Test
    void findById() {
        Optional<Menu> menuOptional = menuRepository.findById(MENU_ID);
        assertTrue(menuOptional.isPresent());
    }

    @Test
    void findAllWithJoin() {
        Page<Menu> menus = menuRepository.findAllWithJoin(PageRequest.of(0, 20, Sort.by("actionDate", "restaurant.name").descending()));
        assertEquals(4, menus.getContent().size());
    }
}
