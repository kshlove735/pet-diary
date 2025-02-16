package com.myproject.petcare.pet_diary.grooming.entity;

import com.myproject.petcare.pet_diary.common.entity.BaseEntity;
import com.myproject.petcare.pet_diary.grooming.enums.GroomingType;
import com.myproject.petcare.pet_diary.pet.entity.Pet;
import jakarta.persistence.*;
import org.hibernate.annotations.Comment;

import java.time.LocalDate;

@Entity
public class Grooming extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "grooming_id")
    @Comment("미용 기록 ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id", nullable = false)
    @Comment("반려견 ID")
    private Pet pet;

    @Column(nullable = false)
    @Comment("미용 날짜")
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Comment("미용 유형(목욕, 이발, 발톱 손질, 귀 청소, 치아 관리)")
    private GroomingType groomingType;

    @Column(columnDefinition = "TEXT")
    @Comment("추가 메모")
    private String notes;
}