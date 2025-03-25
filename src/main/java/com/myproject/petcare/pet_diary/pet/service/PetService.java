package com.myproject.petcare.pet_diary.pet.service;

import com.myproject.petcare.pet_diary.common.exception.custom_exception.NotFoundException;
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

import java.util.ArrayList;
import java.util.List;

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

        return getPetInfoResDto(pet);
    }

    public PetInfoResDto getPet(Long petId) {
        Pet pet = petRepository.findById(petId).orElseThrow(() -> new NotFoundException("해당하는 유저가 없습니다."));
        return getPetInfoResDto(pet);
    }

    public List<PetInfoResDto> getPets(CustomUserDetails customUserDetails) {
        User user = getUserFromUserDetails(customUserDetails);
        List<Pet> pets = user.getPets();

        List<PetInfoResDto> list = new ArrayList<>();
        for (Pet pet : pets) {
            list.add(getPetInfoResDto(pet));
        }
        return list;
    }

    private User getUserFromUserDetails(CustomUserDetails customUserDetails) {
        Long id = Long.valueOf(customUserDetails.getUsername());
        User user = userRepository.findById(id).orElse(null);
        return user;
    }

    private PetInfoResDto getPetInfoResDto(Pet pet) {
        return new PetInfoResDto(
                pet.getId(), pet.getName(), pet.getBreed(), pet.getBirthDate(),
                pet.getGender(), pet.getWeight(), pet.getDescription(), pet.getCreateDate(), pet.getUpdatedDate());
    }


}
