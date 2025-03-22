package com.myproject.petcare.pet_diary.auth.service;

import com.myproject.petcare.pet_diary.auth.dto.UserLoginReqDto;
import com.myproject.petcare.pet_diary.auth.dto.UserLoginResDto;
import com.myproject.petcare.pet_diary.auth.dto.UserSignupReqDto;
import com.myproject.petcare.pet_diary.common.exception.custom_exception.*;
import com.myproject.petcare.pet_diary.jwt.JwtUtil;
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
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@Slf4j
class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

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
    @DisplayName("회원 가입 성공")
    void signupSuccess() {
        // Given : 유효한 회원 가입 데이터 준비
        UserSignupReqDto userSignupReqDto = new UserSignupReqDto();
        userSignupReqDto.setEmail("test@gmail.com");
        userSignupReqDto.setPassword("TestPassword1!!");
        userSignupReqDto.setName("테스트유저");
        userSignupReqDto.setPhone("010-1234-1234");

        // When : 회원 가입 실행
        authService.signup(userSignupReqDto);

        // Then : 사용자가 DB에 저장된는지 확인
        User findUser = userRepository.findByEmail(userSignupReqDto.getEmail()).orElse(null);

        assertThat(findUser).isNotNull();
        assertThat(findUser.getEmail()).isEqualTo(userSignupReqDto.getEmail());
        assertThat(findUser.getName()).isEqualTo(userSignupReqDto.getName());
        assertThat(findUser.getPhone()).isEqualTo(userSignupReqDto.getPhone());
        assertThat(findUser.getRole()).isEqualTo(Role.USER);
        assertThat(findUser.getPassword()).isNotEqualTo(userSignupReqDto.getPassword());
        assertThat(bCryptPasswordEncoder.matches(userSignupReqDto.getPassword(), findUser.getPassword())).isTrue();
    }

    @Test
    @DisplayName("회원 가입 실패 - 중복 이메일로 회원 가입 시도")
    void signupFailDueToDuplicateEmail() {
        // Given : 동일한 이메일로 회원 가입
        UserSignupReqDto existingUser = new UserSignupReqDto();
        existingUser.setEmail("test@gmail.com");
        existingUser.setPassword("TestPassword1!");
        existingUser.setName("테스트유저1");
        existingUser.setPhone("010-1234-1234");

        authService.signup(existingUser);

        UserSignupReqDto duplicateEmailUser = new UserSignupReqDto();
        duplicateEmailUser.setEmail("test@gmail.com");
        duplicateEmailUser.setPassword("TestPassword1!!");
        duplicateEmailUser.setName("테스트유저2");
        duplicateEmailUser.setPhone("010-1234-5678");

        // When : 중복 이메일 회원 가입 실행
        // Then : 예외 발생 확인
        EmailDuplicationException emailDuplicationException = assertThrows(EmailDuplicationException.class, () -> authService.signup(duplicateEmailUser));
        assertThat(emailDuplicationException.getMessage()).isEqualTo("이미 등록된 유저입니다.");
    }

    @Test
    @DisplayName("로그인 성공")
    void loginSuccess() {
        // Given : 유효한 로그인 데이터 준비
        UserLoginReqDto userLoginReqDto = new UserLoginReqDto();

        userLoginReqDto.setEmail("test1@gmail.com");
        userLoginReqDto.setPassword("TestPassword1!!");

        // When : 로그인 실행
        UserLoginResDto userLoginResDto = authService.login(userLoginReqDto);

        // Then : 사용자가 DB에 저장된는지 확인
        assertThat(jwtUtil.isExpired(userLoginResDto.getAccessToken())).isFalse();
        assertThat(jwtUtil.isExpired(userLoginResDto.getRefreshToken())).isFalse();

        User findUser = userRepository.findByEmail(userLoginReqDto.getEmail()).orElse(null);
        assertThat(jwtUtil.getId(userLoginResDto.getAccessToken())).isEqualTo(findUser.getId());
        assertThat(Role.valueOf(jwtUtil.getRole(userLoginResDto.getAccessToken()))).isEqualTo(findUser.getRole());
        assertThat(findUser.getRefreshToken()).isNotNull();
    }

    @Test
    @DisplayName("로그인 실패 - 존재하지 않는 이메일로 예외 발생")
    void loginFailDueToInvalidEmail() {
        // Given : 유효한 로그인 데이터 준비
        UserLoginReqDto userLoginReqDto = new UserLoginReqDto();

        userLoginReqDto.setEmail("nonexitent@gmail.com");
        userLoginReqDto.setPassword("TestPassword1!!");

        // When & Then: 로그인 실패 -  존재하지 않는 이메일로 예외 발생
        EmailNotFoundException emailNotFoundException = assertThrows(EmailNotFoundException.class, () -> authService.login(userLoginReqDto));
        assertThat(emailNotFoundException.getMessage()).isEqualTo("이메일이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("로그인 실패 - 잘못된 비밀번호로로 예외 발생")
    void loginFailDueToInvalidPassword() {
        // Given : 유효한 로그인 데이터 준비
        UserLoginReqDto userLoginReqDto = new UserLoginReqDto();

        userLoginReqDto.setEmail("test1@gmail.com");
        userLoginReqDto.setPassword("wrongPassword1!!");

        // When & Then: 로그인 실패 -  존재하지 않는 이메일로 예외 발생
        InvalidPasswordException invalidPasswordException = assertThrows(InvalidPasswordException.class, () -> authService.login(userLoginReqDto));
        assertThat(invalidPasswordException.getMessage()).isEqualTo("비밀번호가 일치하지 않습니다.");
    }

    @Test
    @DisplayName("토근 유효성 검사")
    void validToken() {

        // 오류
        String expiredToken = "eyJhbGciOiJIUzI1NiJ9.eyJpZCI6MSwicm9sZSI6IlVTRVIiLCJpYXQiOjE3NDI0MzI3NTcsImV4cCI6MTc0MjQzNDU1N30.ErXJo0HzaSWnV-tNMxPx0f2ys2QEJy4bfu2l6UARqW4";
        assertThrows(ExpiredTokenException.class, () -> jwtUtil.isExpired(expiredToken));

        String wrongToken = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJPVXFpTklBSnZCdXVLRnVicFJJbUN0TjFROXZwQkxTYTNxczhvMWpTelNNIn0.eyJleHAiOjE3NTAyNDIyMTAsImlhdCI6MTc0MjQ2NjIxMCwianRpIjoiMTA2Y2UzZmQtOTVjZS00YzQ0LWJhNDktYjU2ZDYzZTJhNmNhIiwiaXNzIjoiaHR0cHM6Ly93d3cuZmFybWluc2YuY29tL2F1dGgvcmVhbG1zL2Zhcm1pbiIsImF1ZCI6ImFjY291bnQiLCJzdWIiOiI3ZTZiNzJkYy0yMTcyLTQ5NzctOTY3ZC1lYTMyMGQ4NTk5MWYiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJmYXJtaW4iLCJzaWQiOiJiZDQ1MzE1Yy03MDNmLTQ1MmEtODJjZS1lMDdjNWQ4OTA1NmYiLCJhY3IiOiIxIiwiYWxsb3dlZC1vcmlnaW5zIjpbImh0dHA6Ly8xMTUuMjEuNzIuMjQ4OjE2NjAwLyoiLCJodHRwczovL2lmYWN0b3J5ZmFybS5mYXJtaW5zZi5jb20vKiIsIioiLCJodHRwczovL3d3dy5mYXJtaW5zZi5jb20vYXV0aC8qIiwiLyoiLCJodHRwczovL3d3dy5mYXJtaW5zZi5jb20vKiJdLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsiUk9MRV9GQVJNRVIiLCJvZmZsaW5lX2FjY2VzcyIsIlJPTEVfQURNSU4iLCJ1bWFfYXV0aG9yaXphdGlvbiIsImRlZmF1bHQtcm9sZXMtZmFybWluIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJvcGVuaWQgZW1haWwgcHJvZmlsZSIsImVtYWlsX3ZlcmlmaWVkIjpmYWxzZSwibmFtZSI6IuyEnOyasOyXoOyXkOyKpCDshJzsmrDsl6Dsl5DsiqQiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJzdWh3b29tczEiLCJnaXZlbl9uYW1lIjoi7ISc7Jqw7Jeg7JeQ7IqkIiwiZmFtaWx5X25hbWUiOiLshJzsmrDsl6Dsl5DsiqQifQ.Ikf2XpIhFRcj4fQWhsj49dCtQPJnCMSoak-ImsP1MroQCGNo4nbtD0AK_mZWeIFoVyH1pjfzZuJFiM2f1cxfWPrHctXBI-tajyA8TNfzmEyfQf9bckq5lcpYEp6VtU9oqtOMORCQ2jfwJ8MhX5ahhTNEtV59zxyrx-_2OY9qwG7pGKWXUisSOhDe3SVQlglQaB06N-OMq0ntfapgZHftzu9ZMTEEIGBihCZm-ZkBjuRZugRF503JoDpGHc9XMZu_YQt0MhfxCikDDeZE0-XWTLyYQXVOdFJ9EY4TWhXINTPIFbrBe7xpxePTQlp5kOTCAF1PLs9B28y7yV6MUcq-9g";
        assertThrows(InvalidTokenException.class, () -> jwtUtil.isExpired(wrongToken));

        //String wrongSecret = "eyJhbGciOiJIUzI1NiJ9.eyJpZCI6MSwicm9sZSI6IlVTRVIiLCJpYXQiOjE3NDI0MzI3NTcsImV4cCI6MTc0MjQzNDU1N30.aoiSzFs_q3vngP5BnWiL3Tk10o51-MUTpnjztKnArK0";
        //assertThrows(SignatureException.class, () -> jwtUtil.isExpired(wrongSecret));
        //
        //assertThrows(JwtException.class, () -> jwtUtil.isExpired(wrongSecret));

        // 정상
        String accessToken = jwtUtil.createAccessToken(1L, "USER");
        String refreshToken = jwtUtil.createRefreshToken(1L);

        assertThat(jwtUtil.isExpired(accessToken)).isFalse();
        assertThat(jwtUtil.isExpired(refreshToken)).isFalse();
    }
}