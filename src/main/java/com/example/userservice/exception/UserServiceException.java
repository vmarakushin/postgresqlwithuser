package com.example.userservice.exception;


/**
 * Unchecked-исключение
 * Используется {@link com.example.userservice.service.UserService}
 * Говорит наружу о нарушении бизнес-правил (уникальность email или phone)
 *
 * @author vmarakushin
 * @version 1.0
 */
public class UserServiceException extends RuntimeException {
    public UserServiceException(String message) {
        super(message);
    }
}
