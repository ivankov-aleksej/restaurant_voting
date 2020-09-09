package com.example.restaurant_voting.web.rest;

import com.example.restaurant_voting.model.Menu;
import com.example.restaurant_voting.repository.MenuRepository;
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

@RestController
@RequestMapping(value = MenuController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class MenuController {
    static final String REST_URL = "/api/menus";

    private final MenuRepository menuRepository;

    public MenuController(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    @GetMapping
    public ResponseEntity<Page<Menu>> getAll(Pageable pageable) {
        Page<Menu> menus = menuRepository.findAllWithJoin(pageable);
        return new ResponseEntity<>(menus, HttpStatus.OK);
    }

    @GetMapping("/current")
    public ResponseEntity<Page<Menu>> getCurrentDate(Pageable pageable) {
        Page<Menu> menus = menuRepository.findByDateWithJoin(DateUtil.getDate(), pageable);
        return new ResponseEntity<>(menus, HttpStatus.OK);
    }

    @GetMapping("/byDate")
    public ResponseEntity<Page<Menu>> getByDate(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date, Pageable pageable) {
        Page<Menu> menus = menuRepository.findByDateWithJoin(date, pageable);
        return new ResponseEntity<>(menus, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Menu> getById(@PathVariable("id") Integer id) {
        Optional<Menu> menuOptional = menuRepository.findByIdWithJoin(id);
        if (menuOptional.isPresent()) {
            return ResponseEntity.ok(menuOptional.get());
        }
        return ResponseEntity.unprocessableEntity().build();

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") final Integer id) {
        if (menuRepository.deleteWithDate(DateUtil.getTomorrow(), id) != 0) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.unprocessableEntity().build();
    }
}
