package com.example.restaurant_voting.web.rest;

import com.example.restaurant_voting.View;
import com.example.restaurant_voting.model.Menu;
import com.example.restaurant_voting.model.Restaurant;
import com.example.restaurant_voting.repository.MenuRepository;
import com.example.restaurant_voting.repository.RestaurantRepository;
import com.example.restaurant_voting.util.DateUtil;
import com.example.restaurant_voting.util.exception.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static com.example.restaurant_voting.util.ValidationUtil.*;

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
    public ResponseEntity<Restaurant> getById(@PathVariable("id") final int id) {
        Optional<Restaurant> restaurantOptional = restaurantRepository.findById(id);
        return ResponseEntity.ok(restaurantOptional.orElseThrow(() -> new NotFoundException("id=" + id)));
    }

    @PostMapping
    public ResponseEntity<Restaurant> create(@Validated(View.Web.class) @RequestBody final Restaurant restaurant) {
        checkNew(restaurant);
        return ResponseEntity.status(HttpStatus.CREATED).body(restaurantRepository.save(restaurant));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") final int id) {
        checkNotFoundCountWithId(restaurantRepository.delete(id), id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable("id") final int id, @Validated(View.Web.class) @RequestBody final Restaurant restaurant) {
        if (!assureIdConsistent(restaurant, id)) {
            restaurant.setId(id);
        }
        Optional<Restaurant> restaurantOptional = restaurantRepository.findById(id);
        checkNotFoundWithId(restaurantOptional.isPresent(), id);
        restaurantRepository.save(restaurant);
    }

    @PostMapping("/{id}/menus")
    public ResponseEntity<Menu> createMenu(@PathVariable("id") final int id) {
        Optional<Restaurant> restaurantOptional = restaurantRepository.findById(id);
        Menu menu = new Menu();
        menu.setActionDate(DateUtil.getTomorrow());
        menu.setRestaurant(restaurantOptional.orElseThrow(() -> new NotFoundException("id=" + id)));
        return ResponseEntity.status(HttpStatus.CREATED).body(menuRepository.save(menu));
    }
}