package com.myproject.petcare.pet_diary.health_record.service;

import com.myproject.petcare.pet_diary.common.exception.custom_exception.NotFoundException;
import com.myproject.petcare.pet_diary.health_record.dto.HealthRecordInfoResDto;
import com.myproject.petcare.pet_diary.health_record.dto.PartialHealthRecordReqDto;
import com.myproject.petcare.pet_diary.health_record.entity.HealthRecord;
import com.myproject.petcare.pet_diary.health_record.repository.HealthRecordRepository;
import com.myproject.petcare.pet_diary.pet.entity.Pet;
import com.myproject.petcare.pet_diary.pet.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HealthRecordService {

    private final PetRepository petRepository;
    private final HealthRecordRepository healthRecordRepository;

    @Transactional
    public HealthRecordInfoResDto createHealthRecord(Long petId, PartialHealthRecordReqDto partialHealthRecordReqDto) {
        Pet pet = petRepository.findById(petId).orElseThrow(() -> new NotFoundException("해당하는 반려견이 없습니다."));

        HealthRecord healthRecord = new HealthRecord(
                pet, partialHealthRecordReqDto.getHealthType(), partialHealthRecordReqDto.getDescription(),
                partialHealthRecordReqDto.getDate(), partialHealthRecordReqDto.getNextDueDate(),
                partialHealthRecordReqDto.getClinic(), partialHealthRecordReqDto.getNotes()
        );

        healthRecordRepository.save(healthRecord);

        HealthRecordInfoResDto healthRecordInfoResDto = getHealthRecordInfoResDto(healthRecord, pet);
        return healthRecordInfoResDto;
    }

    private HealthRecordInfoResDto getHealthRecordInfoResDto(HealthRecord healthRecord, Pet pet) {
        return new HealthRecordInfoResDto(
                healthRecord.getId(), pet.getId(), healthRecord.getHealthType(),
                healthRecord.getDescription(), healthRecord.getDate(),
                healthRecord.getNextDueDate(), healthRecord.getClinic(), healthRecord.getNotes(),
                healthRecord.getCreateDate(), healthRecord.getUpdatedDate()
        );
    }
}
