package com.example.userservice.exception;


import com.example.userservice.service.UserServiceImpl;

/**
 * Unchecked-исключение
 * Используется {@link UserServiceImpl}
 * Говорит наружу абстрактно об ошибке работы с БД
 *
 * @author vmarakushin
 * @version 1.0
 */
public class RepositoryException extends RuntimeException {
    public RepositoryException(String message) {
        super(message);
    }
}
