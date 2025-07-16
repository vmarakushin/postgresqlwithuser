package com.example.userservice.service;

import com.example.userservice.app.ThrowingFunction;
import com.example.userservice.dto.UserDTO;
import com.example.userservice.exception.UserServiceException;
import com.example.userservice.repository.UserRepository;
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

    UserRepository userRepository;

    private final Logger logger = LoggerFactory.getLogger(Validator.class);

    private static final String EXCEPTION_VALIDATOR_MESSAGE_PREFIX = "Ошибка валидации: ";
    private static final String EXCEPTION_MESSAGE_NUMBER_PARSER = "Необходимо целое число.";
    private static final String EXCEPTION_MESSAGE_NAME = "Имя может содержать только буквы, дефис и пробел";
    private static final String EXCEPTION_MESSAGE_SURNAME = "Фамилия может содержать только буквы, дефис и пробел";
    private static final String EXCEPTION_MESSAGE_AGE = "Возраст от 1 до 150";
    private static final String EXCEPTION_MESSAGE_PHONE = "Телефон должен соответствовать формату , например +79992225566";
    private static final String EXCEPTION_MESSAGE_EMAIL = "Email должен соответствовать формату , например example@example.com";
    private static final String EXCEPTION_MESSAGE_ID_SHOULD_BE_POSITIVE = "ID не может быть меньше 1";
    private static final String EXCEPTION_MESSAGE_ID_SHOULD_BE_0 = "Id должен быть равен 0";
    private static final String EXCEPTION_MESSAGE_NOT_UNIQUE_EMAIL = "Этот email уже используется!";
    private static final String EXCEPTION_MESSAGE_NOT_UNIQUE_PHONE = "Этот телефон уже используется!";
    private static final String REGEX_FOR_NAME_SURNAME = "^[a-zA-Zа-яА-ЯёЁ\\-\\s]+$";
    private static final String REGEX_FOR_PHONE = "^\\+\\d{11}$";
    private static final String REGEX_FOR_EMAIL = "^[a-zA-Z0-9._%+-]{3,}@[a-zA-Z0-9.-]{3,}\\.[a-zA-Z]{2,}$";

    public Validator(UserRepository userRepository) {
        this.userRepository = userRepository;
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
            logger.warn(EXCEPTION_VALIDATOR_MESSAGE_PREFIX
                    + EXCEPTION_MESSAGE_NUMBER_PARSER);
            throw new IllegalArgumentException(EXCEPTION_MESSAGE_NUMBER_PARSER);
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
            logger.warn(EXCEPTION_VALIDATOR_MESSAGE_PREFIX
                    + EXCEPTION_MESSAGE_NUMBER_PARSER);
            throw new IllegalArgumentException(EXCEPTION_MESSAGE_NUMBER_PARSER);
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
            logger.warn(EXCEPTION_VALIDATOR_MESSAGE_PREFIX + e.getMessage());
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
            logger.warn(EXCEPTION_VALIDATOR_MESSAGE_PREFIX + e.getMessage());
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
            logger.warn(EXCEPTION_VALIDATOR_MESSAGE_PREFIX + e.getMessage());
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
        if (!name.matches(REGEX_FOR_NAME_SURNAME))
            throw new IllegalArgumentException(EXCEPTION_MESSAGE_NAME);
        return name;
    }

    /**
     * Валидатор для фамилии
     *
     * @param surname строка для валидации
     * @return валидированная фамилия
     */
    public String surname(String surname) {
        if (!surname.matches(REGEX_FOR_NAME_SURNAME))
            throw new IllegalArgumentException(EXCEPTION_MESSAGE_SURNAME);
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
            throw new IllegalArgumentException(EXCEPTION_MESSAGE_AGE);
        return age;
    }

    /**
     * Валидатор для номера телефона
     *
     * @param phone строка для валидации
     * @return валидированный телефон
     */
    public String phone(String phone) {
        if (!phone.matches(REGEX_FOR_PHONE))
            throw new IllegalArgumentException(EXCEPTION_MESSAGE_PHONE);
        return phone;
    }

    /**
     * Валидатор емейла
     *
     * @param email строка для валидации
     * @return валидированный емейл
     */
    public String email(String email) {
        if (!email.matches(REGEX_FOR_EMAIL))
            throw new IllegalArgumentException(EXCEPTION_MESSAGE_EMAIL);
        return email;
    }

    /**
     * Валидатор айди
     *
     * @param id long для валидации
     * @return валидированный айди
     */
    public long id(long id) {
        if (id < 1L) {
            logger.warn(EXCEPTION_VALIDATOR_MESSAGE_PREFIX
                    + EXCEPTION_MESSAGE_ID_SHOULD_BE_POSITIVE);
            throw new IllegalArgumentException(EXCEPTION_MESSAGE_ID_SHOULD_BE_POSITIVE);
        }
        return id;
    }

    public enum Scope {
        CREATE, UPDATE
    }

    /**
     * Валидация данных
     * Scope.CREATE - проверка ID == 0 при создании
     * Scope.UPDATE - проверка ID > 0 при обновлении
     * Проверка полей общая для обоих случаев
     *
     * @param dto   UserDTO с данными
     * @param scope применение метода
     */
    public void fullValidation(UserDTO dto, Scope scope) {

        switch (scope) {
            case CREATE -> {
                if (dto.getId() != 0L) {
                    logger.warn(EXCEPTION_VALIDATOR_MESSAGE_PREFIX
                            + EXCEPTION_MESSAGE_ID_SHOULD_BE_0);
                    throw new IllegalArgumentException(EXCEPTION_MESSAGE_ID_SHOULD_BE_0);
                }
            }
            case UPDATE -> id(dto.getId());
        }


        validStringCreate(dto.getName(), this::name);
        validStringCreate(dto.getSurname(), this::surname);
        validIntCreate(String.valueOf(dto.getAge()), this::age);
        validStringCreate(dto.getPhone(), this::phone);
        validStringCreate(dto.getEmail(), this::email);

        if (userRepository.existsByEmailAndIdNot(dto.getEmail(), dto.getId())) {
            logger.warn(EXCEPTION_VALIDATOR_MESSAGE_PREFIX
                    + EXCEPTION_MESSAGE_NOT_UNIQUE_EMAIL);
            throw new IllegalArgumentException(EXCEPTION_MESSAGE_NOT_UNIQUE_EMAIL);
        }
        if (userRepository.existsByPhoneAndIdNot(dto.getPhone(), dto.getId())) {
            logger.warn(EXCEPTION_VALIDATOR_MESSAGE_PREFIX
                    + EXCEPTION_MESSAGE_NOT_UNIQUE_PHONE);
            throw new IllegalArgumentException(EXCEPTION_MESSAGE_NOT_UNIQUE_PHONE);
        }
    }
}


