package com.myproject.petcare.pet_diary.user.service;

import com.myproject.petcare.pet_diary.jwt.CustomUserDetails;
import com.myproject.petcare.pet_diary.user.entity.User;
import com.myproject.petcare.pet_diary.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public void logout(CustomUserDetails userDetails) {
        Long id = Long.valueOf(userDetails.getUsername());
        User user = userRepository.findById(id).orElse(null);
        user.setRefreshToken(null);
    }
}
