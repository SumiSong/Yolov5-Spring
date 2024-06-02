package com.example.yolov5Project.security.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DTORegister {
    private Long userCode;
    private String userName;
    private String userId;
    private String userPass;
    private String userEmail;

    @Override
    public String toString() {
        return "DTORegister{" +
                "userCode=" + userCode +
                ", userName='" + userName + '\'' +
                ", userId='" + userId + '\'' +
                ", userPass='" + userPass + '\'' +
                ", userEmail='" + userEmail + '\'' +
                '}';
    }
}
