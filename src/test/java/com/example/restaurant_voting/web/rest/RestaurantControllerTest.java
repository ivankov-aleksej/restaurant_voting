package com.example.restaurant_voting.web.rest;


import com.example.restaurant_voting.util.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.example.restaurant_voting.TestUtil.userHttpBasic;
import static com.example.restaurant_voting.UserTestData.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RestaurantControllerTest extends AbstractControllerTest {
    static final Integer RESTAURANT_ID = 103;
    private static final String REST_URL = RestaurantController.REST_URL + '/';
    private static final String REST_NAME = REST_URL + "byName?name=kiNG";
    private static final String PACKAGE_JSON = "restaurant/";

    @Autowired
    RestaurantController controller;

    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(readFile(PACKAGE_JSON + "restaurantAll.json"), true));
    }

    @Test
    void getByName() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_NAME)
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(readFile(PACKAGE_JSON + "restaurantGetByName.json"), true));
    }

    @Test
    void getById() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + RESTAURANT_ID)
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(readFile(PACKAGE_JSON + "restaurantGetById.json"), true));
    }

    @Test
    void getByIdNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + NOT_FOUND_ID)
                .with(userHttpBasic(USER1)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(readFile(PACKAGE_JSON + "restaurantExceptionNotFound.json"), true));

        assertThrows(NotFoundException.class, () -> controller.getById(NOT_FOUND_ID));
    }

    @Test
    void create() throws Exception {
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(readFile(PACKAGE_JSON + "restaurantCreate.json"))
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(readFile(PACKAGE_JSON + "restaurantCreated.json"), true));

    }

    @Test
    void createNotValidName() throws Exception {
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(readFile(PACKAGE_JSON + "restaurantNotValidName.json"))
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(readFile(PACKAGE_JSON + "restaurantExceptionCreateNotValidName.json"), true));
    }

    @Test
    void createNotValidId() throws Exception {
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(readFile(PACKAGE_JSON + "restaurantUpdate.json"))
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(readFile(PACKAGE_JSON + "restaurantExceptionCreateNotValidId.json"), true));
    }

    @Test
    void createBadSyntax() throws Exception {
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"id\": 1,\"name\": \"\",][]}")
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(readFile(PACKAGE_JSON + "restaurantExceptionBadSyntax.json"), true));
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + RESTAURANT_ID)
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + NOT_FOUND_ID)
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(content().json(readFile(PACKAGE_JSON + "restaurantExceptionNotFound.json"), true));

        assertThrows(NotFoundException.class, () -> controller.delete(NOT_FOUND_ID));
    }

    @Test
    void deleteForbidden() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + RESTAURANT_ID)
                .with(userHttpBasic(USER2)))
                .andDo(print())
                .andExpect(status().isForbidden());

    }

    @Test
    void update() throws Exception {
        perform(MockMvcRequestBuilders.put(REST_URL + RESTAURANT_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(readFile(PACKAGE_JSON + "restaurantUpdate.json"))
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    void updateNotValidName() throws Exception {
        perform(MockMvcRequestBuilders.put(REST_URL + RESTAURANT_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(readFile(PACKAGE_JSON + "restaurantNotValidName.json"))
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))

                .andExpect(content().json(readFile(PACKAGE_JSON + "restaurantExceptionUpdateNotValidName.json"), true));
    }

    @Test
    void updateNotValidId() throws Exception {
        perform(MockMvcRequestBuilders.put(REST_URL + RESTAURANT_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(readFile(PACKAGE_JSON + "restaurantNotValidId.json"))
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))

                .andExpect(content().json(readFile(PACKAGE_JSON + "restaurantExceptionUpdateNotValidId.json"), true));
    }

    @Test
    void updateNotFound() throws Exception {
        perform(MockMvcRequestBuilders.put(REST_URL + NOT_FOUND_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(readFile(PACKAGE_JSON + "restaurantUpdateNotFound.json"))
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(readFile(PACKAGE_JSON + "restaurantExceptionUpdateNotFound.json"), true));

        assertThrows(NotFoundException.class, () -> controller.getById(NOT_FOUND_ID));
    }

    @Test
    void updateForbidden() throws Exception {
        perform(MockMvcRequestBuilders.put(REST_URL + RESTAURANT_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(readFile(PACKAGE_JSON + "restaurantUpdate.json"))
                .with(userHttpBasic(USER1)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }
}