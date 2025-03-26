package com.myproject.petcare.pet_diary.health_record.service;

import com.myproject.petcare.pet_diary.health_record.dto.HealthRecordInfoResDto;
import com.myproject.petcare.pet_diary.health_record.dto.PartialHealthRecordReqDto;
import com.myproject.petcare.pet_diary.health_record.enums.HealthType;
import com.myproject.petcare.pet_diary.jwt.CustomUserDetails;
import com.myproject.petcare.pet_diary.pet.entity.Pet;
import com.myproject.petcare.pet_diary.pet.enums.Gender;
import com.myproject.petcare.pet_diary.pet.repository.PetRepository;
import com.myproject.petcare.pet_diary.user.entity.User;
import com.myproject.petcare.pet_diary.user.enums.Role;
import com.myproject.petcare.pet_diary.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@RequiredArgsConstructor
@Transactional
class HealthRecordServiceTest {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private HealthRecordService healthRecordService;


    private User testUser;
    private Pet testPet1;
    private CustomUserDetails customUserDetails;

    @BeforeEach
    void before() {
        testUser = new User();
        testUser.setEmail("test2@gmail.com");
        testUser.setPassword(bCryptPasswordEncoder.encode("TestPassword2!!"));
        testUser.setName("테스트유저1");
        testUser.setPhone("010-1234-1234");
        testUser.setRole(Role.USER);
        userRepository.save(testUser);

        customUserDetails = new CustomUserDetails(testUser);

        testPet1 = new Pet();
        testPet1.setName("멍멍이1");
        testPet1.setBreed("포메라니안");
        testPet1.setBirthDate(LocalDate.parse("1993-10-20"));
        testPet1.setGender(Gender.FEMALE);
        testPet1.setWeight(new BigDecimal("5.23"));
        testPet1.setDescription(null);
        testPet1.changeUser(testUser);
        petRepository.save(testPet1);

        Pet testPet2 = new Pet();
        testPet2.setName("멍멍이2");
        testPet2.setBreed("포메라니안");
        testPet2.setBirthDate(LocalDate.parse("1993-11-10"));
        testPet2.setGender(Gender.MALE);
        testPet2.setWeight(new BigDecimal("3.23"));
        testPet2.setDescription(null);
        testPet2.changeUser(testUser);
        petRepository.save(testPet2);
    }

    @Test
    @DisplayName("건강 기록 등록 성공")
    void createHealthRecordSuccess() {

        PartialHealthRecordReqDto partialHealthRecordReqDto1 = new PartialHealthRecordReqDto();
        partialHealthRecordReqDto1.setHealthType(HealthType.VACCINATION);
        partialHealthRecordReqDto1.setDescription("광견병 예방접종 완료");
        partialHealthRecordReqDto1.setDate(LocalDate.parse("2025-03-25"));
        partialHealthRecordReqDto1.setNextDueDate(LocalDate.parse("2026-03-25"));
        partialHealthRecordReqDto1.setClinic("행복 동물병원");
        partialHealthRecordReqDto1.setNotes("부작용 없음");

        HealthRecordInfoResDto healthRecordInfoResDto1 = healthRecordService.createHealthRecord(testPet1.getId(), partialHealthRecordReqDto1);

        assertThat(healthRecordInfoResDto1.getHealthType()).isEqualTo(partialHealthRecordReqDto1.getHealthType());
        assertThat(healthRecordInfoResDto1.getDescription()).isEqualTo(partialHealthRecordReqDto1.getDescription());
        assertThat(healthRecordInfoResDto1.getDate()).isEqualTo(partialHealthRecordReqDto1.getDate());
        assertThat(healthRecordInfoResDto1.getNextDueDate()).isEqualTo(partialHealthRecordReqDto1.getNextDueDate());
        assertThat(healthRecordInfoResDto1.getClinic()).isEqualTo(partialHealthRecordReqDto1.getClinic());
        assertThat(healthRecordInfoResDto1.getNotes()).isEqualTo(partialHealthRecordReqDto1.getNotes());

        PartialHealthRecordReqDto partialHealthRecordReqDto2 = new PartialHealthRecordReqDto();
        partialHealthRecordReqDto2.setHealthType(HealthType.VACCINATION);
        partialHealthRecordReqDto2.setDate(LocalDate.parse("2025-03-25"));

        HealthRecordInfoResDto healthRecordInfoResDto2 = healthRecordService.createHealthRecord(testPet1.getId(), partialHealthRecordReqDto2);

        assertThat(healthRecordInfoResDto2.getHealthType()).isEqualTo(partialHealthRecordReqDto2.getHealthType());
        assertThat(healthRecordInfoResDto2.getDescription()).isEqualTo(partialHealthRecordReqDto2.getDescription());
        assertThat(healthRecordInfoResDto2.getDate()).isEqualTo(partialHealthRecordReqDto2.getDate());
        assertThat(healthRecordInfoResDto2.getNextDueDate()).isEqualTo(partialHealthRecordReqDto2.getNextDueDate());
        assertThat(healthRecordInfoResDto2.getClinic()).isEqualTo(partialHealthRecordReqDto2.getClinic());
        assertThat(healthRecordInfoResDto2.getNotes()).isEqualTo(partialHealthRecordReqDto2.getNotes());
    }

    @Test
    @DisplayName("건강 기록 등록 실패")
    void createHealthRecordFail() {

        PartialHealthRecordReqDto partialHealthRecordReqDto1 = new PartialHealthRecordReqDto();
        partialHealthRecordReqDto1.setDescription("광견병 예방접종 완료");
        partialHealthRecordReqDto1.setDate(LocalDate.parse("2025-03-25"));
        partialHealthRecordReqDto1.setNextDueDate(LocalDate.parse("2026-03-25"));
        partialHealthRecordReqDto1.setClinic("행복 동물병원");
        partialHealthRecordReqDto1.setNotes("부작용 없음");

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> healthRecordService.createHealthRecord(testPet1.getId(), partialHealthRecordReqDto1));

        PartialHealthRecordReqDto partialHealthRecordReqDto2 = new PartialHealthRecordReqDto();
        partialHealthRecordReqDto2.setHealthType(HealthType.VACCINATION);
        partialHealthRecordReqDto2.setDescription("광견병 예방접종 완료");
        partialHealthRecordReqDto2.setNextDueDate(LocalDate.parse("2026-03-25"));
        partialHealthRecordReqDto2.setClinic("행복 동물병원");
        partialHealthRecordReqDto2.setNotes("부작용 없음");

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> healthRecordService.createHealthRecord(testPet1.getId(), partialHealthRecordReqDto2));
    }

}