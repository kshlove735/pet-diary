package com.myproject.petcare.pet_diary.diary.entity;

import com.myproject.petcare.pet_diary.diary.enums.GroomingType;
import jakarta.persistence.*;
import org.hibernate.annotations.Comment;

import java.time.LocalDate;

@Entity
@DiscriminatorValue("grooming")
public class Grooming extends Diary {

    @Column(nullable = false)
    @Comment("미용 날짜")
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Comment("미용 유형(목욕, 이발, 발톱 손질, 귀 청소, 치아 관리)")
    private GroomingType groomingType;

}