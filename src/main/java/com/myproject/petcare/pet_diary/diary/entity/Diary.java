package com.myproject.petcare.pet_diary.diary.entity;

import com.myproject.petcare.pet_diary.common.entity.BaseEntity;
import com.myproject.petcare.pet_diary.pet.entity.Pet;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;

import java.time.LocalDate;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn
@NoArgsConstructor
@Getter
@Setter
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

    @Column(nullable = false)
    @Comment("일기 행동 날짜")
    private LocalDate date;

    @Column(columnDefinition = "TEXT")
    @Comment("추가 메모")
    private String description;

    public Diary(Pet pet, LocalDate date, String description) {
        this.pet = pet;
        this.date = date;
        this.description = description;
    }
}
