package com.practice.cafe.dto;

import lombok.Getter;

@Getter
public enum UserRole {
    USER(Authority.USER),
    ADMIN(Authority.ADMIN);
    private final String authority;

    UserRole(String authority) {
        this.authority = authority;
    }

    public static class Authority{
        public static final String USER = "USER";
        public static final String ADMIN = "ADMIN";
    }
}
