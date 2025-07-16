package com.example.userservice.service;

import com.example.userservice.dto.RequestUserDTO;
import com.example.userservice.dto.UserDTO;
import com.example.userservice.exception.RepositoryException;
import com.example.userservice.exception.UserServiceException;
import com.example.userservice.mapper.UserMapper;
import com.example.userservice.model.User;
import com.example.userservice.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;


import java.util.Date;



import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


/**
 * Тесты Service-слоя
 * Стоило только убрать нарушение SRP, так сразу все тестируется легко и просто
 * Можно либо пройти оттестированный валидатор, либо не пройти
 *
 * @author vmarakushin
 * @version 2.0
 */
@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private Validator validator;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;


    @DisplayName("Тест создания валидного юзера")
    @Test
    public void testCreateValidUser() {

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

        User mappedUser = User.builder()
                .id(0L)
                .name("Vasya")
                .surname("Petrov")
                .age(32)
                .phone("+79991112233")
                .email("vasya@petrov.com")
                .money(386L)
                .build();


        doNothing().when(validator).fullValidation(userDTO, Validator.Scope.CREATE);

        doReturn(mappedUser).when(userMapper).toEntity(userDTO);

        userService.createUser(userDTO);

        verify(userRepository).save(mappedUser);
    }


    @DisplayName("Тест создания юзера, не прошедшего валидатор")
    @Test
    public void testCreateInvalidUser() {

        UserDTO userDTO = new UserDTO(
                0L,
                "Vasya",
                "6PyTAJLbHblu",
                32,
                "+79991112233",
                "vasya@petrov.com",
                386L,
                new Date()
        );

        doThrow(IllegalArgumentException.class).when(validator).fullValidation(userDTO, Validator.Scope.CREATE);

        assertThrows(IllegalArgumentException.class, () -> userService.createUser(userDTO));
    }


    @DisplayName("Тест получения юзера при валидном ID")
    @ParameterizedTest
    @ValueSource(ints = {1, 100, 2500})
    public void testValidGetUserById(int id) {
        RequestUserDTO userDTO = new RequestUserDTO(id);
        doReturn(userDTO.getId()).when(validator).id(userDTO.getId());
        userService.getUserById(userDTO);
        verify(userRepository).findById(userDTO.getId());
    }

    @DisplayName("Тест получения юзера при невалидном ID")
    @ParameterizedTest
    @ValueSource(ints = {0, -1, -100, -12345})
    public void testInvalidGetUserById(int id) {
        RequestUserDTO userDTO = new RequestUserDTO(id);
        doThrow(IllegalArgumentException.class).when(validator).id(userDTO.getId());
        assertThrows(IllegalArgumentException.class, () -> userService.getUserById(userDTO));
    }


    @DisplayName("Тест получения всех юзеров")
    @Test
    public void testGetAllUsers() {
        userService.getAllUsers();
        verify(userRepository).findAll();
    }

    @DisplayName("Тест обновления пользователя при валидном ID")
    @ParameterizedTest
    @ValueSource(ints = {1, 25, 265, 165356})
    public void testValidUpdateUser(int id) {
        UserDTO userDTO = new UserDTO(
                id,
                "Vasya",
                "Petrov",
                32,
                "+79991112233",
                "vasya@petrov.com",
                386L,
                new Date()
        );

        User userEntity = User.builder()
                .id(id)
                .name("Vasya")
                .surname("Petrov")
                .age(32)
                .phone("+79991112233")
                .email("vasya@petrov.com")
                .money(386L)
                .build();


        doNothing().when(validator).fullValidation(userDTO, Validator.Scope.UPDATE);
        when(userMapper.toEntity(userDTO)).thenReturn(userEntity);
        userService.updateUser(userDTO);
        verify(userRepository).save(userEntity);
        verify(userRepository).flush();
    }

    @DisplayName("Тест обновления пользователя при невалидном ID")
    @ParameterizedTest
    @ValueSource(ints = {0, -1, -200, -4142152})
    public void testInvalidUpdateUser(int id) {
        UserDTO userDTO = new UserDTO(
                id,
                "Vasya",
                "Petrov",
                32,
                "+79991112233",
                "vasya@petrov.com",
                386L,
                new Date()
        );
        doThrow(IllegalArgumentException.class).when(validator).fullValidation(userDTO, Validator.Scope.UPDATE);
        assertThrows(IllegalArgumentException.class, () -> userService.updateUser(userDTO));
    }

    @DisplayName("Тест удаления пользователя при валидном ID")
    @ParameterizedTest
    @ValueSource(ints = {1, 25, 265, 165356})
    public void testValidDeleteUser(int id) {
        RequestUserDTO userDTO = new RequestUserDTO(id);
        doReturn(userDTO.getId()).when(validator).id(userDTO.getId());
        userService.deleteUser(userDTO);
        verify(userRepository).deleteById(userDTO.getId());
        verify(userRepository).flush();
    }

    @DisplayName("Тест удаления пользователя при невалидном ID")
    @ParameterizedTest
    @ValueSource(ints = {0, -1, -200, -4142152})
    public void testInvalidDeleteUser(int id) {
        RequestUserDTO userDTO = new RequestUserDTO(id);
        doThrow(IllegalArgumentException.class).when(validator).id(userDTO.getId());
        assertThrows(IllegalArgumentException.class, () -> userService.deleteUser(userDTO));
    }

    @DisplayName("Тест создание исключение репо")
    @Test
    public void testCreateUserRepoException() {

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

        User mappedUser = User.builder()
                .id(0L)
                .name("Vasya")
                .surname("Petrov")
                .age(32)
                .phone("+79991112233")
                .email("vasya@petrov.com")
                .money(386L)
                .build();


        doNothing().when(validator).fullValidation(userDTO, Validator.Scope.CREATE);

        doReturn(mappedUser).when(userMapper).toEntity(userDTO);

        userService.createUser(userDTO);

        doThrow(RuntimeException.class).when(userRepository).save(mappedUser);
        assertThrows(RepositoryException.class, () -> userService.createUser(userDTO));
    }


    @DisplayName("Тест получения юзера исключение репо")
    @Test
    public void testGetUserByIdRepoException() {
        RequestUserDTO userDTO = new RequestUserDTO(1);
        doReturn(userDTO.getId()).when(validator).id(userDTO.getId());
        doThrow(RuntimeException.class).when(userRepository).findById(userDTO.getId());
        assertThrows(RepositoryException.class, () -> userService.getUserById(userDTO));
    }

    @DisplayName("Тест получения всех юзеров исключение репо")
    @Test
    public void testGetAllUsersRepoException() {
        doThrow(RuntimeException.class).when(userRepository).findAll();
        assertThrows(RepositoryException.class, () -> userService.getAllUsers());
    }

    @DisplayName("Тест обновления исключение репо")
    @Test
    public void testUpdateUserRepoException() {
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

        User mappedUser = User.builder()
                .id(0L)
                .name("Vasya")
                .surname("Petrov")
                .age(32)
                .phone("+79991112233")
                .email("vasya@petrov.com")
                .money(386L)
                .build();

        doNothing().when(validator).fullValidation(userDTO, Validator.Scope.UPDATE);
        doReturn(mappedUser).when(userMapper).toEntity(userDTO);

        doThrow(RuntimeException.class).when(userRepository).save(mappedUser);
        assertThrows(RepositoryException.class, () -> userService.updateUser(userDTO));
    }

    @DisplayName("Тест удаления исключение репо")
    @Test
    public void testDeleteUserRepoException() {
        RequestUserDTO userDTO = new RequestUserDTO(1);
        doReturn(userDTO.getId()).when(validator).id(userDTO.getId());
        doThrow(RuntimeException.class).when(userRepository).deleteById(userDTO.getId());
        assertThrows(RepositoryException.class, () -> userService.deleteUser(userDTO));
    }

}