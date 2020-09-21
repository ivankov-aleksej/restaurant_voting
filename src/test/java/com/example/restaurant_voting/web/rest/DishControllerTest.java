package com.example.restaurant_voting.web.rest;

import com.example.restaurant_voting.util.DateUtil;
import com.example.restaurant_voting.util.exception.DateException;
import com.example.restaurant_voting.util.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.example.restaurant_voting.TestUtil.userHttpBasic;
import static com.example.restaurant_voting.UserTestData.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mockStatic;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class DishControllerTest extends AbstractControllerTest {
    private static final String REST_URL = DishController.REST_URL + '/';
    private static final String REST_URL_BY_DATE = REST_URL + "byDate?date=2020-08-01";
    private static final String REST_URL_BY_NAME = REST_URL + "byName?name=роЛ";
    private static final String PACKAGE_JSON = "dish/";

    private static final Integer DISH_ID = 114;

    /**
     * Warning. Remember, delete or update or create only if DATE menu is FUTURE.
     */

    @Autowired
    DishController controller;

    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(readFile(PACKAGE_JSON + "dishAll.json"), true));
    }

    @Test
    void getByDate() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_BY_DATE)
                .with(userHttpBasic(USER1)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(readFile(PACKAGE_JSON + "dishByDate.json"), true));
    }

    @Test
    void getByName() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_BY_NAME)
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(readFile(PACKAGE_JSON + "dishByName.json"), true));
    }

    @Test
    void getById() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + DISH_ID)
                .with(userHttpBasic(USER1)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(readFile(PACKAGE_JSON + "dish.json"), true));
    }

    @Test
    void getByIdNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + NOT_FOUND_ID)
                .with(userHttpBasic(USER1)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(content().json(readFile(PACKAGE_JSON + "dishExceptionNotFound.json"), true));

        assertThrows(NotFoundException.class, () -> controller.getById(NOT_FOUND_ID));
    }

    @Test
    void deleteWithTomorrowDateMenu() throws Exception {
        try (MockedStatic mocked = mockStatic(DateUtil.class)) {
            mockedDateUtil(mocked, YESTERDAY_DATE, CURRENT_DATE);

            perform(MockMvcRequestBuilders.delete(REST_URL + DISH_ID)
                    .with(userHttpBasic(ADMIN)))
                    .andDo(print())
                    .andExpect(status().isNoContent());
        }
    }

    @Test
    void deleteWithNotTomorrowDateMenu() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + DISH_ID)
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print())
                .andExpect(content().json(readFile(PACKAGE_JSON + "dishExceptionExpiredDate.json"), true));

        assertThrows(DateException.class, () -> controller.delete(DISH_ID));
    }

    @Test
    void deleteWithTomorrowDateMenuForbidden() throws Exception {
        try (MockedStatic mocked = mockStatic(DateUtil.class)) {
            mockedDateUtil(mocked, YESTERDAY_DATE, CURRENT_DATE);

            perform(MockMvcRequestBuilders.delete(REST_URL + DISH_ID)
                    .with(userHttpBasic(USER2)))
                    .andDo(print())
                    .andExpect(status().isForbidden());
        }
    }

    @Test
    void updateWithTomorrowDateMenu() throws Exception {
        try (MockedStatic mocked = mockStatic(DateUtil.class)) {
            mockedDateUtil(mocked, YESTERDAY_DATE, CURRENT_DATE);

            perform(MockMvcRequestBuilders.put(REST_URL + DISH_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(readFile(PACKAGE_JSON + "dishCreateOrUpdate.json"))
                    .with(userHttpBasic(ADMIN)))
                    .andDo(print())
                    .andExpect(status().isNoContent());
        }
    }

    @Test
    void updateWithNotNextDateMenu() throws Exception {
        perform(MockMvcRequestBuilders.put(REST_URL + DISH_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(readFile(PACKAGE_JSON + "dishCreateOrUpdate.json"))
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print())
                .andExpect(content().json(readFile(PACKAGE_JSON + "dishExceptionUpdateExpiredDate.json"), true));
    }

    @Test
    void updateWithTomorrowDateMenuForbidden() throws Exception {
        try (MockedStatic mocked = mockStatic(DateUtil.class)) {
            mockedDateUtil(mocked, YESTERDAY_DATE, CURRENT_DATE);

            perform(MockMvcRequestBuilders.put(REST_URL + DISH_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(readFile(PACKAGE_JSON + "dishCreateOrUpdate.json"))
                    .with(userHttpBasic(USER1)))
                    .andDo(print())
                    .andExpect(status().isForbidden());
        }
    }

    @Test
    void createWithTomorrowDateMenu() throws Exception {
        try (MockedStatic mocked = mockStatic(DateUtil.class)) {
            mockedDateUtil(mocked, YESTERDAY_DATE, CURRENT_DATE);

            perform(MockMvcRequestBuilders.post(MenuControllerTest.REST_URL + MenuControllerTest.MENU_ID + "/dishes")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(readFile(PACKAGE_JSON + "dishCreateOrUpdate.json"))
                    .with(userHttpBasic(ADMIN)))
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(content().json(readFile(PACKAGE_JSON + "dishCreated.json"), true));
        }
    }

    @Test
    void createWithNotTomorrowDateMenu() throws Exception {
        perform(MockMvcRequestBuilders.post(MenuControllerTest.REST_URL + MenuControllerTest.MENU_ID + "/dishes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(readFile(PACKAGE_JSON + "dishCreateOrUpdate.json"))
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print())
                .andExpect(content().json(readFile(PACKAGE_JSON + "dishExceptionCreateExpiredDate.json"), true));
    }

    @Test
    void createWithNotFoundMenu() throws Exception {
        perform(MockMvcRequestBuilders.post(MenuControllerTest.REST_URL + NOT_FOUND_ID + "/dishes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(readFile(PACKAGE_JSON + "dishCreateOrUpdate.json"))
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print())
                .andExpect(content().json(readFile(PACKAGE_JSON + "dishExceptionCreateNotFoundMenu.json"), true));
    }

    @Test
    void createWithTomorrowDateMenuForbidden() throws Exception {
        try (MockedStatic mocked = mockStatic(DateUtil.class)) {
            mockedDateUtil(mocked, YESTERDAY_DATE, CURRENT_DATE);

            perform(MockMvcRequestBuilders.post(MenuControllerTest.REST_URL + MenuControllerTest.MENU_ID + "/dishes")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(readFile(PACKAGE_JSON + "dishCreateOrUpdate.json"))
                    .with(userHttpBasic(USER2)))
                    .andDo(print())
                    .andExpect(status().isForbidden());
        }
    }
}