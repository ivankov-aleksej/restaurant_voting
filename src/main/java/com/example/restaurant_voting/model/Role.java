package com.example.restaurant_voting.model;

import org.springframework.security.core.GrantedAuthority;

//https://stackoverflow.com/a/19542316/548473
public enum Role implements GrantedAuthority {
    USER,
    ADMIN;

    @Override
    public String getAuthority() {
        return "ROLE_" + name();
    }
}
