package com.myproject.petcare.pet_diary.auth.controller;

import com.myproject.petcare.pet_diary.auth.dto.UserLoginReqDto;
import com.myproject.petcare.pet_diary.auth.dto.UserLoginResDto;
import com.myproject.petcare.pet_diary.auth.dto.UserSignupReqDto;
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
    public ResponseDto signup(@RequestBody @Validated UserSignupReqDto userSignupReqDto) {
        authService.signup(userSignupReqDto);
        return new ResponseDto<>(true, "회원 가입 성공", null);
    }

    @PostMapping("/auth/login")
    public ResponseDto<UserLoginResDto> login(@RequestBody @Validated UserLoginReqDto userLoginReqDto) {
        UserLoginResDto userLoginResDto = authService.login(userLoginReqDto);
        return new ResponseDto<>(true, "로그인 성공", userLoginResDto);
    }
}
