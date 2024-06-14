package com.example.yolov5Project.security.controller;

import com.example.yolov5Project.security.DTO.DTORegister;
import com.example.yolov5Project.security.service.JoinService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
        return "joinForm";
    }

    @PostMapping("/create")
    public String create(@Validated @ModelAttribute DTORegister dtoRegister){
        joinService.join(dtoRegister);
        return "loginForm";
    }

    //로그인
    @GetMapping("/login")
    public String login(){
        return "loginForm";
    }


    //로그아웃
    @GetMapping("/logout")
    public String logout(){
        return "index";
    }

    //홈
    @GetMapping("/index")
    public String home(){
        return "index";
    }


}
