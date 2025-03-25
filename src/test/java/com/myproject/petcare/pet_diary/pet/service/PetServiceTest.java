package com.myproject.petcare.pet_diary.pet.service;

import com.myproject.petcare.pet_diary.common.exception.custom_exception.NotFoundException;
import com.myproject.petcare.pet_diary.jwt.CustomUserDetails;
import com.myproject.petcare.pet_diary.pet.dto.CreatePetReqDto;
import com.myproject.petcare.pet_diary.pet.dto.PetInfoResDto;
import com.myproject.petcare.pet_diary.pet.entity.Pet;
import com.myproject.petcare.pet_diary.pet.enums.Gender;
import com.myproject.petcare.pet_diary.pet.repository.PetRepository;
import com.myproject.petcare.pet_diary.user.entity.User;
import com.myproject.petcare.pet_diary.user.enums.Role;
import com.myproject.petcare.pet_diary.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@RequiredArgsConstructor
@Transactional
class PetServiceTest {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PetService petService;

    @Autowired
    private PetRepository petRepository;

    private User testUser;
    private Pet testPet;
    private CustomUserDetails customUserDetails;

    @BeforeEach
    void before() {
        testUser = new User();
        testUser.setEmail("test1@gmail.com");
        testUser.setPassword(bCryptPasswordEncoder.encode("TestPassword1!!"));
        testUser.setName("테스트유저1");
        testUser.setPhone("010-1234-1234");
        testUser.setRole(Role.USER);
        userRepository.save(testUser);

        customUserDetails = new CustomUserDetails(testUser);

        testPet = new Pet();
        testPet.setName("멍멍이");
        testPet.setBreed("포메라니안");
        testPet.setBirthDate(LocalDate.parse("1993-10-20"));
        testPet.setGender(Gender.FEMALE);
        testPet.setWeight(new BigDecimal("5.23"));
        testPet.setDescription(null);
        testPet.changeUser(testUser);
        petRepository.save(testPet);
    }

    @Test
    @DisplayName("반려견 등록 성공")
    void createPetSuccess() {
        // given
        CreatePetReqDto createPetReqDto = new CreatePetReqDto();
        createPetReqDto.setName("나비");
        createPetReqDto.setBreed("푸들");
        createPetReqDto.setBirthDate(LocalDate.parse("1989-09-14"));
        createPetReqDto.setGender(Gender.MALE);
        createPetReqDto.setWeight(new BigDecimal("10.23"));
        createPetReqDto.setDescription("예민함");

        // when
        PetInfoResDto petInfoResDto = petService.createPet(createPetReqDto, customUserDetails);

        // then
        assertThat(petInfoResDto.getName()).isEqualTo(createPetReqDto.getName());
        assertThat(petInfoResDto.getBreed()).isEqualTo(createPetReqDto.getBreed());
        assertThat(petInfoResDto.getBirthDate()).isEqualTo(createPetReqDto.getBirthDate());
        assertThat(petInfoResDto.getGender()).isEqualTo(createPetReqDto.getGender());
        assertThat(petInfoResDto.getWeight()).isEqualTo(createPetReqDto.getWeight());
        assertThat(petInfoResDto.getDescription()).isEqualTo(createPetReqDto.getDescription());
    }

    @Test
    @DisplayName("반려견 단일 조회 성공")
    void getPetSuccess() {
        // given

        // when
        PetInfoResDto petInfoResDto = petService.getPet(testPet.getId());

        //then
        assertThat(petInfoResDto.getName()).isEqualTo(testPet.getName());
        assertThat(petInfoResDto.getBreed()).isEqualTo(testPet.getBreed());
        assertThat(petInfoResDto.getBirthDate()).isEqualTo(testPet.getBirthDate());
        assertThat(petInfoResDto.getGender()).isEqualTo(testPet.getGender());
        assertThat(petInfoResDto.getWeight()).isEqualTo(testPet.getWeight());
        assertThat(petInfoResDto.getDescription()).isEqualTo(testPet.getDescription());
    }

    @Test
    @DisplayName("반려견 단일 조회 실패")
    void getPetFail() {
        // given

        // when & then
        assertThrows(NotFoundException.class,() -> petService.getPet(13L));
    }

}