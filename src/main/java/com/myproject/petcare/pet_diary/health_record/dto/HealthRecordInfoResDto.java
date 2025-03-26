package com.myproject.petcare.pet_diary.health_record.dto;

import com.myproject.petcare.pet_diary.health_record.enums.HealthType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class HealthRecordInfoResDto {

    private Long healthRecordId;
    private Long petId;
    private HealthType healthType;
    private String description;
    private LocalDate date;
    private LocalDate nextDueDate;
    private String clinic;
    private String notes;
    private LocalDateTime createDate;
    private LocalDateTime updatedDate;
}
