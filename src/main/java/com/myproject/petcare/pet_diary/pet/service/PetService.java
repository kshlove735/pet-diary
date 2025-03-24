package com.myproject.petcare.pet_diary.pet.service;

import com.myproject.petcare.pet_diary.jwt.CustomUserDetails;
import com.myproject.petcare.pet_diary.pet.dto.CreatePetReqDto;
import com.myproject.petcare.pet_diary.pet.dto.PetInfoResDto;
import com.myproject.petcare.pet_diary.pet.entity.Pet;
import com.myproject.petcare.pet_diary.pet.repository.PetRepository;
import com.myproject.petcare.pet_diary.user.entity.User;
import com.myproject.petcare.pet_diary.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PetService {

    private final UserRepository userRepository;
    private final PetRepository petRepository;

    @Transactional
    public PetInfoResDto createPet(CreatePetReqDto createPetReqDto, CustomUserDetails customUserDetails) {
        User user = getUserFromUserDetails(customUserDetails);

        Pet pet = new Pet();
        pet.setName(createPetReqDto.getName());
        pet.setBreed(createPetReqDto.getBreed());
        pet.setBirthDate(createPetReqDto.getBirthDate());
        pet.setGender(createPetReqDto.getGender());
        pet.setWeight(createPetReqDto.getWeight());
        pet.setDescription(createPetReqDto.getDescription());
        pet.changeUser(user);
        petRepository.save(pet);

        PetInfoResDto petInfoResDto = new PetInfoResDto(
                pet.getId(),pet.getName(),pet.getBreed(),pet.getBirthDate(),
                pet.getGender(), pet.getWeight(), pet.getDescription(),pet.getCreateDate(), pet.getUpdatedDate());

        return petInfoResDto;
    }


    private User getUserFromUserDetails(CustomUserDetails customUserDetails) {
        Long id = Long.valueOf(customUserDetails.getUsername());
        User user = userRepository.findById(id).orElse(null);
        return user;
    }
}
