package com.example.restaurant_voting.web.rest;

import com.example.restaurant_voting.model.Dish;
import com.example.restaurant_voting.repository.DishRepository;
import com.example.restaurant_voting.util.DateUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Optional;

import static com.example.restaurant_voting.util.EntityUtil.updateDish;

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
    public ResponseEntity<Page<Dish>> getByDate(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date, Pageable pageable) {
        Page<Dish> dishes = dishRepository.findByDate(date, pageable);
        return new ResponseEntity<>(dishes, HttpStatus.OK);
    }

    @GetMapping("/byName")
    public ResponseEntity<Page<Dish>> getByName(@RequestParam("name") String name, Pageable pageable) {
        Page<Dish> dishes = dishRepository.findByNameIgnoreCase(name, pageable);
        return new ResponseEntity<>(dishes, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Dish> getById(@PathVariable("id") Integer id) {
        Optional<Dish> dishOptional = dishRepository.findByIdWithJoin(id);
        if (dishOptional.isPresent()) {
            return ResponseEntity.ok(dishOptional.get());
        }
        return ResponseEntity.unprocessableEntity().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable("id") final Integer id) {
        Optional<Dish> dishOptional = dishRepository.findByIdWithJoin(id);
        if (dishOptional.isPresent()
                && dishOptional.get().getMenu().getActionDate().equals(DateUtil.getTomorrow())
                && dishRepository.delete(id) != 0) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.unprocessableEntity().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity updateById(@PathVariable("id") Integer id, @RequestBody final Dish dish) {
        Optional<Dish> optionalDish = dishRepository.findByIdWithJoin(id);
        if (optionalDish.isPresent() && optionalDish.get().getMenu().getActionDate().equals(DateUtil.getTomorrow())) {
            dishRepository.save(updateDish(optionalDish.get(), dish));
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.unprocessableEntity().build();
        }
    }
}
