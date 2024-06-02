package com.example.yolov5Project.security.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userCode;
    @Column(unique = true) //유일한 값을 저장하기 위함
    private String userId;
    @Column(name = "pass")
    private String userPass;
    @Column(name = "name")
    private String userName;
    @Column(unique = true) //유일한 값을 저장하기 위함
    private String userEmail;

    @Override
    public String toString() {
        return "UserEntity{" +
                "userCode=" + userCode +
                ", userId='" + userId + '\'' +
                ", userPass='" + userPass + '\'' +
                ", userName='" + userName + '\'' +
                ", userEmail='" + userEmail + '\'' +
                '}';
    }
}
