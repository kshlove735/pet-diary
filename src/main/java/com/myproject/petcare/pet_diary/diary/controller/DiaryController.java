package com.myproject.petcare.pet_diary.diary.controller;

import com.myproject.petcare.pet_diary.common.dto.ResponseDto;
import com.myproject.petcare.pet_diary.diary.dto.GroomingInfoResDto;
import com.myproject.petcare.pet_diary.diary.dto.HealthInfoResDto;
import com.myproject.petcare.pet_diary.diary.dto.PartialGroomingReqDto;
import com.myproject.petcare.pet_diary.diary.dto.PartialHealthReqDto;
import com.myproject.petcare.pet_diary.diary.service.DiaryService;
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
            @RequestBody @Validated PartialHealthReqDto partialHealthReqDto
    ){
        HealthInfoResDto healthInfoResDto = diaryService.createHealth(petId, partialHealthReqDto);
        return new ResponseDto<>(true, "건강 기록 등록 성공", healthInfoResDto);
    }

    @PostMapping("/diary/{petId}/grooming")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto<GroomingInfoResDto> createGrooming(
            @PathVariable("petId") Long petId,
            @RequestBody @Validated PartialGroomingReqDto partialHealthReqDto
            ){
        GroomingInfoResDto groomingInfoResDto = diaryService.createGrooming(petId, partialHealthReqDto);
        return new ResponseDto<>(true, "미용 기록 등록 성공", groomingInfoResDto);
    }

    @PutMapping("/diary/{diaryId}/health")
    public ResponseDto<HealthInfoResDto> updateHealth(
            @PathVariable("diaryId") Long diaryId,
            @RequestBody @Validated PartialHealthReqDto partialHealthReqDto
    ){
        HealthInfoResDto healthInfoResDto = diaryService.updateHealth(diaryId, partialHealthReqDto);
        return new ResponseDto<>(true, "건강 기록 수정 성공", healthInfoResDto);
    }

    @PutMapping("/diary/{diaryId}/grooming")
    public ResponseDto<GroomingInfoResDto> updateGrooming(
            @PathVariable("diaryId") Long diaryId,
            @RequestBody @Validated PartialGroomingReqDto partialHealthReqDto
    ){
        GroomingInfoResDto groomingInfoResDto = diaryService.updateGrooming(diaryId, partialHealthReqDto);
        return new ResponseDto<>(true, "미용 기록 수정 성공", groomingInfoResDto);
    }
}
