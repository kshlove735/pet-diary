package com.myproject.petcare.pet_diary.health_record.entity;

import com.myproject.petcare.pet_diary.common.entity.BaseEntity;
import com.myproject.petcare.pet_diary.health_record.enums.HealthType;
import com.myproject.petcare.pet_diary.pet.entity.Pet;
import jakarta.persistence.*;
import org.hibernate.annotations.Comment;

import java.time.LocalDate;

@Entity
public class HealthRecord extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "health_record_id")
    @Comment("건강 기록 ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id", nullable = false)
    @Comment("반려견 ID")
    private Pet pet;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Comment("기록 유형(예방접종, 건강검진, 수술, 투약)")
    private HealthType healthType;

    @Column(columnDefinition = "TEXT", nullable = false)
    @Comment("상세 내용")
    private String description;

    @Column(nullable = false)
    @Comment("기록 날짜")
    private LocalDate date;

    @Comment("다음 예정일(예: 다음 접종일)")
    private LocalDate nextDueDate;

    @Column(length = 255)
    @Comment("병원 이름")
    private String clinic;

    @Column(columnDefinition = "TEXT")
    @Comment("추가 메모")
    private String notes;
}
