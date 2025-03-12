package com.myproject.petcare.pet_diary.auth.controller;

import com.myproject.petcare.pet_diary.auth.dto.UserSignupDto;
import com.myproject.petcare.pet_diary.auth.service.AuthService;
import com.myproject.petcare.pet_diary.common.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
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
    public ResponseDto signup(@RequestBody @Validated UserSignupDto userSignupDto){
        authService.signup(userSignupDto);

        return new ResponseDto(true, "Signup successful", null);
    }
}
