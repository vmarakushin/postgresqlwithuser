package com.example.userservice.app;


/**
 * Функциональный интерфейс для парсирования при считывании
 *
 *
 * @param <T> результат
 * @version 1.0
 * @author vmarakushin
 */
@FunctionalInterface
public interface InputParser<T> {
    T parse(String input) throws Exception;
}