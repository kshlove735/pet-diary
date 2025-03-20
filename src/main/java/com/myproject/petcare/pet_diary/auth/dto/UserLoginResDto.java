package com.myproject.petcare.pet_diary.auth.dto;

import lombok.Getter;

@Getter
public class UserLoginResDto {
    private String accessToken;
    private String refreshToken;

    public UserLoginResDto(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
