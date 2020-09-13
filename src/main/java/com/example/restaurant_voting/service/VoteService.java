package com.example.restaurant_voting.service;

import com.example.restaurant_voting.model.Menu;
import com.example.restaurant_voting.model.Vote;
import com.example.restaurant_voting.repository.MenuRepository;
import com.example.restaurant_voting.repository.UserRepository;
import com.example.restaurant_voting.repository.VoteRepository;
import com.example.restaurant_voting.to.VoteTo;
import com.example.restaurant_voting.util.DateUtil;
import com.example.restaurant_voting.util.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static com.example.restaurant_voting.util.ValidationUtil.*;

@Service
public class VoteService {

    private static final String NOT_FOUND_CURRENT_DATE = "current date";
    private final VoteRepository voteRepository;
    private final MenuRepository menuRepository;
    private final UserRepository userRepository;

    @Value("#{T(java.time.LocalTime).parse('${time-expired}', T(java.time.format.DateTimeFormatter).ofPattern('HH:mm'))}")
    private LocalTime expiredTime;

    public VoteService(VoteRepository voteRepository, MenuRepository menuRepository, UserRepository userRepository) {
        this.voteRepository = voteRepository;
        this.menuRepository = menuRepository;
        this.userRepository = userRepository;
    }

    public LocalTime getExpiredTime() {
        return expiredTime;
    }

    public void delete(int userId) {
        checkExpiredTime(DateUtil.getTime(), expiredTime);
        checkNotFound(voteRepository.delete(DateUtil.getDate(), userId) != 0, NOT_FOUND_CURRENT_DATE);
    }

    public Page<Vote> getAll(Pageable pageable, int userId) {
        return voteRepository.findAll(pageable, userId);
    }

    public Optional<Vote> getByDate(LocalDate date, int userId) {
        List<Vote> votes = voteRepository.findByDateJoin(date, userId);
        if (votes.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(votes.get(0));
    }

    @Transactional
    public Vote save(int menuId, int userId) {
        checkExpiredTime(DateUtil.getTime(), expiredTime);

        Optional<Menu> menuOptional = menuRepository.findByIdWithJoinRestaurant(menuId);
        Menu menu = menuOptional.orElseThrow(() -> new NotFoundException("id=" + menuId));
        checkCurrentDate(menu.getActionDate(), menuId);

        List<Vote> votes = voteRepository.findByDate(DateUtil.getDate(), userId);

        if (votes.isEmpty()) {
            return voteRepository.save(new Vote(menu, userRepository.getOne(userId)));
        }
        Vote vote = votes.get(0);
        vote.setMenu(menu);
        return vote;
    }

    public Page<VoteTo> getStatisticCurrent(Pageable pageable) {
        return voteRepository.getStatisticByDate(DateUtil.getDate(), pageable);
    }

    public Page<VoteTo> getStatisticByDate(LocalDate date, Pageable pageable) {
        return voteRepository.getStatisticByDate(date, pageable);
    }

    public Page<VoteTo> getStatisticAll(Pageable pageable) {
        return voteRepository.getStatisticAll(pageable);
    }
}
