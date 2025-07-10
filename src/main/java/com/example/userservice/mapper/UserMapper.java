package com.example.userservice.mapper;

import com.example.userservice.dto.UserDTO;
import com.example.userservice.model.User;
import org.springframework.stereotype.Component;


/**
 * Маппер {@link User} <-> {@link UserDTO}
 *
 * @author vmarakushin
 * @version 1.0
 */
@Component
public class UserMapper {

    public UserDTO toDto(User user) {
        if (user == null) return null;

        return UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .surname(user.getSurname())
                .age(user.getAge())
                .phone(user.getPhone())
                .email(user.getEmail())
                .money(user.getMoney())
                .createdAt(user.getCreatedAt())
                .build();
    }

    public User toEntity(UserDTO dto) {
        if (dto == null) return null;

        return User.builder()
                .id(dto.getId())
                .name(dto.getName())
                .surname(dto.getSurname())
                .age(dto.getAge())
                .phone(dto.getPhone())
                .email(dto.getEmail())
                .money(dto.getMoney())
                .createdAt(dto.getCreatedAt())
                .build();
    }
}