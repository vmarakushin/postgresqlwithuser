package com.example.userservice.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;


import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты для валидатора
 * @author vmarakushin
 * @version 1.0
 */
public class ValidatorTest {


    @DisplayName("Проверка валидных имен")
    @ParameterizedTest
    @ValueSource(strings = {
            "Василий",
            "Жумыт-Куйлук",
            "Dennis"
    })
    public void validNameShouldPass(String name) {
        assertEquals(name, Validator.name(name));
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
        assertThrows(IllegalArgumentException.class, () -> Validator.name(name));
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
        assertEquals(surname, Validator.surname(surname));
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
        assertThrows(IllegalArgumentException.class, () -> Validator.surname(surname));
    }

    @DisplayName("Проверка валидных возрастов")
    @ParameterizedTest
    @ValueSource(ints = {18, 21, 32, 95})
    public void validAgeShouldPass(int age) {
        assertEquals(age, Validator.age(age));
    }

    @DisplayName("Проверка невалидных возрастов")
    @ParameterizedTest
    @ValueSource(ints = {-122, -1000, 151, 100500})
    public void invalidAgeShouldThrow(int age) {
        assertThrows(IllegalArgumentException.class, () -> Validator.age(age));
    }


    @DisplayName("Проверка валидных номеров")
    @ParameterizedTest
    @ValueSource(strings = {
            "+79991112244",
            "+11111111111"
    })
    public void validPhoneNumberShouldPass(String phoneNumber) {
        assertEquals(phoneNumber, Validator.phone(phoneNumber));
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
        assertThrows(IllegalArgumentException.class, () -> Validator.phone(phoneNumber));
    }


    @DisplayName("Проверка валидных адресов эл. почты")
    @ParameterizedTest
    @ValueSource(strings = {
            "example@example.com",
            "user.name123@mail.co",
            "my_email+filter@domain.org"
    })
    public void validEmailsShouldPass(String email) {
        assertEquals(email, Validator.email(email));
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
        assertThrows(IllegalArgumentException.class, () -> Validator.email(email));
    }

    @DisplayName("Проверка валидатора validInt")
    @ParameterizedTest
    @ValueSource(strings = {"1", "1000", "14423", "-16384"})
    public void validIntShouldPass(String input) {
        Reader mockReader = Mockito.mock(Reader.class);
        Mockito.when(mockReader.readLine()).thenReturn(input);
        int expected = Integer.parseInt(input);
        int actual = Validator.validInt(" ", mockReader);
        assertEquals(expected, actual);
    }
}