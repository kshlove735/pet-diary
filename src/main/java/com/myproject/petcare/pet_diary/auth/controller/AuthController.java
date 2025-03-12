package com.myproject.petcare.pet_diary.auth.controller;

import com.myproject.petcare.pet_diary.auth.dto.UserSignupDto;
import com.myproject.petcare.pet_diary.auth.service.AuthService;
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
    public Object signup(@RequestBody @Validated UserSignupDto userSignupDto){

        //if(bindingResult.hasErrors()){
        //    return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        //}

        authService.signup(userSignupDto);
        return "Signup successful";
    }
}
