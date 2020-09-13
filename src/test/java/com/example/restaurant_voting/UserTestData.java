package com.example.restaurant_voting;

import com.example.restaurant_voting.model.Role;
import com.example.restaurant_voting.model.User;

public class UserTestData {

    private static final int START_SEQ = 100;

    private static final int ADMIN_ID = START_SEQ;
    private static final int USER1_ID = START_SEQ + 1;
    private static final int USER2_ID = START_SEQ + 2;

    public static final User ADMIN = new User(ADMIN_ID, "admin@gmail.com", "admin", Role.ADMIN, Role.USER);
    public static final User USER1 = new User(USER1_ID, "user1@gmail.com", "password", Role.USER);
    public static final User USER2 = new User(USER2_ID, "user2@gmail.com", "password", Role.USER);
}
