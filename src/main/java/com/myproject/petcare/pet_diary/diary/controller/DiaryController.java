package com.myproject.petcare.pet_diary.diary.controller;

import com.myproject.petcare.pet_diary.common.dto.ResponseDto;
import com.myproject.petcare.pet_diary.diary.dto.*;
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

    @PostMapping("/diary/{petId}/meal")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto<MealInfoResDto> createMeal(
            @PathVariable("petId") Long petId,
            @RequestBody @Validated PartialMealReqDto partialMealReqDto
            ){
        MealInfoResDto mealInfoResDto = diaryService.createMeal(petId, partialMealReqDto);
        return new ResponseDto<>(true, "식사 기록 등록 성공", mealInfoResDto);
    }

    @PostMapping("/diary/{petId}/activity")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto<ActivityInfoResDto> createActivity(
            @PathVariable("petId") Long petId,
            @RequestBody @Validated PartialActivityReqDto partialActivityReqDto
    ){
        ActivityInfoResDto activityInfoResDto = diaryService.createActivity(petId, partialActivityReqDto);
        return new ResponseDto<>(true, "운동 기록 등록 성공", activityInfoResDto);
    }

    @PostMapping("/diary/{petId}/behavior")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto<BehaviorInfoResDto> createBehavior(
            @PathVariable("petId") Long petId,
            @RequestBody @Validated PartialBehaviorReqDto partialMealReqDto
    ){
        BehaviorInfoResDto behaviorInfoResDto = diaryService.createBehavior(petId, partialMealReqDto);
        return new ResponseDto<>(true, "행동 기록 등록 성공", behaviorInfoResDto);
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

    @PutMapping("/diary/{diaryId}/meal")
    public ResponseDto<MealInfoResDto> updateMeal(
            @PathVariable("diaryId") Long diaryId,
            @RequestBody @Validated PartialMealReqDto partialMealReqDto
    ){
        MealInfoResDto mealInfoResDto = diaryService.updateMeal(diaryId, partialMealReqDto);
        return new ResponseDto<>(true, "식사 기록 수정 성공", mealInfoResDto);
    }

    @PutMapping("/diary/{diaryId}/activity")
    public ResponseDto<ActivityInfoResDto> updateActivity(
            @PathVariable("diaryId") Long diaryId,
            @RequestBody @Validated PartialActivityReqDto partialActivityReqDto
    ){
        ActivityInfoResDto activityInfoResDto = diaryService.updateActivity(diaryId, partialActivityReqDto);
        return new ResponseDto<>(true, "운동 기록 수정 성공", activityInfoResDto);
    }

    @PutMapping("/diary/{diaryId}/behavior")
    public ResponseDto<BehaviorInfoResDto> updateBehavior(
            @PathVariable("diaryId") Long diaryId,
            @RequestBody @Validated PartialBehaviorReqDto partialBehaviorReqDto
    ){
        BehaviorInfoResDto behaviorInfoResDto = diaryService.updateBehavior(diaryId, partialBehaviorReqDto);
        return new ResponseDto<>(true, "행동 기록 수정 성공", behaviorInfoResDto);
    }
}
