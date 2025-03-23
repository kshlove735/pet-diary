package com.myproject.petcare.pet_diary.user.service;

import com.myproject.petcare.pet_diary.common.exception.custom_exception.InvalidPasswordException;
import com.myproject.petcare.pet_diary.jwt.CustomUserDetails;
import com.myproject.petcare.pet_diary.user.dto.CheckPasswordReqDto;
import com.myproject.petcare.pet_diary.user.dto.UpdatePasswordReqDto;
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

    public UserInfoResDto getUser(CustomUserDetails userDetails) {
        User user = getUserFromUserDetails(userDetails);
        return new UserInfoResDto(user.getEmail(), user.getName(), user.getPhone());
    }

    @Transactional
    public void logout(CustomUserDetails userDetails) {
        User user = getUserFromUserDetails(userDetails);
        user.setRefreshToken(null);
    }

    @Transactional
    public UserInfoResDto updateUser(UpdateUserReqDto updateUserReqDto, CustomUserDetails userDetails) {
        User user = getUserFromUserDetails(userDetails);

        if (StringUtils.hasText(updateUserReqDto.getName())) {
            user.setName(updateUserReqDto.getName());
        }

        if (StringUtils.hasText(updateUserReqDto.getPhone())) {
            user.setPhone(updateUserReqDto.getPhone());
        }

        return new UserInfoResDto(user.getEmail(), user.getName(), user.getPhone());
    }

    public boolean checkPassword(CheckPasswordReqDto checkPasswordReqDto, CustomUserDetails userDetails) {
        User user = getUserFromUserDetails(userDetails);

        // 현재 비밀번호가 DB에 저장된 비밀번호와 같은지 확인
        if (!bCryptPasswordEncoder.matches(checkPasswordReqDto.getCurrentPassword(), user.getPassword())) {
            throw new InvalidPasswordException("비밀번호를 다시 확인해 주세요.");
        }

        return true;
    }

    @Transactional
    public void updatePassword(UpdatePasswordReqDto updatePasswordReqDto, CustomUserDetails userDetails) {
        User user = getUserFromUserDetails(userDetails);

        // 현재 비밀번호가 DB에 저장된 비밀번호와 같은지 확인
        if (!bCryptPasswordEncoder.matches(updatePasswordReqDto.getCurrentPassword(), user.getPassword())) {
            throw new InvalidPasswordException("비밀번호를 다시 확인해 주세요.");
        }

        // 바뀔 비밀번호의 동일 입력 여부 확인
        if (!updatePasswordReqDto.getChangedPassword().equals(updatePasswordReqDto.getChangedPasswordCheck())) {
            throw new InvalidPasswordException("변경될 비밀번호가 동일하지 않습니다.");
        }

        user.setPassword(bCryptPasswordEncoder.encode(updatePasswordReqDto.getChangedPassword()));
    }

    @Transactional
    public void deleteUser(CustomUserDetails userDetails) {
        User user = getUserFromUserDetails(userDetails);
        userRepository.delete(user);
    }

    private User getUserFromUserDetails(CustomUserDetails userDetails) {
        Long id = Long.valueOf(userDetails.getUsername());
        User user = userRepository.findById(id).orElse(null);
        return user;
    }
}
