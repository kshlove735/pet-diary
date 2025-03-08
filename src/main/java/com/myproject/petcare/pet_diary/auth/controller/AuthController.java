package com.myproject.petcare.pet_diary.auth.controller;

import com.myproject.petcare.pet_diary.auth.dto.UserJoinDto;
import com.myproject.petcare.pet_diary.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/auth/signup")
    public ResponseEntity<String> signup(@RequestBody UserJoinDto userJoinDto){
        authService.signup(userJoinDto);
        return ResponseEntity.ok("Signup successful");
    }
}
