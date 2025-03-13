package com.myproject.petcare.pet_diary.auth.service;

import com.myproject.petcare.pet_diary.auth.dto.UserSignupDto;
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

    @Transactional
    public void signup(UserSignupDto userSignupDto) {

        // 중복 회원 가입 검증
        if(userRepository.findByEmail(userSignupDto.getEmail()).isPresent()){
            throw new RuntimeException("Email already exist");
        }

        User user = new User();
        user.setEmail(userSignupDto.getEmail());
        user.setPassword(bCryptPasswordEncoder.encode(userSignupDto.getPassword()));
        user.setName(userSignupDto.getName());
        user.setPhone(userSignupDto.getPhone());
        user.setRole(Role.USER); // TODO : 유저, 관리자 권한에 따른 동적 회원 가입

        // 회원 정보 DB 저장
        userRepository.save(user);
    }


}
