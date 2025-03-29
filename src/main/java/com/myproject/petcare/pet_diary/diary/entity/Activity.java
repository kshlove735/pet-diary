package com.myproject.petcare.pet_diary.diary.entity;

import com.myproject.petcare.pet_diary.diary.enums.ActivityType;
import com.myproject.petcare.pet_diary.pet.entity.Pet;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@DiscriminatorValue("activity")
@NoArgsConstructor
public class Activity extends Diary {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Comment("활동 유형(산책, 놀이, 훈련, 수영)")
    private ActivityType activityType;

    @Comment("활동 시간(분)")
    private int duration;

    @Column(precision = 5, scale = 2)
    @Comment("활동 거리(m)")
    private BigDecimal distance;

    @Column(length = 255)
    @Comment("활동 장소")
    private String location;

    public Activity(Pet pet, LocalDate date, String description) {
        super(pet, date, description);
    }
}
