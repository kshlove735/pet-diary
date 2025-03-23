package com.myproject.petcare.pet_diary.pet.controller;

import com.myproject.petcare.pet_diary.common.dto.ResponseDto;
import com.myproject.petcare.pet_diary.jwt.CustomUserDetails;
import com.myproject.petcare.pet_diary.pet.dto.CreatePetReqDto;
import com.myproject.petcare.pet_diary.pet.service.PetService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class PetController {
    private PetService petService;

    @Transactional
    @PostMapping("/pet")
    public ResponseDto createPet(
            CreatePetReqDto createPetReqDto,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        petService.createPet(createPetReqDto, customUserDetails);

        return new ResponseDto<>(true, "회원 정보 조회 성공", null);
    }
}
