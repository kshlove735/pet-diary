package com.myproject.petcare.pet_diary.diary.entity;

import com.myproject.petcare.pet_diary.diary.enums.MealType;
import com.myproject.petcare.pet_diary.pet.entity.Pet;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.time.LocalDate;

@Entity
@DiscriminatorValue("meal")
@NoArgsConstructor
public class Meal extends Diary {

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


    public Meal(Pet pet, LocalDate date, String description) {
        super(pet, date, description);
    }
}
