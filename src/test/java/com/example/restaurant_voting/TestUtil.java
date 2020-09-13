package com.example.restaurant_voting;

import com.example.restaurant_voting.model.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TestUtil {

    public static RequestPostProcessor userHttpBasic(User user) {
        return SecurityMockMvcRequestPostProcessors.httpBasic(user.getEmail(), user.getPassword());
    }
}
