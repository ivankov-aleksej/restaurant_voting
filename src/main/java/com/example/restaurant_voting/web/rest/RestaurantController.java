package com.example.restaurant_voting.web.rest;

import com.example.restaurant_voting.model.Restaurant;
import com.example.restaurant_voting.repository.RestaurantRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(value = RestaurantController.REST_URL, produces = "application/json")
public class RestaurantController {
    static final String REST_URL = "/api/restaurants";

    private final RestaurantRepository restaurantRepository;

    public RestaurantController(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @GetMapping
    public ResponseEntity<Page<Restaurant>> getAll(Pageable pageable) {
        Page<Restaurant> restaurants = restaurantRepository.findAll(pageable);
        return new ResponseEntity<>(restaurants, HttpStatus.OK);
    }

    @GetMapping("byName")
    public ResponseEntity<Page<Restaurant>> getByName(@RequestParam("name") String name, Pageable pageable) {
        Page<Restaurant> restaurants = restaurantRepository.findByNameIgnoreCase(name, pageable);
        return new ResponseEntity<>(restaurants, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Restaurant> getById(@PathVariable("id") Integer id) {
        Optional<Restaurant> restaurant = restaurantRepository.findById(id);
        if (restaurant.isPresent()) {
            return ResponseEntity.ok(restaurant.get());
        } else {
            return ResponseEntity.unprocessableEntity().build();
        }
    }

    @PostMapping
    public ResponseEntity<Restaurant> create(@RequestBody final Restaurant restaurant) {
        return ResponseEntity.status(HttpStatus.CREATED).body(restaurantRepository.save(restaurant));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") final Integer id) {
        return (restaurantRepository.delete(id) != 0 ? ResponseEntity.noContent() : ResponseEntity.unprocessableEntity()).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateById(@PathVariable("id") Integer id, @RequestBody Restaurant restaurant) {
        if (restaurantRepository.findById(id).isPresent()) {
            restaurant.setId(id);
            restaurantRepository.save(restaurant);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.unprocessableEntity().build();
        }
    }
}

