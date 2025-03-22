package com.myproject.petcare.pet_diary.user.controller;

import com.myproject.petcare.pet_diary.common.dto.ResponseDto;
import com.myproject.petcare.pet_diary.jwt.CustomUserDetails;
import com.myproject.petcare.pet_diary.user.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/user/test")
    public String test() {
        return "ok";
    }


    @PatchMapping("/user/logout")
    public ResponseDto logout(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            HttpServletResponse response) {

        userService.logout(userDetails);

        Cookie deleteAccessCookie = deleteCookie("access");
        Cookie deleteRefreshCookie = deleteCookie("refresh");
        response.addCookie(deleteAccessCookie);
        response.addCookie(deleteRefreshCookie);
        return new ResponseDto<>(true, "로그아웃 성공", null);
    }

    private Cookie deleteCookie(String key) {
        Cookie cookie = new Cookie(key, null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        return cookie;
    }
}
