package com.example.restaurant_voting.web.rest;

import com.example.restaurant_voting.service.VoteService;
import com.example.restaurant_voting.util.DateUtil;
import org.awaitility.Awaitility;
import org.awaitility.Durations;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.time.LocalTime;

import static com.example.restaurant_voting.TestUtil.userHttpBasic;
import static com.example.restaurant_voting.UserTestData.USER1;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.mockStatic;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class VoteControllerTest extends AbstractControllerTest {
    private static final String REST_URL = VoteController.REST_URL + '/';
    private static final String REST_URL_BY_DATE = REST_URL + "byDate?date=2020-08-02";
    private final static Integer FOUR_MENU_ID = 108;  // CURRENT_DATE == Menu.createdOn for this id
    private final static LocalDate CURRENT_DATE = LocalDate.parse("2020-08-02");
    @Autowired
    private VoteService voteService;
    private LocalTime CURRENT_TIME;
    private LocalTime EXPIRED_TIME;

    private void checkTime() {
        // Even a Broken Clock is Right Twice a Day  :)
        if (CURRENT_TIME.equals(DateUtil.getTime())) {
            Awaitility.await().pollDelay(Durations.ONE_SECOND).until(() -> true);
        }
    }

    @BeforeEach
    void setUp() {
        CURRENT_TIME = voteService.getExpiredTime().minusHours(1);
        EXPIRED_TIME = voteService.getExpiredTime().plusHours(1);
    }

    //    https://stackoverflow.com/questions/52830441/junit5-mock-a-static-method
    //    https://javadoc.io/static/org.mockito/mockito-core/3.4.6/org/mockito/Mockito.html#static_mocks
    @Test
    @SuppressWarnings("unchecked")
    void create() throws Exception {
        assertNotEquals(CURRENT_DATE, DateUtil.getDate());
        checkTime();
        assertNotEquals(CURRENT_TIME, DateUtil.getTime());
        try (MockedStatic mocked = mockStatic(DateUtil.class)) {
            mocked.when(DateUtil::getDate).thenReturn(CURRENT_DATE);
            mocked.when(DateUtil::getTime).thenReturn(CURRENT_TIME);
            assertEquals(CURRENT_DATE, DateUtil.getDate());
            assertEquals(CURRENT_TIME, DateUtil.getTime());

            perform(MockMvcRequestBuilders.post(REST_URL + FOUR_MENU_ID)
                    .with(userHttpBasic(USER1)))
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        }
        assertNotEquals(CURRENT_DATE, DateUtil.getDate());
        checkTime();
        assertNotEquals(CURRENT_TIME, DateUtil.getTime());
    }

    @Test
    @SuppressWarnings("unchecked")
    void delete() throws Exception {
        assertNotEquals(CURRENT_DATE, DateUtil.getDate());
        checkTime();
        assertNotEquals(CURRENT_TIME, DateUtil.getTime());
        try (MockedStatic mocked = mockStatic(DateUtil.class)) {
            mocked.when(DateUtil::getDate).thenReturn(CURRENT_DATE);
            mocked.when(DateUtil::getTime).thenReturn(CURRENT_TIME);
            assertEquals(CURRENT_DATE, DateUtil.getDate());
            assertEquals(CURRENT_TIME, DateUtil.getTime());

            perform(MockMvcRequestBuilders.delete(REST_URL)
                    .with(userHttpBasic(USER1)))
                    .andDo(print())
                    .andExpect(status().isNoContent());
        }
        assertNotEquals(CURRENT_DATE, DateUtil.getDate());
        checkTime();
        assertNotEquals(CURRENT_TIME, DateUtil.getTime());
    }

    @Test
    @SuppressWarnings("unchecked")
    void getCurrent() throws Exception {
        assertNotEquals(CURRENT_DATE, DateUtil.getDate());
        try (MockedStatic mocked = mockStatic(DateUtil.class)) {
            mocked.when(DateUtil::getDate).thenReturn(CURRENT_DATE);
            assertEquals(CURRENT_DATE, DateUtil.getDate());

            perform(MockMvcRequestBuilders.get(REST_URL + "current")
                    .with(userHttpBasic(USER1)))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content()
                            .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        }
        assertNotEquals(CURRENT_DATE, DateUtil.getDate());
    }

    @Test
    void getByDate() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_BY_DATE)
                .with(userHttpBasic(USER1)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(USER1)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }
}