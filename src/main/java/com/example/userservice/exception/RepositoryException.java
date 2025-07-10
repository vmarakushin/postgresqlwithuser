package com.example.userservice.exception;


/**
 * Unchecked-исключение
 * Используется {@link com.example.userservice.service.UserService}
 * Говорит наружу абстрактно об ошибке работы с бд
 *
 * @author vmarakushin
 * @version 1.0
 */
public class RepositoryException extends RuntimeException {
    public RepositoryException(String message) {
        super(message);
    }
}
