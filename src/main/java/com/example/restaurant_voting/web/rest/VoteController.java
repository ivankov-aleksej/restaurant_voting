package com.example.restaurant_voting.web.rest;

import com.example.restaurant_voting.model.Vote;
import com.example.restaurant_voting.service.VoteService;
import com.example.restaurant_voting.util.DateUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

import static com.example.restaurant_voting.AuthUser.authUserId;

@RestController
@RequestMapping(value = VoteController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class VoteController {
    static final String REST_URL = "/api/votes";

    private final VoteService voteService;

    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete() {
        voteService.delete(authUserId());
    }

    @GetMapping("/current")
    public ResponseEntity<Vote> getCurrent() {
        Vote vote = voteService.getByDate(DateUtil.getDate(), authUserId());
        return ResponseEntity.ok(vote);
    }

    @GetMapping("/byDate")
    public ResponseEntity<Vote> getByDate(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        Vote vote = voteService.getByDate(date, authUserId());
        return ResponseEntity.ok(vote);
    }

    @GetMapping
    public ResponseEntity<Page<Vote>> getAll(Pageable pageable) {
        Page<Vote> all = voteService.getAll(pageable, authUserId());
        return ResponseEntity.ok(all);
    }
}
