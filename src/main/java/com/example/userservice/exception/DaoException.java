package com.example.userservice.exception;


/**
 * Кастомное исключение
 * @author vmarakushin
 * @version 1.0
 */
public class DaoException extends RuntimeException {
    public DaoException(String message) {
        super(message);
    }
    public DaoException() {
        super();
    }
}
