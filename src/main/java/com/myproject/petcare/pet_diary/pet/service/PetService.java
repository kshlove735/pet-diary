package com.myproject.petcare.pet_diary.pet.service;

import com.myproject.petcare.pet_diary.jwt.CustomUserDetails;
import com.myproject.petcare.pet_diary.pet.dto.CreatePetReqDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PetService {

    public void createPet(CreatePetReqDto createPetReqDto, CustomUserDetails customUserDetails) {

    }

}
