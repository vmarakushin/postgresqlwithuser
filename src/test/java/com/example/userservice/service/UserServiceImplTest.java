package com.example.userservice.service;

import com.example.userservice.dto.RequestUserDTO;
import com.example.userservice.dto.UserDTO;
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
 * Не писал тесты для метода validation, так как
 * он оттестирован в составе остальных методов,
 * а мокать методы тестируемого класса - антипаттерн
 *
 * @author vmarakushin
 * @version 1.0
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

        when(userMapper.toEntity(userDTO)).thenReturn(mappedUser);
        when(validator.name("Vasya")).thenReturn("Vasya");
        when(validator.surname("Petrov")).thenReturn("Petrov");
        when(validator.age(32)).thenReturn(32);
        when(validator.phone("+79991112233")).thenReturn("+79991112233");
        when(validator.email("vasya@petrov.com")).thenReturn("vasya@petrov.com");


        userService.createUser(userDTO);

        verify(userRepository).save(mappedUser);
    }


    @DisplayName("Тест создания невалидного юзера")
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

        when(validator.name("Vasya")).thenReturn("Vasya");
        when(validator.surname("6PyTAJLbHblu")).thenThrow(new IllegalArgumentException("Нифига ты не брутальный"));

        assertThrows(IllegalArgumentException.class, () -> userService.createUser(userDTO));
    }

    @DisplayName("Тест создания неуникального юзера")
    @Test
    public void testCreateNonUniqueUser() {
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


        when(validator.name("Vasya")).thenReturn("Vasya");
        when(validator.surname("Petrov")).thenReturn("Petrov");
        when(validator.age(32)).thenReturn(32);
        when(validator.phone("+79991112233")).thenReturn("+79991112233");
        when(validator.email("vasya@petrov.com")).thenReturn("vasya@petrov.com");
        when(userRepository.existsByEmailAndIdNot("vasya@petrov.com", 0L)).thenReturn(true);


        assertThrows(UserServiceException.class, () -> userService.createUser(userDTO));
    }


    @DisplayName("Тест создания из ДТО с ID != 0")
    @Test
    public void testCreateUserFromNonNullId() {
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

        assertThrows(IllegalArgumentException.class, () -> userService.createUser(userDTO));
    }

    @DisplayName("Тест получения юзера при валидном ID")
    @ParameterizedTest
    @ValueSource(ints = {1, 100, 2500})
    public void testValidGetUserById(int id) {
        RequestUserDTO userDTO = new RequestUserDTO(id);
        userService.getUserById(userDTO);
        verify(userRepository).findById(userDTO.getId());
    }

    @DisplayName("Тест получения юзера при невалидном ID")
    @ParameterizedTest
    @ValueSource(ints = {0, -1, -100, -12345})
    public void testInvalidGetUserById(int id) {
        RequestUserDTO userDTO = new RequestUserDTO(id);
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

        when(userMapper.toEntity(userDTO)).thenReturn(userEntity);
        userService.updateUser(userDTO);
        verify(userRepository).save(userEntity);
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
        assertThrows(IllegalArgumentException.class, () -> userService.updateUser(userDTO));
    }

    @DisplayName("Тест удаления пользователя при валидном ID")
    @ParameterizedTest
    @ValueSource(ints = {1, 25, 265, 165356})
    public void testValidDeleteUser(int id) {
        RequestUserDTO userDTO = new RequestUserDTO(id);
        userService.deleteUser(userDTO);
        verify(userRepository).deleteById(userDTO.getId());
    }

    @DisplayName("Тест удаления пользователя при невалидном ID")
    @ParameterizedTest
    @ValueSource(ints = {0, -1, -200, -4142152})
    public void testInvalidDeleteUser(int id) {
        RequestUserDTO userDTO = new RequestUserDTO(id);
        assertThrows(IllegalArgumentException.class, () -> userService.deleteUser(userDTO));
    }
}