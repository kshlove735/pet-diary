package com.myproject.petcare.pet_diary.health_record.controller;

import com.myproject.petcare.pet_diary.common.dto.ResponseDto;
import com.myproject.petcare.pet_diary.health_record.dto.HealthRecordInfoResDto;
import com.myproject.petcare.pet_diary.health_record.dto.PartialHealthRecordReqDto;
import com.myproject.petcare.pet_diary.health_record.service.HealthRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class HealthRecordController {
    private final HealthRecordService healthRecordService;

    @PostMapping("/health-record/{petId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto<HealthRecordInfoResDto> createHealthRecord(
            @PathVariable("petId") Long petId,
            @RequestBody @Validated PartialHealthRecordReqDto partialHealthRecordReqDto) {

        HealthRecordInfoResDto healthRecordInfoResDto = healthRecordService.createHealthRecord(petId, partialHealthRecordReqDto);

        return new ResponseDto<>(true, "건강 기록 성공", healthRecordInfoResDto);
    }
}
