package com.myproject.petcare.pet_diary.user.service;

import com.myproject.petcare.pet_diary.jwt.CustomUserDetails;
import com.myproject.petcare.pet_diary.user.dto.UpdateUserReqDto;
import com.myproject.petcare.pet_diary.user.dto.UserInfoResDto;
import com.myproject.petcare.pet_diary.user.entity.User;
import com.myproject.petcare.pet_diary.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public void logout(CustomUserDetails userDetails) {
        Long id = Long.valueOf(userDetails.getUsername());
        User user = userRepository.findById(id).orElse(null);
        user.setRefreshToken(null);
    }

    @Transactional
    public UserInfoResDto updateUser(UpdateUserReqDto updateUserReqDto, CustomUserDetails userDetails) {
        Long id = Long.valueOf(userDetails.getUsername());
        User user = userRepository.findById(id).orElse(null);

        if (StringUtils.hasText(updateUserReqDto.getName())) {
            user.setName(updateUserReqDto.getName());
        }

        if (StringUtils.hasText(updateUserReqDto.getPhone())) {
            user.setPhone(updateUserReqDto.getPhone());
        }

        return new UserInfoResDto(user.getEmail(), user.getName(), user.getPhone());
    }
}
