package com.myproject.petcare.pet_diary.user.controller;

import com.myproject.petcare.pet_diary.auth.dto.UserLoginReqDto;
import com.myproject.petcare.pet_diary.auth.dto.UserLoginResDto;
import com.myproject.petcare.pet_diary.auth.service.AuthService;
import com.myproject.petcare.pet_diary.jwt.CustomUserDetails;
import com.myproject.petcare.pet_diary.jwt.CustomUserDetailsService;
import com.myproject.petcare.pet_diary.user.entity.User;
import com.myproject.petcare.pet_diary.user.enums.Role;
import com.myproject.petcare.pet_diary.user.repository.UserRepository;
import com.myproject.petcare.pet_diary.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Slf4j
class UserControllerTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthService authService;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @BeforeEach
    void before() {
        for (int i = 1; i <= 10; i++) {
            User user = new User();
            user.setEmail("test" + i + "@gmail.com");
            user.setPassword(bCryptPasswordEncoder.encode("TestPassword" + i + "!!"));
            user.setName("테스트유저" + i);
            user.setPhone("010-1234-1234");
            user.setRole(Role.USER);

            userRepository.save(user);
        }
    }

    @Test
    @DisplayName("로그아웃 성공")
    void logoutSuccess() {
        // given
        // 로그인
        UserLoginReqDto userLoginReqDto = new UserLoginReqDto();
        userLoginReqDto.setEmail("test1@gmail.com");
        userLoginReqDto.setPassword("TestPassword1!!");
        UserLoginResDto userLoginResDto = authService.login(userLoginReqDto);

        // 로그아웃 데이터 준비
        User findUser = userRepository.findByEmail(userLoginReqDto.getEmail()).orElse(null);
        CustomUserDetails customUserDetails = customUserDetailsService.loadUserByUsername(findUser.getId().toString());

        // when
        userService.logout(customUserDetails);

        // then
        assertThat(findUser).isNotNull();
        assertThat(findUser.getRefreshToken()).isNull();
    }

}