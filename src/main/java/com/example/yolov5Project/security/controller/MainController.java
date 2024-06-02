package com.example.yolov5Project.security.controller;

import com.example.yolov5Project.security.DTO.DTORegister;
import com.example.yolov5Project.security.service.JoinService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MainController {
    private final JoinService joinService;

    public MainController(JoinService joinService) {
        this.joinService = joinService;
    }

    //회원가입
    @GetMapping("/join")
    public String join(){
        return "join";
    }

    @PostMapping("/create")
    public String create(DTORegister dtoRegister){
        joinService.join(dtoRegister);
        return "studyRoom";
    }

    //로그인
    @GetMapping("/login")
    public String login(){
        return "loginForm";
    }


}
