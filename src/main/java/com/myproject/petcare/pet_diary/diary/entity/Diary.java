package com.myproject.petcare.pet_diary.diary.entity;

import com.myproject.petcare.pet_diary.common.entity.BaseEntity;
import com.myproject.petcare.pet_diary.pet.entity.Pet;
import jakarta.persistence.*;
import org.hibernate.annotations.Comment;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn
public class Diary extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "diary_id")
    @Comment("일기 ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id", nullable = false)
    @Comment("반려견 ID")
    private Pet pet;

    @Column(columnDefinition = "TEXT")
    @Comment("추가 메모")
    private String note;
}
