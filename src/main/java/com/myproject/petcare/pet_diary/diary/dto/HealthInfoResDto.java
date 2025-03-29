package com.myproject.petcare.pet_diary.diary.dto;

import com.myproject.petcare.pet_diary.diary.enums.HealthType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class HealthInfoResDto {

    private Long diaryId;
    private Long petId;
    private HealthType healthType;
    private String description;
    private LocalDate date;
    private LocalDate nextDueDate;
    private String clinic;
    private LocalDateTime createDate;
    private LocalDateTime updatedDate;
}
