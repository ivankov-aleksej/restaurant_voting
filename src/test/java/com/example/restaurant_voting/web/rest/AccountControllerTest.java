package com.example.restaurant_voting.web.rest;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.example.restaurant_voting.TestUtil.userHttpBasic;
import static com.example.restaurant_voting.UserTestData.USER1;
import static com.example.restaurant_voting.UserTestData.USER2;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AccountControllerTest extends AbstractControllerTest {
    private static final String REST_URL = AccountController.REST_URL + '/';
    private static final String PACKAGE_JSON = "account/";

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(USER1)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(readFile(PACKAGE_JSON + "account_user_1.json")));
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL)
                .with(userHttpBasic(USER2)))
                .andExpect(status().isNoContent())
                .andDo(print());

        perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(USER2)))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    void register() throws Exception {
        perform(MockMvcRequestBuilders.post(REST_URL + "register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(readFile(PACKAGE_JSON + "account_registration.json")))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json(readFile(PACKAGE_JSON + "account_new_registered.json")));
    }

    @Test
    void update() throws Exception {
        perform(MockMvcRequestBuilders.put(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(readFile(PACKAGE_JSON + "account_registration.json"))
                .with(userHttpBasic(USER1)))
                .andExpect(status().isNoContent())
                .andDo(print());
    }
}