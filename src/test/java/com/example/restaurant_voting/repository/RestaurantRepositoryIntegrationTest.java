package com.example.restaurant_voting.repository;

import com.example.restaurant_voting.model.Restaurant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class RestaurantRepositoryIntegrationTest {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Test
    void delete() {
        assertEquals(1, restaurantRepository.delete(103));
    }

    @Test
    void deleteNotFound() {
        assertEquals(0, restaurantRepository.delete(1));
    }

    @Test
    void findByName() {
        Page<Restaurant> restaurants = restaurantRepository.findByNameIgnoreCase("kiNG", PageRequest.of(0, 20));
        assertEquals(1, restaurants.getContent().size());
        assertEquals(restaurants.getContent().get(0).getName(), "Burger King");
    }
}
