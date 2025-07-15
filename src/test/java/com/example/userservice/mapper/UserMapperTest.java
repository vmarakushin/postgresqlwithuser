package com.example.userservice.mapper;


import com.example.userservice.dto.UserDTO;
import com.example.userservice.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import java.util.Date;


/**
 * Тесты маппера
 *
 * @author vmarakushin
 * @version 1.0
 */
public class UserMapperTest {

    UserMapper mapper = new UserMapper();

    @DisplayName("Тест маппинга ДТО -> энтити")
    @Test
    public void testDtoToEntity() {

        Date date = new Date();

        UserDTO userDTO = UserDTO.builder()
                .id(0L)
                .name("Vasya")
                .surname("Petrov")
                .age(32)
                .phone("+79991112233")
                .email("vasya@petrov.com")
                .money(386L)
                .createdAt(date)
                .build();

        User expected = User.builder()
                .id(0L)
                .name("Vasya")
                .surname("Petrov")
                .age(32)
                .phone("+79991112233")
                .email("vasya@petrov.com")
                .money(386L)
                .createdAt(date)
                .build();

        User actual = mapper.toEntity(userDTO);

        Assertions.assertEquals(expected, actual);
    }

    @DisplayName("Тест маппинга энтити -> ДТО")
    @Test
    public void testEntityToDto() {

        Date date = new Date();

        User user = User.builder()
                .id(0L)
                .name("Vasya")
                .surname("Petrov")
                .age(32)
                .phone("+79991112233")
                .email("vasya@petrov.com")
                .money(386L)
                .createdAt(date)
                .build();

        UserDTO expected = UserDTO.builder()
                .id(0L)
                .name("Vasya")
                .surname("Petrov")
                .age(32)
                .phone("+79991112233")
                .email("vasya@petrov.com")
                .money(386L)
                .createdAt(date)
                .build();

        UserDTO actual = mapper.toDto(user);

        Assertions.assertEquals(expected, actual);
    }
}
