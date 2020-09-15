package com.example.restaurant_voting.web.rest;

import com.example.restaurant_voting.util.DateUtil;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.example.restaurant_voting.TestUtil.userHttpBasic;
import static com.example.restaurant_voting.UserTestData.ADMIN;
import static com.example.restaurant_voting.UserTestData.USER1;
import static com.example.restaurant_voting.web.rest.RestaurantControllerTest.RESTAURANT_ID;
import static org.mockito.Mockito.mockStatic;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MenuControllerTest extends AbstractControllerTest {
    static final String REST_URL = MenuController.REST_URL + '/';
    final static Integer MENU_ID = 107;
    private static final String REST_URL_BY_DATE = REST_URL + "byDate?date=2020-08-02";
    private static final String PACKAGE_JSON = "menu/";
    /**
     * Warning. Remember, delete or update only if DATE menu is FUTURE.
     */

    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(readFile(PACKAGE_JSON + "menuAll.json"), true));
    }


    @Test
    void getCurrentDate() throws Exception {
        try (MockedStatic mocked = mockStatic(DateUtil.class)) {
            mockedDateUtil(mocked, CURRENT_DATE);

            perform(MockMvcRequestBuilders.get(REST_URL + "current")
                    .with(userHttpBasic(ADMIN)))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content()
                            .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(content().json(readFile(PACKAGE_JSON + "menuCurrentOrByDate.json"), true));
        }
    }

    @Test
    void getByDate() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_BY_DATE)
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(readFile(PACKAGE_JSON + "menuCurrentOrByDate.json"), true));
    }

    @Test
    void getById() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + MENU_ID)
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(readFile(PACKAGE_JSON + "menuById.json"), true));
    }

    @Test
    void getByIdNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + NOT_FOUND_ID)
                .with(userHttpBasic(USER1)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(content().json(readFile(PACKAGE_JSON + "menuExceptionNotFound.json"), true));
    }

    @Test
    void delete() throws Exception {
        try (MockedStatic mocked = mockStatic(DateUtil.class)) {
            mockedDateUtil(mocked, YESTERDAY_DATE, CURRENT_DATE);

            perform(MockMvcRequestBuilders.delete(REST_URL + MENU_ID)
                    .with(userHttpBasic(ADMIN)))
                    .andDo(print())
                    .andExpect(status().isNoContent());
        }
    }

    @Test
    void deleteNotTomorrowDateMenu() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + MENU_ID)
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print())
                .andExpect(content().json(readFile(PACKAGE_JSON + "menuExceptionDeleteNotTomorrow.json"), true));
    }

    @Test
    void deleteForbidden() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + MENU_ID)
                .with(userHttpBasic(USER1)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    void create() throws Exception {
        try (MockedStatic mocked = mockStatic(DateUtil.class)) {
            mockedDateUtil(mocked, CURRENT_DATE, TOMORROW_DATE);

            perform(MockMvcRequestBuilders.post(RestaurantController.REST_URL + "/" + RESTAURANT_ID + "/menus")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{}")
                    .with(userHttpBasic(ADMIN)))
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(content().json(readFile(PACKAGE_JSON + "menuCreated.json"), true));
        }
    }

    @Test
    void createWithNotFoundRestaurant() throws Exception {
        try (MockedStatic mocked = mockStatic(DateUtil.class)) {
            mockedDateUtil(mocked, CURRENT_DATE);

            perform(MockMvcRequestBuilders.post(RestaurantController.REST_URL + "/" + NOT_FOUND_ID + "/menus")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{}")
                    .with(userHttpBasic(ADMIN)))
                    .andExpect(status().isUnprocessableEntity())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(content().json(readFile(PACKAGE_JSON + "menuExceptionRestaurantNotFound.json"), true));
        }
    }

    @Test
    void createAlreadyExisted() throws Exception {
        try (MockedStatic mocked = mockStatic(DateUtil.class)) {
            mockedDateUtil(mocked, YESTERDAY_DATE, CURRENT_DATE);

            perform(MockMvcRequestBuilders.post(RestaurantController.REST_URL + "/" + RESTAURANT_ID + "/menus")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{}")
                    .with(userHttpBasic(ADMIN)))
                    .andExpect(status().isConflict())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(content().json(readFile(PACKAGE_JSON + "menuExceptionAlreadyExisted.json"), true));
        }
    }

    @Test
    void createForbidden() throws Exception {
        perform(MockMvcRequestBuilders.post(RestaurantController.REST_URL + "/" + RESTAURANT_ID + "/menus")
                .with(userHttpBasic(USER1)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }
}