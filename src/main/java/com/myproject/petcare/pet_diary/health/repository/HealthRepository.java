package com.myproject.petcare.pet_diary.health.repository;

import com.myproject.petcare.pet_diary.health.entity.Health;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HealthRepository extends JpaRepository<Health, Long> {
}
