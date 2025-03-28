package com.myproject.petcare.pet_diary.health.service;

import com.myproject.petcare.pet_diary.diary.dto.HealthInfoResDto;
import com.myproject.petcare.pet_diary.diary.dto.PartialHealthReqDto;
import com.myproject.petcare.pet_diary.diary.enums.HealthType;
import com.myproject.petcare.pet_diary.diary.service.DiaryService;
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
class HealthServiceTest {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private DiaryService diaryService;


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
    void createHealthSuccess() {

        PartialHealthReqDto partialHealthReqDto1 = new PartialHealthReqDto();
        partialHealthReqDto1.setHealthType(HealthType.VACCINATION);
        partialHealthReqDto1.setDescription("광견병 예방접종 완료");
        partialHealthReqDto1.setDate(LocalDate.parse("2025-03-25"));
        partialHealthReqDto1.setNextDueDate(LocalDate.parse("2026-03-25"));
        partialHealthReqDto1.setClinic("행복 동물병원");
        partialHealthReqDto1.setNotes("부작용 없음");

        HealthInfoResDto healthInfoResDto1 = diaryService.createHealth(testPet1.getId(), partialHealthReqDto1);

        assertThat(healthInfoResDto1.getHealthType()).isEqualTo(partialHealthReqDto1.getHealthType());
        assertThat(healthInfoResDto1.getDescription()).isEqualTo(partialHealthReqDto1.getDescription());
        assertThat(healthInfoResDto1.getDate()).isEqualTo(partialHealthReqDto1.getDate());
        assertThat(healthInfoResDto1.getNextDueDate()).isEqualTo(partialHealthReqDto1.getNextDueDate());
        assertThat(healthInfoResDto1.getClinic()).isEqualTo(partialHealthReqDto1.getClinic());
        assertThat(healthInfoResDto1.getNotes()).isEqualTo(partialHealthReqDto1.getNotes());

        PartialHealthReqDto partialHealthReqDto2 = new PartialHealthReqDto();
        partialHealthReqDto2.setHealthType(HealthType.VACCINATION);
        partialHealthReqDto2.setDate(LocalDate.parse("2025-03-25"));

        HealthInfoResDto healthInfoResDto2 = diaryService.createHealth(testPet1.getId(), partialHealthReqDto2);

        assertThat(healthInfoResDto2.getHealthType()).isEqualTo(partialHealthReqDto2.getHealthType());
        assertThat(healthInfoResDto2.getDescription()).isEqualTo(partialHealthReqDto2.getDescription());
        assertThat(healthInfoResDto2.getDate()).isEqualTo(partialHealthReqDto2.getDate());
        assertThat(healthInfoResDto2.getNextDueDate()).isEqualTo(partialHealthReqDto2.getNextDueDate());
        assertThat(healthInfoResDto2.getClinic()).isEqualTo(partialHealthReqDto2.getClinic());
        assertThat(healthInfoResDto2.getNotes()).isEqualTo(partialHealthReqDto2.getNotes());
    }

    @Test
    @DisplayName("건강 기록 등록 실패")
    void createHealthFail() {

        PartialHealthReqDto partialHealthReqDto1 = new PartialHealthReqDto();
        partialHealthReqDto1.setDescription("광견병 예방접종 완료");
        partialHealthReqDto1.setDate(LocalDate.parse("2025-03-25"));
        partialHealthReqDto1.setNextDueDate(LocalDate.parse("2026-03-25"));
        partialHealthReqDto1.setClinic("행복 동물병원");
        partialHealthReqDto1.setNotes("부작용 없음");

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> diaryService.createHealth(testPet1.getId(), partialHealthReqDto1));

        PartialHealthReqDto partialHealthReqDto2 = new PartialHealthReqDto();
        partialHealthReqDto2.setHealthType(HealthType.VACCINATION);
        partialHealthReqDto2.setDescription("광견병 예방접종 완료");
        partialHealthReqDto2.setNextDueDate(LocalDate.parse("2026-03-25"));
        partialHealthReqDto2.setClinic("행복 동물병원");
        partialHealthReqDto2.setNotes("부작용 없음");

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> diaryService.createHealth(testPet1.getId(), partialHealthReqDto2));
    }

}