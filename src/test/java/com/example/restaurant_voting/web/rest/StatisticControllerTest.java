package com.example.restaurant_voting.web.rest;

import com.example.restaurant_voting.util.DateUtil;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;

import static com.example.restaurant_voting.TestUtil.userHttpBasic;
import static com.example.restaurant_voting.UserTestData.USER1;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.mockStatic;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class StatisticControllerTest extends AbstractControllerTest {
    private static final String REST_URL = StatisticController.REST_URL + "/";
    private static final String REST_URL_BY_DATE = REST_URL + "byDate?date=2020-08-01";
    private LocalDate CURRENT_DATE = LocalDate.parse("2020-08-01");
    private static final String PACKAGE_JSON = "statistic/";

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
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(content().json(readFile(PACKAGE_JSON + "statisticCurrentByDate.json"), true));
        }
        assertNotEquals(CURRENT_DATE, DateUtil.getDate());
    }

    @Test
    void getByDate() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_BY_DATE)
                .with(userHttpBasic(USER1)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(readFile(PACKAGE_JSON + "statisticCurrentByDate.json"), true));
    }

    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(USER1)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(readFile(PACKAGE_JSON + "statisticAll.json"), true));
    }
}