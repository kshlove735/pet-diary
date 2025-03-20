package com.myproject.petcare.pet_diary.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class UserController {

    @GetMapping("/user/test")
    public String test(){
        return "ok";
    }
}
