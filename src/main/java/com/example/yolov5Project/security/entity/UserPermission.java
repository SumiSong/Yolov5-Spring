package com.example.yolov5Project.security.entity;

import lombok.Getter;

@Getter
public enum UserPermission {
    ADMIN("ADMIN"),
    User("USER");

    private final String value;

    UserPermission(String value) {
        this.value = value;
    }
}
