package com.example.userservice.controller;


import com.example.userservice.dto.RequestUserDTO;
import com.example.userservice.dto.UserDTO;
import com.example.userservice.exception.RepositoryException;
import com.example.userservice.exception.UserServiceException;
import com.example.userservice.service.UserServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * Тесты для контроллера
 *
 * @author vmarakushin
 * @version 1.0
 */
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserServiceImpl userService;

    @Autowired
    private ObjectMapper objectMapper;


    @DisplayName("Проверка POST при валидных значениях")
    @Test
    public void testPostShouldReturn201ValidPost() throws Exception {

        UserDTO userDto = new UserDTO(
                0,
                "Ivan",
                "Pivnov",
                47,
                "+71111111111",
                "ivan@pivnov.ru",
                60000,
                new Date());

        doNothing().when(userService).createUser(userDto);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isCreated());
    }

    @DisplayName("Проверка POST при UserServiceException")
    @Test
    public void testPostShouldReturn400UserServiceException() throws Exception {
        UserDTO userDto = new UserDTO(
                0,
                "Ivan",
                "Pivnov",
                47,
                "+71111111111",
                "ivan@pivnov.ru",
                60000,
                new Date());

        String message = "Этот телефон уже используется.";

        doThrow(new UserServiceException(message))
                .when(userService).createUser(userDto);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(message));
    }

    @DisplayName("Проверка POST при IllegalArgumentException")
    @Test
    public void testPostShouldReturn400IllegalArgumentException() throws Exception {
        UserDTO userDto = new UserDTO(
                0,
                "Ivan",
                "Pivnov",
                47,
                "+71111111111",
                "ivan@pivnov.ru",
                60000,
                new Date());

        String message = "ID должен быть равен нулю.";

        doThrow(new IllegalArgumentException(message))
                .when(userService).createUser(userDto);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(message));
    }

    @DisplayName("Проверка POST при RepositoryException")
    @Test
    public void testPostShouldReturn500RepositoryException() throws Exception {
        UserDTO userDto = new UserDTO(
                0,
                "Ivan",
                "Pivnov",
                47,
                "+71111111111",
                "ivan@pivnov.ru",
                60000,
                new Date());

        String message = "Ошибка при обращении к БД";

        doThrow(new RepositoryException(message))
                .when(userService).createUser(userDto);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(message));
    }

    @DisplayName("Проверка GET ALL при успехе")
    @Test
    public void testGetAllShouldReturn200AllUsers() throws Exception {

        UserDTO userDto0 = new UserDTO(
                0,
                "Ivan",
                "Pivnov",
                47,
                "+71111111111",
                "ivan@pivnov.ru",
                60000,
                new Date());

        UserDTO userDto1 = new UserDTO(
                1,
                "Divan",
                "Stolov",
                48,
                "+71111111112",
                "divan@stolov.example",
                12000,
                new Date());

        UserDTO userDto2 = new UserDTO(
                2,
                "Jonh",
                "Doe",
                33,
                "+12345678900",
                "john@doe.com",
                180000,
                new Date());

        List<UserDTO> users = new ArrayList<>();
        users.add(userDto0);
        users.add(userDto1);
        users.add(userDto2);

        String expectedJson = objectMapper.writeValueAsString(users);

        doReturn(users).when(userService).getAllUsers();

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedJson));
    }

    @DisplayName("Проверка GET ALL при RepositoryException")
    @Test
    public void testGetAllShouldReturn500RepositoryException() throws Exception {

        String message = "Ошибка при обращении к БД";

        doThrow(new RepositoryException(message))
                .when(userService).getAllUsers();

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(message));
    }

    @DisplayName("Проверка GET при успехе")
    @Test
    public void testGetByIdShouldReturn200UserById() throws Exception {

        UserDTO userDto = new UserDTO(
                1,
                "Ivan",
                "Pivnov",
                47,
                "+71111111111",
                "ivan@pivnov.ru",
                60000,
                new Date());

        Optional<UserDTO> optional = Optional.of(userDto);

        String expectedJson = objectMapper.writeValueAsString(userDto);

        doReturn(optional).when(userService).getUserById(any(RequestUserDTO.class));

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedJson));
    }

    @DisplayName("Проверка GET пользователь не найден")
    @Test
    public void testGetByIdShouldReturn404UserById() throws Exception {

        doReturn(Optional.empty()).when(userService).getUserById(any(RequestUserDTO.class));
        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isNotFound());
    }

    @DisplayName("Проверка GET ID < 1")
    @Test
    public void testGetByIdShouldReturn400UserById() throws Exception {

        String message = "ID должен быть больше 0";

        doThrow(new IllegalArgumentException(message)).when(userService).getUserById(any(RequestUserDTO.class));

        mockMvc.perform(get("/api/users/0"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(message));
    }

    @DisplayName("Проверка GET при нерабочей БД")
    @Test
    public void testGetByIdShouldReturn500UserById() throws Exception {

        String message = "БД упала позвоните в Астон";
        doThrow(new RepositoryException(message)).when(userService).getUserById(any(RequestUserDTO.class));
        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(message));
    }

    @DisplayName("Проверка PUT при успехе")
    @Test
    public void testPutShouldReturn200UserById() throws Exception {

        UserDTO userDto = new UserDTO(
                1,
                "Ivan",
                "Pivnov",
                47,
                "+71111111111",
                "ivan@pivnov.ru",
                60000,
                new Date());

        doNothing().when(userService).updateUser(any(UserDTO.class));

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk());
    }

    @DisplayName("Проверка PUT при несовпадающих ID")
    @Test
    public void testPutShouldReturn400DifferentIDs() throws Exception {
        UserDTO userDto = new UserDTO(
                1,
                "Ivan",
                "Pivnov",
                47,
                "+71111111111",
                "ivan@pivnov.ru",
                60000,
                new Date());

        mockMvc.perform(put("/api/users/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("ID в пути и теле не совпадают."));
    }

    @DisplayName("Проверка PUT при UserServiceException")
    @Test
    public void testPutShouldReturn400UserServiceException() throws Exception {
        UserDTO userDto = new UserDTO(
                1,
                "Ivan",
                "Pivnov",
                47,
                "+71111111111",
                "ivan@pivnov.ru",
                60000,
                new Date());

        String message = "Этот email уже используется";

        doThrow(new UserServiceException(message)).when(userService).updateUser(any(UserDTO.class));

        mockMvc.perform(put("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(message));
    }

    @DisplayName("Проверка PUT при IllegalArgumentException")
    @Test
    public void testPutShouldReturn400UserIllegalArgumentException() throws Exception {
        UserDTO userDto = new UserDTO(
                1,
                "Ivan",
                "Pivnov",
                387,
                "+71111111111",
                "ivan@pivnov.ru",
                60000,
                new Date());

        String message = "Возраст от 1 до 150";

        doThrow(new IllegalArgumentException(message)).when(userService).updateUser(any(UserDTO.class));

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(message));
    }

    @DisplayName("Проверка PUT при RepositoryException")
    @Test
    public void testPutShouldReturn500UserRepositoryException() throws Exception {
        UserDTO userDto = new UserDTO(
                1,
                "Ivan",
                "Pivnov",
                47,
                "+71111111111",
                "ivan@pivnov.ru",
                60000,
                new Date());

        String message = "БД устала от тестов, потому и упала";

        doThrow(new RepositoryException(message)).when(userService).updateUser(any(UserDTO.class));

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(message));
    }

    @DisplayName("Проверка DELETE при успехе")
    @Test
    public void testDeleteShouldReturn204UserById() throws Exception {

        doNothing().when(userService).deleteUser(any(RequestUserDTO.class));

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNoContent());
    }

    @DisplayName("Проверка DELETE при некорректном ID")
    @Test
    public void testDeleteShouldReturn400IllegalArgumentException() throws Exception {

        String message = "ID должен быть больше 0";

        doThrow(new IllegalArgumentException(message)).when(userService).deleteUser(any(RequestUserDTO.class));

        mockMvc.perform(delete("/api/users/-385"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(message));
    }

    @DisplayName("Проверка DELETE при ошибке репозитория")
    @Test
    public void testDeleteShouldReturn500RepositoryException() throws Exception {

        String message = "БД ушла спать";

        doThrow(new RepositoryException(message)).when(userService).deleteUser(any(RequestUserDTO.class));

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(message));

    }
}
