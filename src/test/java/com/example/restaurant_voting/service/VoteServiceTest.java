package com.example.restaurant_voting.service;

import com.example.restaurant_voting.model.Menu;
import com.example.restaurant_voting.model.User;
import com.example.restaurant_voting.model.Vote;
import com.example.restaurant_voting.repository.MenuRepository;
import com.example.restaurant_voting.repository.UserRepository;
import com.example.restaurant_voting.repository.VoteRepository;
import com.example.restaurant_voting.util.DateUtil;
import com.example.restaurant_voting.util.exception.NotFoundException;
import com.example.restaurant_voting.util.exception.TimeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mockStatic;

@ExtendWith(MockitoExtension.class)
class VoteServiceTest {

    private final static LocalDate TOMORROW_DATE = LocalDate.parse("2020-08-03");
    private final static LocalDate CURRENT_DATE = LocalDate.parse("2020-08-02");
    private final static LocalDate YESTERDAY_DATE = LocalDate.parse("2020-08-01");
    private LocalTime CURRENT_TIME = LocalTime.parse("10:00");
    private LocalTime EXPIRED_TIME = LocalTime.parse("12:00");
    private LocalTime EXPIRED_TIME_TEMPLATE = LocalTime.parse("11:00");
    private final int userOneId = 100;
    private final int userTwoId = 101;
    private final int menuIdOneCurrent = 50;
    private final int menuIdTwoCurrent = 40;
    private final int menuIdThreeCurrent = 30;

    private VoteService voteService;

    @BeforeEach
    void setUp(@Mock VoteRepository voteRepository, @Mock MenuRepository menuRepository, @Mock UserRepository userRepository) {
        Menu menuOneCurrent = new Menu();
        menuOneCurrent.setId(menuIdOneCurrent);
        menuOneCurrent.setActionDate(CURRENT_DATE);

        Menu menuTwoCurrent = new Menu();
        menuTwoCurrent.setId(menuIdTwoCurrent);
        menuTwoCurrent.setActionDate(CURRENT_DATE);

        Menu menuThreeCurrent = new Menu();
        menuThreeCurrent.setId(menuIdThreeCurrent);
        menuThreeCurrent.setActionDate(YESTERDAY_DATE);

        Vote voteUpdate = new Vote();
        voteUpdate.setId(120);
        voteUpdate.setDate(CURRENT_DATE);
        voteUpdate.setMenu(menuTwoCurrent);

        Vote vote = new Vote();
        vote.setId(120);
        vote.setDate(CURRENT_DATE);
        vote.setMenu(menuOneCurrent);


        User user1 = new User();
        user1.setId(userOneId);
        User user2 = new User();
        user2.setId(userTwoId);

        Mockito.lenient().when(voteRepository.findByDateJoin(eq(CURRENT_DATE), eq(userOneId))).thenReturn(List.of(vote));
        Mockito.lenient().when(voteRepository.findByDateJoin(eq(TOMORROW_DATE), eq(userOneId))).thenReturn(List.of());

        Mockito.lenient().when(voteRepository.findByDate(eq(CURRENT_DATE), eq(userOneId))).thenReturn(List.of());
        Mockito.lenient().when(voteRepository.findByDate(eq(CURRENT_DATE), eq(userTwoId))).thenReturn(List.of(vote));


        Mockito.lenient().when(voteRepository.delete(eq(CURRENT_DATE), eq(userOneId))).thenReturn(1);
        Mockito.lenient().when(voteRepository.delete(eq(CURRENT_DATE), eq(userTwoId))).thenReturn(0);
        Mockito.lenient().when(voteRepository.delete(eq(TOMORROW_DATE), eq(userOneId))).thenReturn(0);

        Mockito.lenient().when(menuRepository.findByIdWithJoinRestaurant(eq(menuIdOneCurrent))).thenReturn(Optional.of(menuOneCurrent));
        Mockito.lenient().when(menuRepository.findByIdWithJoinRestaurant(eq(menuIdTwoCurrent))).thenReturn(Optional.of(menuTwoCurrent));

        Mockito.lenient().when(menuRepository.findByIdWithJoinRestaurant(eq(menuIdTwoCurrent))).thenReturn(Optional.of(menuTwoCurrent));

        Mockito.lenient().when(userRepository.getOne(anyInt())).thenReturn(new User());
        Mockito.lenient().when(userRepository.getOne(userOneId)).thenReturn(user1);
        Mockito.lenient().when(userRepository.getOne(userTwoId)).thenReturn(user2);

        Mockito.lenient().when(voteRepository.save(any(Vote.class))).thenReturn(voteUpdate);

        voteService = new VoteService(voteRepository, menuRepository, userRepository);
        //    https://www.baeldung.com/spring-reflection-test-utils
        ReflectionTestUtils.setField(voteService, "expiredTime", EXPIRED_TIME_TEMPLATE);
    }

    @Test
    @SuppressWarnings("unchecked")
    void delete() {
        assertNotEquals(CURRENT_DATE, DateUtil.getDate());
        try (MockedStatic mocked = mockStatic(DateUtil.class)) {
            mocked.when(DateUtil::getDate).thenReturn(CURRENT_DATE);
            mocked.when(DateUtil::getTime).thenReturn(CURRENT_TIME);
            assertEquals(CURRENT_DATE, DateUtil.getDate());

            voteService.delete(userOneId);
        }
        assertNotEquals(CURRENT_DATE, DateUtil.getDate());
    }

    @Test
    @SuppressWarnings("unchecked")
    void deleteNotCurrentDate() {
        assertNotEquals(TOMORROW_DATE, DateUtil.getDate());
        try (MockedStatic mocked = mockStatic(DateUtil.class)) {
            mocked.when(DateUtil::getDate).thenReturn(TOMORROW_DATE);
            mocked.when(DateUtil::getTime).thenReturn(CURRENT_TIME);
            assertEquals(TOMORROW_DATE, DateUtil.getDate());

            assertThrows(NotFoundException.class, () -> voteService.delete(userOneId));
        }
        assertNotEquals(TOMORROW_DATE, DateUtil.getDate());
    }

    @Test
    @SuppressWarnings("unchecked")
    void deleteNotCurrentTime() {
        assertNotEquals(CURRENT_DATE, DateUtil.getDate());
        try (MockedStatic mocked = mockStatic(DateUtil.class)) {
            mocked.when(DateUtil::getDate).thenReturn(CURRENT_DATE);
            mocked.when(DateUtil::getTime).thenReturn(EXPIRED_TIME);
            assertEquals(CURRENT_DATE, DateUtil.getDate());

            assertThrows(TimeException.class, () -> voteService.delete(userOneId));
        }
        assertNotEquals(CURRENT_DATE, DateUtil.getDate());
    }

    @Test
    @SuppressWarnings("unchecked")
    void saveAsCreate() {
        assertNotEquals(CURRENT_DATE, DateUtil.getDate());
        try (MockedStatic mocked = mockStatic(DateUtil.class)) {
            mocked.when(DateUtil::getDate).thenReturn(CURRENT_DATE);
            mocked.when(DateUtil::getTime).thenReturn(CURRENT_TIME);
            assertEquals(CURRENT_DATE, DateUtil.getDate());

            assertEquals(menuIdTwoCurrent, voteService.save(menuIdTwoCurrent, userOneId).getMenu().getId());
        }
        assertNotEquals(CURRENT_DATE, DateUtil.getDate());
    }

    @Test
    @SuppressWarnings("unchecked")
    void saveUpdate() {
        assertNotEquals(CURRENT_DATE, DateUtil.getDate());
        try (MockedStatic mocked = mockStatic(DateUtil.class)) {
            mocked.when(DateUtil::getDate).thenReturn(CURRENT_DATE);
            mocked.when(DateUtil::getTime).thenReturn(CURRENT_TIME);
            assertEquals(CURRENT_DATE, DateUtil.getDate());

            assertEquals(menuIdTwoCurrent, voteService.save(menuIdTwoCurrent, userTwoId).getMenu().getId());
        }
        assertNotEquals(CURRENT_DATE, DateUtil.getDate());
    }

    @Test
    @SuppressWarnings("unchecked")
    void saveNotCurrentMenu() {
        assertNotEquals(CURRENT_DATE, DateUtil.getDate());
        try (MockedStatic mocked = mockStatic(DateUtil.class)) {
            mocked.when(DateUtil::getDate).thenReturn(CURRENT_DATE);
            mocked.when(DateUtil::getTime).thenReturn(CURRENT_TIME);
            assertEquals(CURRENT_DATE, DateUtil.getDate());

            assertThrows(NotFoundException.class, () -> voteService.save(menuIdThreeCurrent, userOneId));
        }
        assertNotEquals(CURRENT_DATE, DateUtil.getDate());
    }

    @Test
    void getByDate() {
        assertTrue(voteService.getByDate(CURRENT_DATE, userOneId).isPresent());
    }

    @Test
    void getByDateNotFound() {
        assertTrue(voteService.getByDate(TOMORROW_DATE, userOneId).isEmpty());
    }
}