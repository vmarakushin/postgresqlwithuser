package com.example.userservice.service;

import com.example.userservice.dto.RequestUserDTO;
import com.example.userservice.dto.UserDTO;
import com.example.userservice.exception.UserServiceException;
import com.example.userservice.kafka.UserEventProducer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


/**
 * Тесты для декоратора с кафкой
 *
 * @author vmarakushin
 * @version 1.0
 */
@ExtendWith(MockitoExtension.class)
public class UserServiceKafkaTest {

    @Mock
    private UserServiceImpl userServiceImpl;

    @Mock
    private UserEventProducer userEventProducer;

    @Mock
    private Logger logger;

    @InjectMocks
    private UserServiceKafka userServiceKafka;

    @Test
    @DisplayName("Проверка успешного создания юзера")
    public void testSuccessCreateUser() {

        UserDTO userDTO = new UserDTO(
                0L,
                "Vasya",
                "Petrov",
                32,
                "+79991112233",
                "vasya@petrov.com",
                386L,
                new Date()
        );

        doNothing().when(userServiceImpl).createUser(userDTO);
        userServiceKafka.createUser(userDTO);
        verify(userEventProducer).sendUserEvent("CREATE", userDTO.getEmail());
    }

    @Test
    @DisplayName("Проверка создания, исключение в UserEventProducer")
    public void testCreateUserException(){

        UserDTO dto = new UserDTO(
                0L,
                "Vasya",
                "Petrov",
                32,
                "+79991112233",
                "vasya@petrov.com",
                386L,
                new Date()
        );

        doNothing().when(userServiceImpl).createUser(dto);
        doThrow(new RuntimeException()).when(userEventProducer).sendUserEvent("CREATE", dto.getEmail());

        assertThrows(UserServiceException.class, () -> userServiceKafka.createUser(dto));
    }


    @Test
    @DisplayName("Проверка успешного удаления юзера")
    public void testSuccessDeleteUser() {

        UserDTO userDTO = new UserDTO(
                1L,
                "Vasya",
                "Petrov",
                32,
                "+79991112233",
                "vasya@petrov.com",
                386L,
                new Date()
        );

        RequestUserDTO requestUserDTO = new RequestUserDTO(1);
        Optional<UserDTO> userDtoOp = Optional.of(userDTO);

        doReturn(userDtoOp).when(userServiceImpl).getUserById(requestUserDTO);
        doNothing().when(userServiceImpl).deleteUser(requestUserDTO);

        userServiceKafka.deleteUser(requestUserDTO);
        verify(userEventProducer).sendUserEvent("DELETE", userDTO.getEmail());
    }

    @Test
    @DisplayName("Проверка удаления юзера, юзер не найден")
    public void testDeleteUserNotFound() {

        RequestUserDTO requestUserDTO = new RequestUserDTO(1);
        Optional<UserDTO> userDtoOp = Optional.empty();

        doReturn(userDtoOp).when(userServiceImpl).getUserById(requestUserDTO);

        userServiceKafka.deleteUser(requestUserDTO);
        verifyNoInteractions(userEventProducer);
    }

    @Test
    @DisplayName("Проверка удаления, исключение в UserEventProducer")
    public void testDeleteUserException(){

        UserDTO dto = new UserDTO(
                1L,
                "Vasya",
                "Petrov",
                32,
                "+79991112233",
                "vasya@petrov.com",
                386L,
                new Date()
        );

        Optional<UserDTO> userDtoOp = Optional.of(dto);
        RequestUserDTO requestUserDTO = new RequestUserDTO(dto.getId());

        doReturn(userDtoOp).when(userServiceImpl).getUserById(requestUserDTO);
        doNothing().when(userServiceImpl).deleteUser(requestUserDTO);
        doThrow(new RuntimeException()).when(userEventProducer).sendUserEvent("DELETE", dto.getEmail());

        assertThrows(UserServiceException.class, () -> userServiceKafka.createUser(dto));
    }

}
