package com.example.restaurant_voting.web.rest;

import com.example.restaurant_voting.View;
import com.example.restaurant_voting.model.Dish;
import com.example.restaurant_voting.repository.DishRepository;
import com.example.restaurant_voting.util.exception.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Optional;

import static com.example.restaurant_voting.util.EntityUtil.updateDish;
import static com.example.restaurant_voting.util.ValidationUtil.checkExpiredDate;

@RestController
@RequestMapping(value = DishController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class DishController {
    static final String REST_URL = "/api/dishes";

    private final DishRepository dishRepository;

    public DishController(DishRepository dishRepository) {
        this.dishRepository = dishRepository;
    }

    @GetMapping
    public ResponseEntity<Page<Dish>> getAll(Pageable pageable) {
        Page<Dish> dishes = dishRepository.findAllWithJoin(pageable);
        return new ResponseEntity<>(dishes, HttpStatus.OK);
    }

    @GetMapping("/byDate")
    public ResponseEntity<Page<Dish>> getByDate(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date, Pageable pageable) {
        Page<Dish> dishes = dishRepository.findByDate(date, pageable);
        return new ResponseEntity<>(dishes, HttpStatus.OK);
    }

    @GetMapping("/byName")
    public ResponseEntity<Page<Dish>> getByName(@RequestParam("name") String name, Pageable pageable) {
        Page<Dish> dishes = dishRepository.findByNameIgnoreCase(name, pageable);
        return new ResponseEntity<>(dishes, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Dish> getById(@PathVariable("id") final int id) {
        Optional<Dish> dishOptional = dishRepository.findByIdWithJoin(id);
        return ResponseEntity.ok(dishOptional.orElseThrow(() -> new NotFoundException("id=" + id)));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") final int id) {
        Optional<Dish> dishOptional = dishRepository.findByIdWithJoin(id);
        Dish dish = dishOptional.orElseThrow(() -> new NotFoundException("id=" + id));
        checkExpiredDate(dish.getMenu().getActionDate(), id);
        dishRepository.deleteById(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateById(@PathVariable("id") final int id, @Validated(View.Web.class) @RequestBody final Dish dish) {
        Optional<Dish> dishOptional = dishRepository.findByIdWithJoin(id);
        Dish dishDb = dishOptional.orElseThrow(() -> new NotFoundException("id=" + id));
        checkExpiredDate(dishDb.getMenu().getActionDate(), id);
        dishRepository.save(updateDish(dishDb, dish));
    }
}
