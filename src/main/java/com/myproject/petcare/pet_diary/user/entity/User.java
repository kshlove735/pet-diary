package com.myproject.petcare.pet_diary.user.entity;

import com.myproject.petcare.pet_diary.common.entity.BaseEntity;
import com.myproject.petcare.pet_diary.user.enums.Role;
import jakarta.persistence.*;

@Entity
public class User extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    private String email;
    private String password;
    private String name;
    private String phone;

    @Enumerated(value = EnumType.STRING)
    private Role role;
}
