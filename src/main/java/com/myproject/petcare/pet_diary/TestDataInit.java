package com.myproject.petcare.pet_diary;

import com.myproject.petcare.pet_diary.pet.entity.Pet;
import com.myproject.petcare.pet_diary.pet.enums.Gender;
import com.myproject.petcare.pet_diary.pet.repository.PetRepository;
import com.myproject.petcare.pet_diary.user.entity.User;
import com.myproject.petcare.pet_diary.user.enums.Role;
import com.myproject.petcare.pet_diary.user.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class TestDataInit {

    private final UserRepository userRepository;
    private final PetRepository petRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostConstruct
    public void init(){
        User user = new User();
        user.setEmail("test1@gmail.com");
        user.setPassword(bCryptPasswordEncoder.encode("TestPassword1!!"));
        user.setName("테스트유저1");
        user.setPhone("010-1234-1234");
        user.setRole(Role.USER);
        userRepository.save(user);

        Pet pet = new Pet();
        pet.setName("멍멍이1");
        pet.setBreed("포메라니안");
        pet.setBirthDate(LocalDate.parse("1993-10-20"));
        pet.setGender(Gender.FEMALE);
        pet.setWeight(new BigDecimal("5.23"));
        pet.setDescription(null);
        pet.changeUser(user);
        petRepository.save(pet);
    }
}
