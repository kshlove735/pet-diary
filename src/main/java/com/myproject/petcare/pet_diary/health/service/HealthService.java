package com.myproject.petcare.pet_diary.health.service;

import com.myproject.petcare.pet_diary.common.exception.custom_exception.NotFoundException;
import com.myproject.petcare.pet_diary.health.entity.Health;
import com.myproject.petcare.pet_diary.health.repository.HealthRepository;
import com.myproject.petcare.pet_diary.health.dto.HealthInfoResDto;
import com.myproject.petcare.pet_diary.health.dto.PartialHealthReqDto;
import com.myproject.petcare.pet_diary.pet.entity.Pet;
import com.myproject.petcare.pet_diary.pet.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HealthService {

    private final PetRepository petRepository;
    private final HealthRepository healthRepository;

    @Transactional
    public HealthInfoResDto createHealth(Long petId, PartialHealthReqDto partialHealthReqDto) {
        Pet pet = petRepository.findById(petId).orElseThrow(() -> new NotFoundException("해당하는 반려견이 없습니다."));

        Health health = new Health(
                pet, partialHealthReqDto.getHealthType(), partialHealthReqDto.getDescription(),
                partialHealthReqDto.getDate(), partialHealthReqDto.getNextDueDate(),
                partialHealthReqDto.getClinic(), partialHealthReqDto.getNotes()
        );

        healthRepository.save(health);

        HealthInfoResDto healthInfoResDto = getHealthInfoResDto(health, pet);
        return healthInfoResDto;
    }

    private HealthInfoResDto getHealthInfoResDto(Health health, Pet pet) {
        return new HealthInfoResDto(
                health.getId(), pet.getId(), health.getHealthType(),
                health.getDescription(), health.getDate(),
                health.getNextDueDate(), health.getClinic(), health.getNotes(),
                health.getCreateDate(), health.getUpdatedDate()
        );
    }
}
