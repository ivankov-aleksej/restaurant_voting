package com.example.restaurant_voting.web.rest;

import com.example.restaurant_voting.util.DateUtil;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.ResourceUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@SpringBootTest
@AutoConfigureMockMvc
@Sql({"/drop.sql", "/data.sql"})
abstract class AbstractControllerTest {

    private static final CharacterEncodingFilter CHARACTER_ENCODING_FILTER = new CharacterEncodingFilter();
    final static Integer NOT_FOUND_ID = 1000;

    final static LocalDate TOMORROW_DATE = LocalDate.parse("2020-08-03");
    final static LocalDate CURRENT_DATE = LocalDate.parse("2020-08-02");
    final static LocalDate YESTERDAY_DATE = LocalDate.parse("2020-08-01");

    static {
        CHARACTER_ENCODING_FILTER.setEncoding("UTF-8");
        CHARACTER_ENCODING_FILTER.setForceEncoding(true);
    }

    @Autowired
    protected MockMvc mockMvc;

    static String readFile(String fileName) throws IOException {
        File file = ResourceUtils.getFile("classpath:json/" + fileName);
        return Files.readString(file.toPath(), StandardCharsets.UTF_8);
    }

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .addFilter(CHARACTER_ENCODING_FILTER)
                .apply(springSecurity())
                .build();
    }

    ResultActions perform(MockHttpServletRequestBuilder builder) throws Exception {
        return mockMvc.perform(builder);
    }

    //    https://stackoverflow.com/questions/52830441/junit5-mock-a-static-method
    //    https://javadoc.io/static/org.mockito/mockito-core/3.4.6/org/mockito/Mockito.html#static_mocks
    @SuppressWarnings("unchecked")
    static void mockedDateUtil(MockedStatic mocked, @NotNull LocalDate current) {
        mocked.when(DateUtil::getDate).thenReturn(current);
        assertEquals(current, DateUtil.getDate());
    }

    @SuppressWarnings("unchecked")
    static void mockedDateUtil(MockedStatic mocked, @NotNull LocalDate current, @NotNull LocalDate tomorrow) {
        mocked.when(DateUtil::getDate).thenReturn(current);
        mocked.when(DateUtil::getTomorrow).thenReturn(tomorrow);
        assertEquals(current, DateUtil.getDate());
        assertEquals(tomorrow, DateUtil.getTomorrow());
    }

    @SuppressWarnings("unchecked")
    static void mockedDateUtil(MockedStatic mocked, @NotNull LocalDate current, @NotNull LocalTime time) {
        mocked.when(DateUtil::getDate).thenReturn(current);
        mocked.when(DateUtil::getTime).thenReturn(time);
        assertEquals(current, DateUtil.getDate());
        assertEquals(time, DateUtil.getTime());
    }

    @SuppressWarnings("unchecked")
    static void mockedDateUtil(MockedStatic mocked, @NotNull LocalDate current, @NotNull LocalDate tomorrow, @NotNull LocalTime time) {
        mocked.when(DateUtil::getDate).thenReturn(current);
        mocked.when(DateUtil::getTomorrow).thenReturn(tomorrow);
        mocked.when(DateUtil::getTime).thenReturn(time);
        assertEquals(current, DateUtil.getDate());
        assertEquals(tomorrow, DateUtil.getTomorrow());
        assertEquals(time, DateUtil.getTime());
    }
}
