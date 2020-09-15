package com.example.restaurant_voting.web.rest;

import com.example.restaurant_voting.service.VoteService;
import com.example.restaurant_voting.util.DateUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalTime;

import static com.example.restaurant_voting.TestUtil.userHttpBasic;
import static com.example.restaurant_voting.UserTestData.USER1;
import static com.example.restaurant_voting.UserTestData.USER2;
import static org.mockito.Mockito.mockStatic;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class VoteControllerTest extends AbstractControllerTest {
    private static final String REST_URL = VoteController.REST_URL + '/';
    private static final String REST_URL_BY_DATE = REST_URL + "byDate?date=2020-08-02";
    private final static Integer MENU_ID = 108;  // CURRENT_DATE == Menu.createdOn for this id
    private final static Integer YESTERDAY_MENU_ID = 105;
    private static final String PACKAGE_JSON = "vote/";


    @Autowired
    private VoteService voteService;

    private LocalTime CURRENT_TIME;
    private LocalTime EXPIRED_TIME;

    @BeforeEach
    void setUp() {
        CURRENT_TIME = voteService.getExpiredTime().minusHours(1);
        EXPIRED_TIME = voteService.getExpiredTime().plusHours(1);
    }

    @Test
    void update() throws Exception {
        try (MockedStatic mocked = mockStatic(DateUtil.class)) {
            mockedDateUtil(mocked, CURRENT_DATE, CURRENT_TIME);

            perform(MockMvcRequestBuilders.post(MenuControllerTest.REST_URL + MENU_ID + "/votes")
                    .with(userHttpBasic(USER1)))
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(content().json(readFile(PACKAGE_JSON + "voteUpdated.json"), true));
        }
    }

    @Test
    void create() throws Exception {
        try (MockedStatic mocked = mockStatic(DateUtil.class)) {
            mockedDateUtil(mocked, CURRENT_DATE, CURRENT_TIME);

            perform(MockMvcRequestBuilders.post(MenuControllerTest.REST_URL + MENU_ID + "/votes")
                    .with(userHttpBasic(USER2)))
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(content().json(readFile(PACKAGE_JSON + "voteCreated.json"), true));
        }
    }

    @Test
    void createAfterExpiredTime() throws Exception {
        try (MockedStatic mocked = mockStatic(DateUtil.class)) {
            mockedDateUtil(mocked, CURRENT_DATE, EXPIRED_TIME);

            perform(MockMvcRequestBuilders.post(MenuControllerTest.REST_URL + MENU_ID + "/votes")
                    .with(userHttpBasic(USER1)))
                    .andExpect(status().isUnprocessableEntity())
                    .andDo(print())
                    .andExpect(content().json(readFile(PACKAGE_JSON + "voteExceptionExpiredTime.json"), true));
        }
    }

    @Test
    void createMenuNotCurrentDate() throws Exception {
        try (MockedStatic mocked = mockStatic(DateUtil.class)) {
            mockedDateUtil(mocked, CURRENT_DATE, CURRENT_TIME);

            perform(MockMvcRequestBuilders.post(MenuControllerTest.REST_URL + YESTERDAY_MENU_ID + "/votes")
                    .with(userHttpBasic(USER1)))
                    .andExpect(status().isUnprocessableEntity())
                    .andDo(print())
                    .andExpect(content().json(readFile(PACKAGE_JSON + "voteExceptionMenuNotCurrentDate.json"), true));
        }
    }

    @Test
    void delete() throws Exception {
        try (MockedStatic mocked = mockStatic(DateUtil.class)) {
            mockedDateUtil(mocked, CURRENT_DATE, CURRENT_TIME);

            perform(MockMvcRequestBuilders.delete(REST_URL)
                    .with(userHttpBasic(USER1)))
                    .andDo(print())
                    .andExpect(status().isNoContent());
        }
    }

    @Test
    void deleteAfterExpiredTime() throws Exception {
        try (MockedStatic mocked = mockStatic(DateUtil.class)) {
            mockedDateUtil(mocked, CURRENT_DATE, EXPIRED_TIME);

            perform(MockMvcRequestBuilders.delete(REST_URL)
                    .with(userHttpBasic(USER1)))
                    .andExpect(status().isUnprocessableEntity())
                    .andDo(print())
                    .andExpect(content().json(readFile(PACKAGE_JSON + "voteExceptionDeleteExpiredTime.json"), true));
        }
    }

    @Test
    void deleteNotFoundCurrentDate() throws Exception {
        try (MockedStatic mocked = mockStatic(DateUtil.class)) {
            mockedDateUtil(mocked, TOMORROW_DATE, CURRENT_TIME);

            perform(MockMvcRequestBuilders.delete(REST_URL)
                    .with(userHttpBasic(USER1)))
                    .andExpect(status().isUnprocessableEntity())
                    .andDo(print())
                    .andExpect(content().json(readFile(PACKAGE_JSON + "voteExceptionNotFoundWithCurrentDate.json"), true));
        }
    }

    @Test
    void getCurrent() throws Exception {
        try (MockedStatic mocked = mockStatic(DateUtil.class)) {
            mockedDateUtil(mocked, CURRENT_DATE);

            perform(MockMvcRequestBuilders.get(REST_URL + "current")
                    .with(userHttpBasic(USER1)))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content()
                            .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(content().json(readFile(PACKAGE_JSON + "voteCurrentOrByDate.json"), true));
        }
    }

    @Test
    void getByDate() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_BY_DATE)
                .with(userHttpBasic(USER1)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(readFile(PACKAGE_JSON + "voteCurrentOrByDate.json"), true));
    }

    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(USER1)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(readFile(PACKAGE_JSON + "voteAll.json"), true));
    }
}