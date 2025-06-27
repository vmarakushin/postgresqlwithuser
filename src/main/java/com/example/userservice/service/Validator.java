package com.example.userservice.service;

import com.example.userservice.app.ThrowingFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Scanner;


/**
 * Утилитный класс для валидации всего на свете
 *
 * @author vmarakushin
 * @version 1.1
 */
public class Validator {

    private static final Logger logger = LoggerFactory.getLogger(Validator.class);
    private static final Scanner scanner = new Scanner(System.in);
    private static final Reader reader = new Reader(new Scanner(System.in));


    private Validator() {
    }

    /**
     * Считывает int
     * Отсекает неправильный тип данных
     *
     * @param message сообщение для пользователя при вводе(опционально)
     * @return валидированное целочисленное значение
     */
    public static int validInt(String message, Reader reader) {
        while (true) {
            if (!message.isBlank()) System.out.println(message);
            try {
                int x = Integer.parseInt(reader.readLine());
                return x;
            } catch (NumberFormatException e) {
                System.out.println("Введите целое число!");
                logger.warn("Ошибка валидации ввода: {}", e.getMessage());
            }
        }
    }
    public static int validInt() {
        return validInt("", reader);
    }

    /**
     * Считывающий адаптер для валидатора
     * при создании пользователя
     * @param message сообщение в консоль при вводе
     * @param parser  ссылка на String метод-валидатор
     * @return валидированная строка
     */
    public static String validStringCreate(String message, ThrowingFunction<String> parser) {
        while (true) {
            System.out.print(message);
            String input = scanner.nextLine();
            try {
                return parser.apply(input);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                logger.warn("Ошибка валидации ввода: {}", e.getMessage());
            }
        }
    }

    /**
     * Валидатор данных для создания пользователя для возраста
     *
     * @param message сообщение в сосноль при вводе
     * @param parser  ссылка на Int метод-валидатор
     * @return валидированный int
     */
    public static int validIntCreate(String message, ThrowingFunction<Integer> parser) {
        while (true) {
            System.out.print(message);
            int number = validInt();
            try {
                return parser.apply(number);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                logger.warn("Ошибка валидации ввода: {}", e.getMessage());
            }
        }
    }

    /**
     * Валидатор данных для обновления пользователя
     *
     * @param message сообщение в сосноль при вводе
     * @param parser  ссылка на валидатор
     * @return валидированная строка
     */
    public static String validStringUpdate(String message, ThrowingFunction<String> parser, String oldOne) {
        while (true) {
            System.out.print(message);
            String input = scanner.nextLine();
            if (input.isBlank()) return oldOne;
            try {
                return parser.apply(input);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                logger.warn("Ошибка валидации ввода: {}", e.getMessage());
            }
        }
    }

    /**
     * Валидатор данных для обновления пользователя для возраста
     *
     * @param message сообщение в сосноль при вводе
     * @param parser  ссылка на валидатор
     * @return валидированный возраст
     */
    public static int validIntUpdate(String message, ThrowingFunction<Integer> parser, int oldAge) {
        while (true) {
            System.out.print(message);
            int number = validInt();
            if (number == 0) return oldAge;
            try {
                return parser.apply(number);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                logger.warn("Ошибка валидации ввода: {}", e.getMessage());
            }
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


