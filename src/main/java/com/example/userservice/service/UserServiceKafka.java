package com.example.userservice.service;

import com.example.userservice.dto.RequestUserDTO;
import com.example.userservice.dto.UserDTO;
import com.example.userservice.exception.UserServiceException;
import com.example.userservice.kafka.UserEventProducer;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Декоратор {@link UserServiceImpl} с Кафкой
 * Функционал дополнен только для методов create() и delete()
 *
 * @author vmarakushin
 * @version 1.0
 */
@Service
public class UserServiceKafka implements UserService {


    private final UserServiceImpl userService;

    private final Logger logger;

    private final UserEventProducer eventProducer;


    public UserServiceKafka(UserEventProducer eventProducer, UserServiceImpl userService) {
        this.eventProducer = eventProducer;
        this.userService = userService;
        this.logger = LoggerFactory.getLogger(UserServiceKafka.class);
    }


    public void createUser(UserDTO dto) {
        userService.createUser(dto);
        try {
            eventProducer.sendUserEvent("CREATE", dto.getEmail());
        }catch (Exception e){
            logger.warn("Не удалось отправить событие в Kafka: ", e);
            throw new UserServiceException("Не удалось отправить сообщение в Kafka");
        }
    }

    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    public Optional<UserDTO> getUserById(RequestUserDTO dto) {
        return userService.getUserById(dto);
    }

    public void updateUser(UserDTO dto) {
        userService.updateUser(dto);
    }

    public void deleteUser(RequestUserDTO dto) {
        Optional<UserDTO> userOp = userService.getUserById(dto);
        if (userOp.isPresent()) {
            UserDTO user = userOp.get();
            userService.deleteUser(dto);
            try {
                eventProducer.sendUserEvent("DELETE", user.getEmail());
            }catch (Exception e){
                logger.warn("Не удалось отправить событие в Kafka: ", e);
                throw new UserServiceException("Не удалось отправить сообщение в Kafka");
            }
        }
    }
}
