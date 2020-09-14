package com.example.restaurant_voting.web.rest;

import com.example.restaurant_voting.model.Menu;
import com.example.restaurant_voting.model.Restaurant;
import com.example.restaurant_voting.repository.MenuRepository;
import com.example.restaurant_voting.repository.RestaurantRepository;
import com.example.restaurant_voting.util.DateUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(value = RestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class RestaurantController {
    static final String REST_URL = "/api/restaurants";

    private final RestaurantRepository restaurantRepository;

    private final MenuRepository menuRepository;

    public RestaurantController(RestaurantRepository restaurantRepository, MenuRepository menuRepository) {
        this.restaurantRepository = restaurantRepository;
        this.menuRepository = menuRepository;
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
        }
        return ResponseEntity.unprocessableEntity().build();
    }

    @PostMapping
    public ResponseEntity<Restaurant> create(@RequestBody final Restaurant restaurant) {
        return ResponseEntity.status(HttpStatus.CREATED).body(restaurantRepository.save(restaurant));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable("id") final Integer id) {
        return (restaurantRepository.delete(id) != 0 ? ResponseEntity.noContent() : ResponseEntity.unprocessableEntity()).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity updateById(@PathVariable("id") Integer id, @RequestBody Restaurant restaurant) {
        //TODO check id = restaurant.id, if empty , add id  else throws exception
        if (restaurantRepository.findById(id).isPresent()) {
            restaurant.setId(id);
            restaurantRepository.save(restaurant);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.unprocessableEntity().build();
    }

    @PostMapping("/{id}/menus")
    public ResponseEntity<Menu> createMenu(@PathVariable("id") final Integer id) {
        if (menuRepository.findByDateWithRestaurantId(DateUtil.getTomorrow(), id).isEmpty()) {
            Optional<Restaurant> restaurantOptional = restaurantRepository.findById(id);
            if (restaurantOptional.isPresent()) {
                Menu menu = new Menu();
                menu.setActionDate(DateUtil.getTomorrow());
                menu.setRestaurant(restaurantOptional.get());
                return ResponseEntity.status(HttpStatus.CREATED).body(menuRepository.save(menu));
            }
        }
        return ResponseEntity.unprocessableEntity().build();
    }
}

