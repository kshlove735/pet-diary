package com.myproject.petcare.pet_diary.auth.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserJoinDto {
    private String email;
    private String password;
    private String name;
    private String phone;
}
