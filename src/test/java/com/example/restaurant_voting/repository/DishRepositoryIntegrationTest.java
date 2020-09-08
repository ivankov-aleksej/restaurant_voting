package com.example.restaurant_voting.repository;

import com.example.restaurant_voting.model.Dish;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class DishRepositoryIntegrationTest {

    private final LocalDate DATE = LocalDate.parse("2020-08-01");

    @Autowired
    private DishRepository dishRepository;

    @Test
    void delete() {
        assertEquals(1, dishRepository.delete(1));
    }

    @Test
    void deleteNotFound() {
        assertEquals(0, dishRepository.delete(100));
    }

    @Test
    void findByDate() {
        Page<Dish> dishes = dishRepository.findByDate(DATE, PageRequest.of(0, 20));
        assertEquals(4, dishes.getContent().size());
        dishes.getContent().forEach(dish -> assertEquals(DATE, dish.getMenu().getActionDate()));
    }

    @Test
    void findByName() {
        Page<Dish> restaurants = dishRepository.findByNameIgnoreCase("Роя", PageRequest.of(0, 20));
        assertEquals(2, restaurants.getContent().size());
        restaurants.getContent().forEach(dish -> assertEquals("Роял Фреш", dish.getName()));
    }
}