package com.example.restaurant_voting.web.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
abstract class AbstractControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    protected static String readFile(String fileName) throws IOException {
        File file = ResourceUtils.getFile("classpath:json/" + fileName);
        return Files.readString(file.toPath(), StandardCharsets.UTF_8);
    }
}
