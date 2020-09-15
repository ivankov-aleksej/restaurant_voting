package com.example.restaurant_voting.web.rest;

import com.example.restaurant_voting.View;
import com.example.restaurant_voting.model.Dish;
import com.example.restaurant_voting.model.Menu;
import com.example.restaurant_voting.model.Vote;
import com.example.restaurant_voting.repository.DishRepository;
import com.example.restaurant_voting.repository.MenuRepository;
import com.example.restaurant_voting.service.VoteService;
import com.example.restaurant_voting.util.DateUtil;
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

import static com.example.restaurant_voting.AuthUser.authUserId;
import static com.example.restaurant_voting.util.ValidationUtil.checkExpiredDate;

@RestController
@RequestMapping(value = MenuController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class MenuController {
    static final String REST_URL = "/api/menus";

    private final MenuRepository menuRepository;

    private final DishRepository dishRepository;

    private final VoteService voteService;

    public MenuController(MenuRepository menuRepository,
                          DishRepository dishRepository, VoteService voteService) {
        this.menuRepository = menuRepository;
        this.dishRepository = dishRepository;
        this.voteService = voteService;
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
    public ResponseEntity<Menu> getById(@PathVariable("id") final int id) {
        Optional<Menu> menuOptional = menuRepository.findByIdWithJoin(id);
        return ResponseEntity.ok(menuOptional.orElseThrow(() -> new NotFoundException("id=" + id)));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") final int id) {
        Optional<Menu> menuOptional = menuRepository.findByIdWithJoin(id);
        Menu menu = menuOptional.orElseThrow(() -> new NotFoundException("id=" + id));
        checkExpiredDate(menu.getActionDate(), id);
        menuRepository.deleteById(id);
    }

    @PostMapping("/{id}/dishes")
    public ResponseEntity<Dish> createDish(@PathVariable("id") final int id, @Validated(View.Web.class) @RequestBody Dish dish) {
        Optional<Menu> menuOptional = menuRepository.findById(id);
        if (menuOptional.isPresent() && menuOptional.get().getActionDate().equals(DateUtil.getTomorrow())) {
            dish.setMenu(menuOptional.get());
            return ResponseEntity.status(HttpStatus.CREATED).body(dishRepository.save(dish));
        }
        return ResponseEntity.unprocessableEntity().build();
    }

    @PostMapping("/{id}/votes")
    public ResponseEntity<Vote> createVote(@PathVariable("id") final int menuId) {
        Vote vote = voteService.save(menuId, authUserId());
        return ResponseEntity.status(HttpStatus.CREATED).body(vote);
    }
}
