package com.example.userservice.app;

/**
 * Функциональный интерфейс для лямбд в методах обновления и создания юзверя
 *
 * @param <T> обобщенный тип данных
 */
@FunctionalInterface
public interface ThrowingFunction<T> {
    T apply(T t) throws Exception;
}