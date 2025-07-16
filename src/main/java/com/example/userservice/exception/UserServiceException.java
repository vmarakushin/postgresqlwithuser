package com.example.userservice.exception;


import com.example.userservice.service.UserServiceImpl;

/**
 * Unchecked-исключение
 * Используется {@link UserServiceImpl}
 * Говорит наружу о сбое работы Kafka
 *
 * @author vmarakushin
 * @version 1.0
 */
public class UserServiceException extends RuntimeException {
    public UserServiceException(String message) {
        super(message);
    }
}
