package com.example.yolov5Project.security.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DTOLogin {
    private String userId;
    private String userPass;

    public DTOLogin(String userId, String userPass) {
        this.userId = userId;
        this.userPass = userPass;
    }

    @Override
    public String toString() {
        return "DTOLogin{" +
                "userId='" + userId + '\'' +
                ", userPass='" + userPass + '\'' +
                '}';
    }
}
