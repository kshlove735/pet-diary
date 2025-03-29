package com.myproject.petcare.pet_diary.diary.service;

import com.myproject.petcare.pet_diary.common.exception.custom_exception.NotFoundException;
import com.myproject.petcare.pet_diary.diary.dto.*;
import com.myproject.petcare.pet_diary.diary.entity.Grooming;
import com.myproject.petcare.pet_diary.diary.entity.Health;
import com.myproject.petcare.pet_diary.diary.entity.Meal;
import com.myproject.petcare.pet_diary.diary.repository.DiaryRepository;
import com.myproject.petcare.pet_diary.pet.entity.Pet;
import com.myproject.petcare.pet_diary.pet.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DiaryService {

    private final PetRepository petRepository;
    private final DiaryRepository diaryRepository;


    @Transactional
    public HealthInfoResDto createHealth(Long petId, PartialHealthReqDto partialHealthReqDto) {
        Pet pet = petRepository.findById(petId).orElseThrow(() -> new NotFoundException("해당하는 반려견이 없습니다."));

        Health health = new Health(
                pet, partialHealthReqDto.getHealthType(), partialHealthReqDto.getDescription(),
                partialHealthReqDto.getDate(), partialHealthReqDto.getNextDueDate(),
                partialHealthReqDto.getClinic()
        );

        diaryRepository.save(health);

        HealthInfoResDto healthInfoResDto = getHealthInfoResDto(health);
        return healthInfoResDto;
    }

    @Transactional
    public GroomingInfoResDto createGrooming(Long petId, PartialGroomingReqDto partialHealthReqDto) {
        Pet pet = petRepository.findById(petId).orElseThrow(() -> new NotFoundException("해당하는 반려견이 없습니다."));

        Grooming grooming = new Grooming(
                pet, partialHealthReqDto.getDate(),
                partialHealthReqDto.getDescription(),
                partialHealthReqDto.getGroomingType()
        );

        diaryRepository.save(grooming);

        GroomingInfoResDto groomingInfoResDto = getGroomingInfoResDto(grooming);
        return groomingInfoResDto;
    }

    @Transactional
    public MealInfoResDto createMeal(Long petId, PartialMealReqDto partialMealReqDto) {
        Pet pet = petRepository.findById(petId).orElseThrow(() -> new NotFoundException("해당하는 반려견이 없습니다."));

        Meal meal = new Meal(
                pet, partialMealReqDto.getDate(),
                partialMealReqDto.getDescription(), partialMealReqDto.getMealType(),
                partialMealReqDto.getFoodBrand(), partialMealReqDto.getFoodAmount()
        );

        diaryRepository.save(meal);

        MealInfoResDto mealInfoResDto = getMealInfoResDto(meal);
        return mealInfoResDto;
    }

    @Transactional
    public HealthInfoResDto updateHealth(Long diaryId, PartialHealthReqDto partialHealthReqDto) {
        Health health = (Health) diaryRepository.findById(diaryId).orElseThrow(() -> new NotFoundException("해당하는 일기가 없습니다."));

        health.setHealthType(partialHealthReqDto.getHealthType());

        if (StringUtils.hasText(partialHealthReqDto.getDescription())) {
            health.setDescription(partialHealthReqDto.getDescription());
        }

        health.setDate(partialHealthReqDto.getDate());

        if (partialHealthReqDto.getNextDueDate() != null) {
            health.setNextDueDate(partialHealthReqDto.getNextDueDate());
        }

        if (StringUtils.hasText(partialHealthReqDto.getClinic())) {
            health.setClinic(partialHealthReqDto.getClinic());
        }

        HealthInfoResDto healthInfoResDto = getHealthInfoResDto(health);
        return healthInfoResDto;
    }

    @Transactional
    public GroomingInfoResDto updateGrooming(Long diaryId, PartialGroomingReqDto partialHealthReqDto) {
        Grooming grooming = (Grooming) diaryRepository.findById(diaryId).orElseThrow(() -> new NotFoundException("해당하는 일기가 없습니다."));

        grooming.setGroomingType(partialHealthReqDto.getGroomingType());
        grooming.setDate(partialHealthReqDto.getDate());

        if(StringUtils.hasText(partialHealthReqDto.getDescription())){
            grooming.setDescription(partialHealthReqDto.getDescription());
        }

        GroomingInfoResDto groomingInfoResDto = getGroomingInfoResDto(grooming);
        return groomingInfoResDto;
    }

    @Transactional
    public MealInfoResDto updateMeal(Long diaryId, PartialMealReqDto partialMealReqDto) {
        Meal meal = (Meal) diaryRepository.findById(diaryId).orElseThrow(() -> new NotFoundException("해당하는 일기가 없습니다."));

        meal.setMealType(partialMealReqDto.getMealType());

        if(StringUtils.hasText(partialMealReqDto.getFoodBrand())){
            meal.setFoodBrand(partialMealReqDto.getFoodBrand());
        }

        if(partialMealReqDto.getFoodAmount() != null){
            meal.setFoodAmount(partialMealReqDto.getFoodAmount());
        }

        meal.setDate(partialMealReqDto.getDate());

        if(StringUtils.hasText(partialMealReqDto.getDescription())){
            meal.setDescription(partialMealReqDto.getDescription());
        }

        MealInfoResDto mealInfoResDto = getMealInfoResDto(meal);
        return mealInfoResDto;
    }

    private HealthInfoResDto getHealthInfoResDto(Health health) {
        return new HealthInfoResDto(
                health.getId(), health.getPet().getId(),
                health.getDate(), health.getDescription(),
                health.getHealthType(), health.getNextDueDate(),
                health.getClinic(), health.getCreateDate(), health.getUpdatedDate()
        );
    }

    private GroomingInfoResDto getGroomingInfoResDto(Grooming grooming) {
        return new GroomingInfoResDto(
                grooming.getId(), grooming.getPet().getId(),
                grooming.getDate(), grooming.getDescription(),
                grooming.getGroomingType(), grooming.getCreateDate(), grooming.getUpdatedDate()
        );
    }

    private  MealInfoResDto getMealInfoResDto(Meal meal) {
        return new MealInfoResDto(
                meal.getId(), meal.getPet().getId(), meal.getDate(),
                meal.getDescription(), meal.getMealType(),
                meal.getFoodBrand(), meal.getFoodAmount(),
                meal.getCreateDate(), meal.getUpdatedDate()
        );
    }
}
