package com.example.userservice.kafka;


import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;


/**
 * Издатель сообщений Kafka
 * Публикует {@link UserEvent}
 *
 * @author vmarakushin
 * @version 1.0
 */
@Component
public class UserEventProducer {
    private static final String TOPIC = "user-events";

    private final KafkaTemplate<String, UserEvent> kafkaTemplate;

    public UserEventProducer(KafkaTemplate<String, UserEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendUserEvent(String operation, String email) {
        UserEvent event = new UserEvent(operation, email);
        kafkaTemplate.send(TOPIC, event);
    }
}