package com.myproject.petcare.pet_diary.health_record.repository;

import com.myproject.petcare.pet_diary.health_record.entity.HealthRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HealthRecordRepository extends JpaRepository<HealthRecord, Long> {
}
