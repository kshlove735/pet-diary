package com.myproject.petcare.pet_diary.auth.service;

import com.myproject.petcare.pet_diary.auth.dto.UserLoginReqDto;
import com.myproject.petcare.pet_diary.auth.dto.UserLoginResDto;
import com.myproject.petcare.pet_diary.auth.dto.UserSignupReqDto;
import com.myproject.petcare.pet_diary.common.exception.custom_exception.EmailDuplicationException;
import com.myproject.petcare.pet_diary.common.exception.custom_exception.EmailNotFoundException;
import com.myproject.petcare.pet_diary.common.exception.custom_exception.InvalidPasswordException;
import com.myproject.petcare.pet_diary.jwt.JwtUtil;
import com.myproject.petcare.pet_diary.user.entity.User;
import com.myproject.petcare.pet_diary.user.enums.Role;
import com.myproject.petcare.pet_diary.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional
    public void signup(UserSignupReqDto userSignupReqDto) {

        // 중복 회원 가입 검증
        if (userRepository.findByEmail(userSignupReqDto.getEmail()).isPresent()) {
            throw new EmailDuplicationException("Email already exist");
        }

        User user = new User();
        user.setEmail(userSignupReqDto.getEmail());
        user.setPassword(bCryptPasswordEncoder.encode(userSignupReqDto.getPassword()));
        user.setName(userSignupReqDto.getName());
        user.setPhone(userSignupReqDto.getPhone());
        user.setRole(Role.USER); // TODO : 유저, 관리자 권한에 따른 동적 회원 가입

        // 회원 정보 DB 저장
        userRepository.save(user);
    }


    @Transactional
    public UserLoginResDto login(UserLoginReqDto userLoginReqDto) {
        User findUser = userRepository.findByEmail(userLoginReqDto.getEmail()).orElse(null);

        // DB에 저장된 회원인지 여부 검증
        if (findUser == null) {
            throw new EmailNotFoundException("이메일이 존재하지 않습니다.");
        }

        // password 검증
        if (!bCryptPasswordEncoder.matches(userLoginReqDto.getPassword(), findUser.getPassword())) {
            throw new InvalidPasswordException("비밀번호가 일치하지 않습니다.");
        }

        // JWT 발급
        String accessToken = jwtUtil.createAccessToken(findUser.getId(), findUser.getRole().toString());
        String refreshToken = jwtUtil.createRefreshToken(findUser.getId());

        // refresh token DB에 저장
        findUser.setRefreshToken(refreshToken);

        // return JWT 토큰
        return new UserLoginResDto(accessToken, refreshToken);
    }
}
