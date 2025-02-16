package com.myproject.petcare.pet_diary.meal.entity;

import com.myproject.petcare.pet_diary.common.entity.BaseEntity;
import com.myproject.petcare.pet_diary.meal.enums.MealType;
import com.myproject.petcare.pet_diary.pet.entity.Pet;
import jakarta.persistence.*;
import org.hibernate.annotations.Comment;

import java.time.LocalDate;

@Entity
public class Meal extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "meal_id")
    @Comment("식사 기록 ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id", nullable = false)
    @Comment("반려견 ID")
    private Pet pet;

    @Column(nullable = false)
    @Comment("식사 날짜")
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Comment("식사 유형(아침, 점심, 저녁, 간식)")
    private MealType mealType;

    @Column(length = 255)
    @Comment("사료 브랜드")
    private String foodBrand;

    @Comment("급여량(g)")
    private int foodAmount;

    @Column(columnDefinition = "TEXT")
    @Comment("반응(알레르기 여부 등)")
    private String reaction;

    @Column(columnDefinition = "TEXT")
    @Comment("추가 메모")
    private String note;
}
