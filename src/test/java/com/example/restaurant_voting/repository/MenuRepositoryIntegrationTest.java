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

    private final static Integer MENU_ID = 105;
    private final static Integer MENU_NOT_FOUND_ID = 1;
    private final LocalDate DATE = LocalDate.parse("2020-08-01");
    @Autowired
    private MenuRepository menuRepository;

    @Test
    void delete() {
        assertEquals(1, menuRepository.deleteWithDate(DATE, MENU_ID));
    }

    @Test
    void deleteNotFound() {
        assertEquals(0, menuRepository.deleteWithDate(DATE, MENU_NOT_FOUND_ID));
    }

    @Test
    void findByName() {
        Page<Menu> menus = menuRepository.findByDateWithJoin(DATE, PageRequest.of(0, 20));
        assertEquals(2, menus.getContent().size());
        menus.getContent().forEach(menu -> assertEquals(DATE, menu.getActionDate()));
    }

    @Test
    void findByIdWithJoin() {
        Optional<Menu> menuOptional = menuRepository.findByIdWithJoin(MENU_ID);
        assertTrue(menuOptional.isPresent());
        assertEquals(2, menuOptional.get().getDishes().size());
    }

    @Test
    void findAllWithJoin() {
        Page<Menu> menus = menuRepository.findAllWithJoin(PageRequest.of(0, 20));
        assertEquals(4, menus.getContent().size());
    }
}
