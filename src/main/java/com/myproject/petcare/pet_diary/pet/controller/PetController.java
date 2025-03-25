package com.myproject.petcare.pet_diary.pet.controller;

import com.myproject.petcare.pet_diary.common.dto.ResponseDto;
import com.myproject.petcare.pet_diary.jwt.CustomUserDetails;
import com.myproject.petcare.pet_diary.pet.dto.CreatePetReqDto;
import com.myproject.petcare.pet_diary.pet.dto.PetInfoResDto;
import com.myproject.petcare.pet_diary.pet.service.PetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class PetController {
    private final PetService petService;

    @PostMapping("/pet")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto<PetInfoResDto> createPet(
            @RequestBody @Validated CreatePetReqDto createPetReqDto,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        PetInfoResDto petInfoResDto = petService.createPet(createPetReqDto, customUserDetails);

        return new ResponseDto<>(true, "반려견 등록 성공", petInfoResDto);
    }

    @GetMapping("/pet/{petId}")
    public ResponseDto getPet(@PathVariable("petId") Long petId) {

        PetInfoResDto petInfoResDto = petService.getPet(petId);

        return new ResponseDto<>(true, "반려견 단일 조회", petInfoResDto);
    }
}
