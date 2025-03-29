package com.myproject.petcare.pet_diary.diary.dto;

import com.myproject.petcare.pet_diary.diary.enums.GroomingType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class GroomingInfoResDto {

    private Long diaryId;
    private Long petId;
    private LocalDate date;
    private String description;

    private GroomingType groomingType;

    private LocalDateTime createDate;
    private LocalDateTime updatedDate;
}
