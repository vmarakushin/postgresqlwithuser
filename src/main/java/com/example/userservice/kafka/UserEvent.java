package com.example.userservice.kafka;

/**
 * Событие-сообщение для Kafka
 *
 * @param operation CREATE или DELETE для соответствующего сообщения
 * @param email адрес жертвы
 * @author vmarakushin
 * @version 1.0
 */
public record UserEvent(String operation, String email) {}