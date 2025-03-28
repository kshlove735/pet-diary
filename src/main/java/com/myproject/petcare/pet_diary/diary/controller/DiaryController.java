package com.myproject.petcare.pet_diary.diary.controller;

import com.myproject.petcare.pet_diary.common.dto.ResponseDto;
import com.myproject.petcare.pet_diary.diary.service.DiaryService;
import com.myproject.petcare.pet_diary.diary.dto.HealthInfoResDto;
import com.myproject.petcare.pet_diary.diary.dto.PartialHealthReqDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class DiaryController {

    private final DiaryService diaryService;

    @PostMapping("/diary/{petId}/health")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto<HealthInfoResDto> createHealth(
            @PathVariable("petId") Long petId,
            @RequestBody @Validated PartialHealthReqDto partialHealthReqDto){

        HealthInfoResDto healthInfoResDto = diaryService.createHealth(petId, partialHealthReqDto);

        return new ResponseDto<>(true, "건강 기록 성공", healthInfoResDto);
    }
}
