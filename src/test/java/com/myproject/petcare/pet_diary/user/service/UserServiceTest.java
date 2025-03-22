package com.myproject.petcare.pet_diary.user.service;

import com.myproject.petcare.pet_diary.auth.dto.LoginReqDto;
import com.myproject.petcare.pet_diary.auth.dto.LoginResDto;
import com.myproject.petcare.pet_diary.auth.service.AuthService;
import com.myproject.petcare.pet_diary.jwt.CustomUserDetails;
import com.myproject.petcare.pet_diary.jwt.CustomUserDetailsService;
import com.myproject.petcare.pet_diary.user.dto.UpdateUserReqDto;
import com.myproject.petcare.pet_diary.user.dto.UserInfoResDto;
import com.myproject.petcare.pet_diary.user.entity.User;
import com.myproject.petcare.pet_diary.user.enums.Role;
import com.myproject.petcare.pet_diary.user.repository.UserRepository;
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
class UserServiceTest {

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
        LoginReqDto loginReqDto = new LoginReqDto();
        loginReqDto.setEmail("test1@gmail.com");
        loginReqDto.setPassword("TestPassword1!!");
        LoginResDto loginResDto = authService.login(loginReqDto);

        // 로그아웃 데이터 준비
        User findUser = userRepository.findByEmail(loginReqDto.getEmail()).orElse(null);
        CustomUserDetails customUserDetails = customUserDetailsService.loadUserByUsername(findUser.getId().toString());

        // when
        userService.logout(customUserDetails);

        // then
        assertThat(findUser).isNotNull();
        assertThat(findUser.getRefreshToken()).isNull();
    }

    @Test
    @DisplayName("회원 정보 수정 성공")
    void updateUserSuccess() {
        // given
        // 로그인
        LoginReqDto loginReqDto = new LoginReqDto();
        loginReqDto.setEmail("test1@gmail.com");
        loginReqDto.setPassword("TestPassword1!!");
        LoginResDto loginResDto = authService.login(loginReqDto);

        // 회원 정보 데이터 준비
        User findUser = userRepository.findByEmail(loginReqDto.getEmail()).orElse(null);
        CustomUserDetails customUserDetails = customUserDetailsService.loadUserByUsername(findUser.getId().toString());

        // 회원 정보 수정 데이터 준비
        UpdateUserReqDto updateUserReqDto = new UpdateUserReqDto();
        updateUserReqDto.setName("김승현");
        updateUserReqDto.setPhone("010-3304-2341");

        // when
        UserInfoResDto userInfoResDto = userService.updateUser(updateUserReqDto, customUserDetails);

        // then
        assertThat(userInfoResDto.getName()).isEqualTo(updateUserReqDto.getName());
        assertThat(userInfoResDto.getPhone()).isEqualTo(updateUserReqDto.getPhone());

        assertThat(userInfoResDto.getEmail()).isEqualTo(findUser.getEmail());
    }
}