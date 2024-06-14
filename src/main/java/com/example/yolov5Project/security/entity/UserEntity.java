package com.example.yolov5Project.security.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Table(name="userInfo")
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userCode;

    @Column(unique = true, name = "id")
    private String userId;
    @Column(name = "pass")
    private String userPass;
    @Column(name = "name")
    private String userName;
    @Column(unique = true, name = "email")
    private String userEmail;

}
