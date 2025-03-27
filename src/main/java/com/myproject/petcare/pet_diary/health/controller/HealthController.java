package com.myproject.petcare.pet_diary.health.controller;

import com.myproject.petcare.pet_diary.common.dto.ResponseDto;
import com.myproject.petcare.pet_diary.health.service.HealthService;
import com.myproject.petcare.pet_diary.health.dto.HealthInfoResDto;
import com.myproject.petcare.pet_diary.health.dto.PartialHealthReqDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class HealthController {
    private final HealthService healthService;

    @PostMapping("/health/{petId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto<HealthInfoResDto> createHealth(
            @PathVariable("petId") Long petId,
            @RequestBody @Validated PartialHealthReqDto partialHealthReqDto) {

        HealthInfoResDto healthInfoResDto = healthService.createHealth(petId, partialHealthReqDto);

        return new ResponseDto<>(true, "건강 기록 성공", healthInfoResDto);
    }
}
