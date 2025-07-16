package com.example.userservice.service;

import com.example.userservice.dto.UserDTO;
import com.example.userservice.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;


import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

/**
 * Тесты для валидатора
 * @author vmarakushin
 * @version 2.0
 */
@ExtendWith(MockitoExtension.class)
public class ValidatorTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    Validator validator;

    @DisplayName("Проверка валидных имен")
    @ParameterizedTest
    @ValueSource(strings = {
            "Василий",
            "Жумыт-Куйлук",
            "Dennis"
    })
    public void validNameShouldPass(String name) {
        assertEquals(name, validator.name(name));
    }

    @DisplayName("Проверка невалидных имен")
    @ParameterizedTest
    @ValueSource(strings = {
            "Васян465",
            "}{0TTABbl4",
            "NAGIBATOR777",
            ".:|{PYT0U_n3P3U:."
    })
    public void invalidNameShouldThrow(String name) {
        assertThrows(IllegalArgumentException.class, () -> validator.name(name));
    }

    @DisplayName("Проверка валидных фамилий")
    @ParameterizedTest
    @ValueSource(strings = {
            "Петров",
            "Гуйлумбалиев",
            "Peterson",
            "Корсикова-Романова"
    })
    public void validSurnameShouldPass(String surname) {
        assertEquals(surname, validator.surname(surname));
    }


    @DisplayName("Проверка невалидных фамилий")
    @ParameterizedTest
    @ValueSource(strings = {
            "@@@KPYTOU",
            "Из.Фильма",
            "NAGIBAYU777",
            "3-й раз замужем!!!"
    })
    public void invalidSurnameShouldThrow(String surname) {
        assertThrows(IllegalArgumentException.class, () -> validator.surname(surname));
    }

    @DisplayName("Проверка валидных возрастов")
    @ParameterizedTest
    @ValueSource(ints = {18, 21, 32, 95})
    public void validAgeShouldPass(int age) {
        assertEquals(age, validator.age(age));
    }

    @DisplayName("Проверка невалидных возрастов")
    @ParameterizedTest
    @ValueSource(ints = {-122, -1000, 151, 100500})
    public void invalidAgeShouldThrow(int age) {
        assertThrows(IllegalArgumentException.class, () -> validator.age(age));
    }


    @DisplayName("Проверка валидных номеров")
    @ParameterizedTest
    @ValueSource(strings = {
            "+79991112244",
            "+11111111111"
    })
    public void validPhoneNumberShouldPass(String phoneNumber) {
        assertEquals(phoneNumber, validator.phone(phoneNumber));
    }

    @DisplayName("Проверка невалидных номеров")
    @ParameterizedTest
    @ValueSource(strings = {
            "+1",
            "12345678901",
            "+7(999)-777-88-99",
            "ДА Я УСТАЛ СЮДА УЖЕ ТЕЛЕФОН ВБИВАТЬ!!!" //замучили беднягу
    })
    public void invalidPhoneNumberShouldThrow(String phoneNumber) {
        assertThrows(IllegalArgumentException.class, () -> validator.phone(phoneNumber));
    }


    @DisplayName("Проверка валидных адресов эл. почты")
    @ParameterizedTest
    @ValueSource(strings = {
            "example@example.com",
            "user.name123@mail.co",
            "my_email+filter@domain.org"
    })
    public void validEmailsShouldPass(String email) {
        assertEquals(email, validator.email(email));
    }

    @DisplayName("Проверка невалидных адресов эл. почты")
    @ParameterizedTest
    @ValueSource(strings = {
            "no-at-symbol.com",
            "bad@domain",
            "a@b.c",
            "wrong@@example.com",
            "user@.com"
    })
    public void invalidEmailsShouldThrow(String email) {
        assertThrows(IllegalArgumentException.class, () -> validator.email(email));
    }

    @DisplayName("Проверка валидатора validInt")
    @ParameterizedTest
    @ValueSource(strings = {"1", "1000", "14423", "-16384"})
    public void validIntShouldPass(String input) {
        int expected = Integer.parseInt(input);
        int actual = validator.validInt(input);
        assertEquals(expected, actual);
    }


    @DisplayName("Инвалидные для validInt")
    @ParameterizedTest
    @ValueSource(strings = {"shafbgish", "  fsagf336==sv"})
    public void inValidIntShouldThrow(String input){
        assertThrows(IllegalArgumentException.class, () -> validator.validInt(input));
    }


    @DisplayName("Проверка валидатора validLong")
    @ParameterizedTest
    @ValueSource(strings = {"1", "1000", "14423", "-16384", "1000000"})
    public void validLongShouldPass(String input) {
        long expected = Long.parseLong(input);
        long actual = validator.validLong(input);
        assertEquals(expected, actual);
    }

    @DisplayName("Проверка невалидных для ValidLong")
    @ParameterizedTest
    @ValueSource(strings = {"shafbgish", "  fsagf336==sv", "1_000_000L"})
    public void inValidLongShouldThrow(String input) {
        assertThrows(IllegalArgumentException.class, () -> validator.validLong(input));
    }

    @DisplayName("Проверка валидных ID")
    @ParameterizedTest
    @ValueSource(strings = {"1", "1500", "55672356"})
    public void validIdShouldPass(String input) {
        long expected = Long.parseLong(input);
        long actual = validator.validIdCreate(input,validator::id);
        assertEquals(expected, actual);
    }

    @DisplayName("Проверка невалидных ID")
    @ParameterizedTest
    @ValueSource(strings = {"-1", "-100","ID", "0"})
    public void invalidIdShouldThrow(String input) {
        assertThrows(IllegalArgumentException.class, () -> validator.validIdCreate(input,validator::id));
    }

    @DisplayName("Полная валидация ид !=0")
    @ParameterizedTest
    @ValueSource(longs = {1, 123, -121})
    public void createUserNot0Id(long input){
        UserDTO dto = new UserDTO(
                input,
                "Василий",
                "Пупкин",
                32,
                "+79992221122",
                "vasiliy@pupkin.com",
                0,
                new Date());
        assertThrows(IllegalArgumentException.class,() -> validator.fullValidation(dto, Validator.Scope.CREATE));
    }

    @DisplayName("Полная валидация ид =0")
    @Test
    public void createUser0Id(){
        UserDTO dto = new UserDTO(
                0,
                "Василий",
                "Пупкин",
                32,
                "+79992221122",
                "vasiliy@pupkin.com",
                0,
                new Date());

        doReturn(false).when(userRepository).existsByEmailAndIdNot(dto.getEmail(), dto.getId());
        doReturn(false).when(userRepository).existsByPhoneAndIdNot(dto.getPhone(), dto.getId());

        assertDoesNotThrow(() -> validator.fullValidation(dto, Validator.Scope.CREATE));
    }

    @DisplayName("Полная валидация при неуникальном email")
    @Test
    public void createUserNotUniqueEmail(){
        UserDTO dto = new UserDTO(
                0,
                "Василий",
                "Пупкин",
                32,
                "+79992221122",
                "vasiliy@pupkin.com",
                0,
                new Date());

        doReturn(true).when(userRepository).existsByEmailAndIdNot(dto.getEmail(), dto.getId());
        assertThrows(IllegalArgumentException.class,() -> validator.fullValidation(dto, Validator.Scope.CREATE));
    }

    @DisplayName("Полная валидация ид =0")
    @Test
    public void createUserNotUniquePhone(){
        UserDTO dto = new UserDTO(
                0,
                "Василий",
                "Пупкин",
                32,
                "+79992221122",
                "vasiliy@pupkin.com",
                0,
                new Date());

        doReturn(false).when(userRepository).existsByEmailAndIdNot(dto.getEmail(), dto.getId());
        doReturn(true).when(userRepository).existsByPhoneAndIdNot(dto.getPhone(), dto.getId());

        assertThrows(IllegalArgumentException.class,() -> validator.fullValidation(dto, Validator.Scope.CREATE));
    }

    @DisplayName("Полная валидация обновление ид некорректен")
    @ParameterizedTest
    @ValueSource(longs = {0, -32111, -121})
    public void updateUserNotCorrectID(long input){
        UserDTO dto = new UserDTO(
                input,
                "Василий",
                "Пупкин",
                32,
                "+79992221122",
                "vasiliy@pupkin.com",
                0,
                new Date());
        assertThrows(IllegalArgumentException.class,() -> validator.fullValidation(dto, Validator.Scope.UPDATE));
    }

}