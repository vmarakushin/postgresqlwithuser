package com.example.userservice.service;

import com.example.userservice.app.ThrowingFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


/**
 * Класс для валидации всего на свете
 *
 * @author vmarakushin
 * @version 2.0
 */
@Service
public class Validator {

    private final Logger logger = LoggerFactory.getLogger(Validator.class);
    private final String validatorMessage = "Ошибка валидации: ";

    public Validator() {
    }

    /**
     * Валидирует int
     * Отсекает неправильный тип данных
     *
     * @return валидированное int значение
     */
    public int validInt(String input) {
        try {
            return Integer.parseInt(input);
        } catch (IllegalArgumentException e) {
            logger.warn(validatorMessage + e.getMessage());
            throw new IllegalArgumentException("Необходимо целое число.");
        }
    }

    /**
     * Валидирует long
     * Отсекает неправильный тип данных
     *
     * @return валидированное long значение
     */
    public long validLong(String input) {
        try {
            return Long.parseLong(input);
        } catch (IllegalArgumentException e) {
            logger.warn(validatorMessage + e.getMessage());
            throw new IllegalArgumentException("Необходимо целое число.");
        }
    }


    /**
     * Адаптер для валидации строк
     *
     * @param input  строка для валидации
     * @param parser ссылка на String метод-валидатор
     * @return валидированная строка
     */
    public String validStringCreate(String input, ThrowingFunction<String, String> parser) {
        try {
            return parser.apply(input);
        } catch (IllegalArgumentException e) {
            logger.warn(validatorMessage + e.getMessage());
            throw e;
        }
    }

    /**
     * Валидатор данных для создания пользователя для возраста
     *
     * @param input  - String для валидации
     * @param parser ссылка на Int метод-валидатор
     * @return валидированный int
     */
    public int validIntCreate(String input, ThrowingFunction<Integer, Integer> parser) {
        try {
            int number = validInt(input);
            return parser.apply(number);
        } catch (IllegalArgumentException e) {
            logger.warn(validatorMessage + e.getMessage());
            throw e;
        }
    }

    /**
     * Валидатор данных для создания пользователя для возраста
     *
     * @param input  - String для валидации
     * @param parser ссылка на long метод-валидатор
     * @return валидированный long
     */
    public long validIdCreate(String input, ThrowingFunction<Long, Long> parser) {
        try {
            long number = validLong(input);
            return parser.apply(number);
        } catch (RuntimeException e) {
            logger.warn(validatorMessage + e.getMessage());
            throw e;
        }
    }


    /**
     * Валидатор для имени
     *
     * @param name строка для валидации
     * @return валидированное имя
     */
    public String name(String name) {
        if (!name.matches("[a-zA-Zа-яА-ЯёЁ\\-\\s]+$"))
            throw new IllegalArgumentException("Имя может содержать только буквы, дефис и пробел");
        return name;
    }

    /**
     * Валидатор для фамилии
     *
     * @param surname строка для валидации
     * @return валидированная фамилия
     */
    public String surname(String surname) {
        if (!surname.matches("[a-zA-Zа-яА-ЯёЁ\\-\\s]+$"))
            throw new IllegalArgumentException("Фамилия может содержать только буквы, дефис и пробел");
        return surname;
    }

    /**
     * Валидатор для возраста
     *
     * @param age число для валидации
     * @return валидированный возраст
     */
    public int age(int age) {
        if (age < 1 || age > 150)
            throw new IllegalArgumentException("Возраст от 1 до 150");
        return age;
    }

    /**
     * Валидатор для номера телефона
     *
     * @param phone строка для валидации
     * @return валидированный телефон
     */
    public String phone(String phone) {
        if (!phone.matches("^\\+\\d{11}$"))
            throw new IllegalArgumentException("Телефон должен соответствовать формату , например +79992225566");
        return phone;
    }

    /**
     * Валидатор емейла
     *
     * @param email строка для валидации
     * @return валидированный емейл
     */
    public String email(String email) {
        if (!email.matches("^[a-zA-Z0-9._%+-]{3,}@[a-zA-Z0-9.-]{3,}\\.[a-zA-Z]{2,}$"))
            throw new IllegalArgumentException("Email должен соответствовать формату , например example@example.com");
        return email;
    }

    /**
     * Валидатор айди
     *
     * @param id long для валидации
     * @return валидированный айди
     */
    public long id(long id) {
        if (id < 1L)
            throw new IllegalArgumentException("ID не может быть меньше 1");
        return id;
    }
}


