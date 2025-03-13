package com.myproject.petcare.pet_diary.activity.entity;

import com.myproject.petcare.pet_diary.activity.enums.ActivityType;
import com.myproject.petcare.pet_diary.common.entity.BaseEntity;
import com.myproject.petcare.pet_diary.pet.entity.Pet;
import jakarta.persistence.*;
import org.hibernate.annotations.Comment;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class Activity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "activity_id")
    @Comment("활동 기록 ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id", nullable = false)
    @Comment("반려견 ID")
    private Pet pet;

    @Column(nullable = false)
    @Comment("활동 날짜")
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Comment("활동 유형(산책, 놀이, 훈련, 수영)")
    private ActivityType activityType;

    @Comment("활동 시간(분)")
    private int duration;

    @Column(precision = 5, scale = 2)
    @Comment("활동 시간(분)")
    private BigDecimal distance;

    @Column(length = 255)
    @Comment("활동 장소")
    private String location;

    @Column(columnDefinition = "TEXT")
    @Comment("추가 메모")
    private String notes;
}
