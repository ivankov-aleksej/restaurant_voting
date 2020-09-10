package com.example.restaurant_voting.web.rest;

import com.example.restaurant_voting.util.DateUtil;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;

import static com.example.restaurant_voting.web.rest.RestaurantControllerTest.RESTAURANT_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.mockStatic;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MenuControllerTest extends AbstractControllerTest {
    private static final String REST_URL = MenuController.REST_URL + '/';
    private static final String REST_URL_BY_DATE = REST_URL + "byDate?date=2020-08-01";

    /**
     * Warning. Remember, delete or update or create only if DATE menu is FUTURE.
     */
    private final static LocalDate TOMORROW_DATE = LocalDate.parse("2020-08-03");
    private final static LocalDate CURRENT_DATE = LocalDate.parse("2020-08-02");
    private final static LocalDate YESTERDAY_DATE = LocalDate.parse("2020-08-01");
    private final static Integer MENU_ID = 107;

    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    //    https://stackoverflow.com/questions/52830441/junit5-mock-a-static-method
    @Test
    @SuppressWarnings("unchecked")
    void getCurrentDate() throws Exception {
        assertNotEquals(CURRENT_DATE, DateUtil.getDate());
        try (MockedStatic mocked = mockStatic(DateUtil.class)) {
            mocked.when(DateUtil::getDate).thenReturn(CURRENT_DATE);
            assertEquals(CURRENT_DATE, DateUtil.getDate());

            perform(MockMvcRequestBuilders.get(REST_URL + "current"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content()
                            .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        }
        assertNotEquals(CURRENT_DATE, DateUtil.getDate());
    }

    @Test
    void getByDate() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_BY_DATE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void getById() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + MENU_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    @SuppressWarnings("unchecked")
    void delete() throws Exception {
        assertNotEquals(YESTERDAY_DATE, DateUtil.getDate());
        try (MockedStatic mocked = mockStatic(DateUtil.class)) {
            mocked.when(DateUtil::getDate).thenReturn(YESTERDAY_DATE);
            mocked.when(DateUtil::getTomorrow).thenReturn(CURRENT_DATE);
            assertEquals(YESTERDAY_DATE, DateUtil.getDate());

            perform(MockMvcRequestBuilders.delete(REST_URL + MENU_ID))
                    .andDo(print())
                    .andExpect(status().isNoContent());
        }
        assertNotEquals(YESTERDAY_DATE, DateUtil.getDate());
    }

    @Test
    @SuppressWarnings("unchecked")
    void create() throws Exception {
        assertNotEquals(CURRENT_DATE, DateUtil.getDate());
        try (MockedStatic mocked = mockStatic(DateUtil.class)) {
            mocked.when(DateUtil::getDate).thenReturn(CURRENT_DATE);
            mocked.when(DateUtil::getTomorrow).thenReturn(TOMORROW_DATE);
            assertEquals(CURRENT_DATE, DateUtil.getDate());

            perform(MockMvcRequestBuilders.post(RestaurantController.REST_URL + "/" + RESTAURANT_ID + "/menus")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{}"))
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        }
        assertNotEquals(CURRENT_DATE, DateUtil.getDate());
    }
}