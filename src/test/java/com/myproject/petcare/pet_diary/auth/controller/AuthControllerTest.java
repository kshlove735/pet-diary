package com.myproject.petcare.pet_diary.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myproject.petcare.pet_diary.auth.dto.UserSignupDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("정상 회원 가입 요청")
    void signupSuccess() throws Exception {
        // Given : 유효한 회원 가입 데이터 준비
        UserSignupDto userSignupDto = new UserSignupDto();
        userSignupDto.setEmail("test@gmail.com");
        userSignupDto.setPassword("TestPassword1!!");
        userSignupDto.setName("테스트유저");
        userSignupDto.setPhone("010-1234-1234");

        // When & Then : POST 요청 후 성공 응답 확인
        mockMvc.perform(post("/v1/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userSignupDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Signup successful"));
    }

    @Test
    @DisplayName("유효하지 않은 이메일")
    void signupFailDueToInvalidEmail() throws Exception {
        // Given : 유효한 회원 가입 데이터 준비
        UserSignupDto userSignupDto = new UserSignupDto();
        userSignupDto.setEmail("test_gmail.com");
        userSignupDto.setPassword("TestPassword1!!");
        userSignupDto.setName("테스트유저");
        userSignupDto.setPhone("010-1234-1234");

        // When & Then : POST 요청 후 성공 응답 확인
        mockMvc.perform(post("/v1/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userSignupDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Validation Error"));
    }

    @Test
    @DisplayName("비밀번호 패턴 불일치")
    void signupFailDueToInvalidPassword() throws Exception {
        // Given : 유효한 회원 가입 데이터 준비
        UserSignupDto userSignupDto = new UserSignupDto();
        userSignupDto.setEmail("test_gmail.com");
        userSignupDto.setPassword("Test!");
        userSignupDto.setName("테스트유저");
        userSignupDto.setPhone("010-1234-1234");

        // When & Then : POST 요청 후 성공 응답 확인
        mockMvc.perform(post("/v1/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userSignupDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Validation Error"));
    }
}