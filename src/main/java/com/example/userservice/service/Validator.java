package com.example.userservice.service;

import com.example.userservice.app.ThrowingFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Утилитный класс для валидации всего на свете
 *
 * @author vmarakushin
 * @version 2.0
 */
public class Validator {

    private static final Logger logger = LoggerFactory.getLogger(Validator.class);
    private static final String validatorMessage = "Ошибка валидации ввода: ";

    private Validator() {
    }

    /**
     * Валидирует int
     * Отсекает неправильный тип данных
     *
     * @return валидированное целочисленное значение
     */
    public static int validInt(String input) {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            logger.warn(validatorMessage + e.getMessage());
            throw new IllegalArgumentException("Введите целое число!");
        }
    }


    /**
     * Адаптер для валидации строк
     *
     * @param input  строка для валидации
     * @param parser ссылка на String метод-валидатор
     * @return валидированная строка
     */
    public static String validStringCreate(String input, ThrowingFunction<String> parser) throws Exception {
        try {
            return parser.apply(input);
        } catch (Exception e) {
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
    public static int validIntCreate(String input, ThrowingFunction<Integer> parser) throws Exception {
        try {
            int number = validInt(input);
            return parser.apply(number);
        } catch (Exception e) {
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
    public static String name(String name) {
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
    public static String surname(String surname) {
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
    public static int age(int age) {
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
    public static String phone(String phone) {
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
    public static String email(String email) {
        if (!email.matches("^[a-zA-Z0-9._%+-]{3,}@[a-zA-Z0-9.-]{3,}\\.[a-zA-Z]{2,}$"))
            throw new IllegalArgumentException("Email должен соответствовать формату , например example@example.com");
        return email;
    }

}


