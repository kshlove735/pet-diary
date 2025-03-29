package com.myproject.petcare.pet_diary.diary.repository;

import com.myproject.petcare.pet_diary.diary.entity.Diary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiaryRepository extends JpaRepository<Diary, Long> {
}
