package com.example.userservice.app;

/**
 * Функциональный интерфейс - контракт для использования валидаторов
 *
 * @param <T> обобщенный тип данных на вход
 * @param <R> обобщенный тип данных на выход
 * @author vmarakushin
 * @version 1.1
 */


@FunctionalInterface
public interface ThrowingFunction<T, R> {
    R apply(T t) throws RuntimeException;
}