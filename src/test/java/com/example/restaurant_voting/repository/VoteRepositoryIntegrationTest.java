package com.example.restaurant_voting.repository;

import com.example.restaurant_voting.model.Vote;
import com.example.restaurant_voting.to.VoteTo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class VoteRepositoryIntegrationTest {
    private final LocalDate DATE = LocalDate.parse("2020-08-01");
    private final LocalDate DATE_NOT_FOUND = LocalDate.parse("2022-08-01");

    @Autowired
    private VoteRepository voteRepository;

    @Test
    void delete() {
        assertEquals(1, voteRepository.delete(DATE, 1));
    }

    @Test
    void deleteNotFound() {
        assertEquals(0, voteRepository.delete(DATE_NOT_FOUND, 1));
    }

    @Test
    void findAll() {
        Page<Vote> votes = voteRepository.findAll(PageRequest.of(0, 20), 1);
        assertEquals(2, votes.getContent().size());
    }

    @Test
    void findByDate() {
        List<Vote> votes = voteRepository.findByDate(DATE, 1);
        assertEquals(1, votes.size());
        assertEquals(DATE, votes.get(0).getDate());
        assertEquals(1, votes.get(0).getUser().getId());
    }

    @Test
    void getStatisticByDate() {
        Page<VoteTo> voteTos = voteRepository.getStatisticByDate(DATE, PageRequest.of(0, 20));
        assertEquals(2, voteTos.getContent().size());
        assertEquals(1, voteTos.getContent().get(0).getCount());
        assertEquals(2, voteTos.getContent().get(1).getCount());
    }

    @Test
    void getStatisticAll() {
        Page<VoteTo> voteTos = voteRepository.getStatisticAll(PageRequest.of(0, 20,
                Sort.by(new Sort.Order(Sort.Direction.DESC, "date"), new Sort.Order(Sort.Direction.ASC, "menu.id"))));
        assertEquals(4, voteTos.getContent().size());
        assertEquals(2, voteTos.getContent().get(0).getCount());
        assertEquals(1, voteTos.getContent().get(1).getCount());
        assertEquals(1, voteTos.getContent().get(2).getCount());
        assertEquals(2, voteTos.getContent().get(3).getCount());

        assertEquals(3, voteTos.getContent().get(0).getMenu().getId());
        assertEquals(4, voteTos.getContent().get(1).getMenu().getId());
        assertEquals(1, voteTos.getContent().get(2).getMenu().getId());
        assertEquals(2, voteTos.getContent().get(3).getMenu().getId());

    }
}