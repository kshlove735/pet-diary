package com.myproject.petcare.pet_diary.health.entity;

import com.myproject.petcare.pet_diary.common.entity.BaseEntity;
import com.myproject.petcare.pet_diary.health.enums.HealthType;
import com.myproject.petcare.pet_diary.pet.entity.Pet;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.Comment;

import java.time.LocalDate;

@Entity
@Getter
public class Health extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "health_id")
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

    @Column(columnDefinition = "TEXT")
    @Comment("상세 내용")
    private String description;

    @Column(nullable = false)
    @Comment("기록 날짜")
    private LocalDate date;

    @Comment("다음 예정일(예: 다음 접종일)")
    private LocalDate nextDueDate;

    @Column(length = 50)
    @Comment("병원 이름")
    private String clinic;

    @Column(columnDefinition = "TEXT")
    @Comment("추가 메모")
    private String notes;

    public Health(Pet pet, HealthType healthType, String description, LocalDate date, LocalDate nextDueDate, String clinic, String notes) {
        this.pet = pet;
        this.healthType = healthType;
        this.description = description;
        this.date = date;
        this.nextDueDate = nextDueDate;
        this.clinic = clinic;
        this.notes = notes;
    }
}
