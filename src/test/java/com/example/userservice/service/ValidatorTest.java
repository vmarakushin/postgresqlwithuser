package com.example.userservice.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;



import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты для валидатора
 * @author vmarakushin
 * @version 1.2
 */
public class ValidatorTest {

    Validator validator = new Validator();

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
    @ValueSource(strings = {"-1", "-100","ID"})
    public void invalidIdShouldThrow(String input) {
        assertThrows(IllegalArgumentException.class, () -> validator.validIdCreate(input,validator::id));
    }
}