package com.myproject.petcare.pet_diary.auth.service;

import com.myproject.petcare.pet_diary.auth.dto.UserSignupDto;
import com.myproject.petcare.pet_diary.user.entity.User;
import com.myproject.petcare.pet_diary.user.enums.Role;
import com.myproject.petcare.pet_diary.user.repository.UserRepository;
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

    @Test
    @DisplayName("회원 가입 성공")
    void signupSuccess() {
        // Given : 유효한 회원 가입 데이터 준비
        UserSignupDto userSignupDto = new UserSignupDto();
        userSignupDto.setEmail("test@gmail.com");
        userSignupDto.setPassword("TestPassword1!!");
        userSignupDto.setName("테스트유저");
        userSignupDto.setPhone("010-1234-1234");

        // When : 회원 가입 실행
        authService.signup(userSignupDto);

        // Then : 사용자가 DB에 저장된는지 확인
        User findUser = userRepository.findByEmail(userSignupDto.getEmail()).orElse(null);

        assertThat(findUser).isNotNull();
        assertThat(findUser.getEmail()).isEqualTo(userSignupDto.getEmail());
        assertThat(findUser.getName()).isEqualTo(userSignupDto.getName());
        assertThat(findUser.getPhone()).isEqualTo(userSignupDto.getPhone());
        assertThat(findUser.getRole()).isEqualTo(Role.USER);
        assertThat(findUser.getPassword()).isNotEqualTo(userSignupDto.getPassword());
        assertThat(bCryptPasswordEncoder.matches(userSignupDto.getPassword(), findUser.getPassword())).isTrue();
    }

    @Test
    @DisplayName("중복 이메일로 회원 가입 시도 - 예외 발생 확인")
    void signupFailDueToDuplicateEmail() {
        // Given : 동일한 이메일로 회원 가입
        UserSignupDto existingUser = new UserSignupDto();
        existingUser.setEmail("test@gmail.com");
        existingUser.setPassword("TestPassword1!");
        existingUser.setName("테스트유저1");
        existingUser.setPhone("010-1234-1234");

        authService.signup(existingUser);

        UserSignupDto duplicateEmailUser = new UserSignupDto();
        duplicateEmailUser.setEmail("test@gmail.com");
        duplicateEmailUser.setPassword("TestPassword1!!");
        duplicateEmailUser.setName("테스트유저2");
        duplicateEmailUser.setPhone("010-1234-5678");

        // When : 중복 이메일 회원 가입 실행
        // Then : 예외 발생 확인
        RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> authService.signup(duplicateEmailUser));
        assertThat(runtimeException.getMessage()).isEqualTo("Email already exist");
    }

}