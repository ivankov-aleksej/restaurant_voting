package com.example.restaurant_voting.web.rest;

import com.example.restaurant_voting.AuthUser;
import com.example.restaurant_voting.config.WebSecurityConfig;
import com.example.restaurant_voting.model.Role;
import com.example.restaurant_voting.model.User;
import com.example.restaurant_voting.repository.UserRepository;
import com.example.restaurant_voting.util.ValidationUtil;
import com.example.restaurant_voting.util.exception.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;

@RestController
@RequestMapping(value = AccountController.REST_URL)
@AllArgsConstructor
public class AccountController {
    static final String REST_URL = "/api/account";

    private final UserRepository userRepository;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> get(@AuthenticationPrincipal AuthUser authUser) {
        return ResponseEntity.ok(userRepository.findById(authUser.getId())
                .orElseThrow(() -> new NotFoundException(String.valueOf(authUser.getId()))));
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@AuthenticationPrincipal AuthUser authUser) {
        userRepository.deleteById(authUser.getId());
    }

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<User> register(@Valid @RequestBody User user) {
        ValidationUtil.checkNew(user);
        user.setRoles(Set.of(Role.USER));
        user.setPassword(WebSecurityConfig.PASSWORD_ENCODER.encode(user.getPassword()));
        return ResponseEntity.status(HttpStatus.CREATED).body(userRepository.save(user));
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody User user, @AuthenticationPrincipal AuthUser authUser) {
        userRepository.findById(authUser.getId()).ifPresent(oldUser -> {
            ValidationUtil.assureIdConsistent(user, oldUser.id());
            user.setRoles(oldUser.getRoles());
            if (user.getPassword() == null) {
                user.setPassword(oldUser.getPassword());
            }
        });
        userRepository.save(user);
    }
}