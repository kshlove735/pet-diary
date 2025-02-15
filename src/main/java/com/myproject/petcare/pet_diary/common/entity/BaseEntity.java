package com.myproject.petcare.pet_diary.common.entity;

import jakarta.persistence.MappedSuperclass;

import java.time.LocalDateTime;

@MappedSuperclass
public class BaseEntity {
    private LocalDateTime createDate;
    private LocalDateTime updatedDate;
}
