package com.example.userservice.app;


/**
 * Функциональный интерфейс для лямбд в методах обновления и создания юзверя
 *
 * @param <T> функция валидатора
 * @param <R> результат
 */
@FunctionalInterface
public interface ThrowingFunction<T, R> {
    R apply(T t) throws Exception;
}