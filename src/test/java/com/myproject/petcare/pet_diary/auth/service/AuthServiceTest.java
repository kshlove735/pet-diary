package com.myproject.petcare.pet_diary.auth.service;

import com.myproject.petcare.pet_diary.auth.dto.UserLoginReqDto;
import com.myproject.petcare.pet_diary.auth.dto.UserLoginResDto;
import com.myproject.petcare.pet_diary.auth.dto.UserSignupReqDto;
import com.myproject.petcare.pet_diary.jwt.JwtUtil;
import com.myproject.petcare.pet_diary.user.entity.User;
import com.myproject.petcare.pet_diary.user.enums.Role;
import com.myproject.petcare.pet_diary.user.repository.UserRepository;
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
        RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> authService.signup(duplicateEmailUser));
        assertThat(runtimeException.getMessage()).isEqualTo("Email already exist");
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
        RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> authService.login(userLoginReqDto));
        assertThat(runtimeException.getMessage()).isEqualTo("이메일이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("로그인 실패 - 잘못된 비밀번호로로 예외 발생")
    void loginFailDueToInvalidPassword() {
        // Given : 유효한 로그인 데이터 준비
        UserLoginReqDto userLoginReqDto = new UserLoginReqDto();

        userLoginReqDto.setEmail("test1@gmail.com");
        userLoginReqDto.setPassword("wrongPassword1!!");

        // When & Then: 로그인 실패 -  존재하지 않는 이메일로 예외 발생
        RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> authService.login(userLoginReqDto));
        assertThat(runtimeException.getMessage()).isEqualTo("비밀번호가 일치하지 않습니다.");
    }

}