package com.myproject.petcare.pet_diary.pet.service;

import com.myproject.petcare.pet_diary.jwt.CustomUserDetails;
import com.myproject.petcare.pet_diary.pet.dto.CreatePetReqDto;
import com.myproject.petcare.pet_diary.pet.dto.PetInfoResDto;
import com.myproject.petcare.pet_diary.pet.enums.Gender;
import com.myproject.petcare.pet_diary.user.entity.User;
import com.myproject.petcare.pet_diary.user.enums.Role;
import com.myproject.petcare.pet_diary.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDate;

@SpringBootTest
@RequiredArgsConstructor
class PetServiceTest {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PetService petService;

    private User testUser;
    private CustomUserDetails customUserDetails;

    @BeforeEach
    void before(){
        testUser = new User();
        testUser.setEmail("test1@gmail.com");
        testUser.setPassword(bCryptPasswordEncoder.encode("TestPassword1!!"));
        testUser.setName("테스트유저1");
        testUser.setPhone("010-1234-1234");
        testUser.setRole(Role.USER);
        userRepository.save(testUser);

        customUserDetails = new CustomUserDetails(testUser);
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
        Assertions.assertThat(petInfoResDto.getName()).isEqualTo(createPetReqDto.getName());
        Assertions.assertThat(petInfoResDto.getBreed()).isEqualTo(createPetReqDto.getBreed());
        Assertions.assertThat(petInfoResDto.getBirthDate()).isEqualTo(createPetReqDto.getBirthDate());
        Assertions.assertThat(petInfoResDto.getGender()).isEqualTo(createPetReqDto.getGender());
        Assertions.assertThat(petInfoResDto.getWeight()).isEqualTo(createPetReqDto.getWeight());
        Assertions.assertThat(petInfoResDto.getDescription()).isEqualTo(createPetReqDto.getDescription());
    }



}