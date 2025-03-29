package com.myproject.petcare.pet_diary.diary.service;

import com.myproject.petcare.pet_diary.common.exception.custom_exception.NotFoundException;
import com.myproject.petcare.pet_diary.diary.dto.GroomingInfoResDto;
import com.myproject.petcare.pet_diary.diary.dto.HealthInfoResDto;
import com.myproject.petcare.pet_diary.diary.dto.PartialGroomingReqDto;
import com.myproject.petcare.pet_diary.diary.dto.PartialHealthReqDto;
import com.myproject.petcare.pet_diary.diary.entity.Grooming;
import com.myproject.petcare.pet_diary.diary.entity.Health;
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

}
