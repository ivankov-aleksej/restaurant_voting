package com.example.restaurant_voting.web.rest;

import com.example.restaurant_voting.service.VoteService;
import com.example.restaurant_voting.to.VoteTo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping(value = StatisticController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class StatisticController {
    static final String REST_URL = "/api/statistic";

    private final VoteService voteService;

    public StatisticController(VoteService voteService) {
        this.voteService = voteService;
    }

    @GetMapping("/current")
    public ResponseEntity<Page<VoteTo>> getCurrent(Pageable pageable) {
        return ResponseEntity.ok(voteService.getStatisticCurrent(pageable));
    }

    @GetMapping("/byDate")
    public ResponseEntity<Page<VoteTo>> getByDate(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date, Pageable pageable) {
        return ResponseEntity.ok(voteService.getStatisticByDate(date, pageable));
    }

    @GetMapping
    public ResponseEntity<Page<VoteTo>> getAll(Pageable pageable) {
        Page<VoteTo> statisticAll = voteService.getStatisticAll(pageable);
        return ResponseEntity.ok(statisticAll);
    }
}
