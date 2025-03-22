package com.myproject.petcare.pet_diary.auth.controller;

import com.myproject.petcare.pet_diary.auth.dto.UserLoginReqDto;
import com.myproject.petcare.pet_diary.auth.dto.UserLoginResDto;
import com.myproject.petcare.pet_diary.auth.dto.UserSignupReqDto;
import com.myproject.petcare.pet_diary.auth.service.AuthService;
import com.myproject.petcare.pet_diary.common.dto.ResponseDto;
import com.myproject.petcare.pet_diary.jwt.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;

    //@Value("${spring.jwt.access-token-expiration-time}")
    //private Long accessTokenExpTime;
    //
    //@Value("${spring.jwt.refresh-token-expiration-time}")
    //private Long refreshTokenExpTime;

    @PostMapping("/auth/signup")
    public ResponseDto signup(@RequestBody @Validated UserSignupReqDto userSignupReqDto) {
        authService.signup(userSignupReqDto);
        return new ResponseDto<>(true, "회원 가입 성공", null);
    }

    @PostMapping("/auth/login")
    public ResponseDto<UserLoginResDto> login(@RequestBody @Validated UserLoginReqDto userLoginReqDto, HttpServletResponse response) {
        UserLoginResDto userLoginResDto = authService.login(userLoginReqDto);
        response.addCookie(createCookie("access", userLoginResDto.getAccessToken(), jwtUtil.getAccessTokenExpTime().intValue()/1000));
        response.addCookie(createCookie("refresh", userLoginResDto.getRefreshToken(), jwtUtil.getRefreshTokenExpTime().intValue()/1000));

        return new ResponseDto<>(true, "로그인 성공", null);
    }

    @PostMapping("/auth/refresh")
    public ResponseDto refresh(@CookieValue("refresh") String refreshToken, HttpServletResponse response){
        String accessToken = authService.refresh(refreshToken);
        response.addCookie(createCookie("access", accessToken, jwtUtil.getAccessTokenExpTime().intValue()/1000));

        return new ResponseDto<>(true, "access token 재생성 성공", null);
    }

    private Cookie createCookie(String key, String value, int maxAge){
        Cookie cookie = new Cookie(key, value);
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);
        cookie.setHttpOnly(true); // JS로 접근 불가, 탈취 위험 감소
        cookie.setSecure(true); // HTTPS에서만 전송 보장
        return cookie;
    }
}
