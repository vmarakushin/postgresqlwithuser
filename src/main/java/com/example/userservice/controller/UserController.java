package com.example.userservice.controller;

import com.example.userservice.dto.RequestUserDTO;
import com.example.userservice.dto.UserDTO;
import com.example.userservice.exception.RepositoryException;
import com.example.userservice.exception.UserServiceException;
import com.example.userservice.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * Контроллер для REST взаимодействия с UserService
 *
 * @author vmarakushin
 * @version 1.0
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    /**
     * Метод создания пользователя
     *
     * @param dto UserDTO с данными
     * @return 201 - при успехе
     * 400 - в случае неправильных данных
     * 500 - в случае ошибки обращения к БД
     */
    @PostMapping(consumes = "application/json;charset=UTF-8", produces = "text/plain;charset=UTF-8")
    public ResponseEntity<?> createUser(@RequestBody UserDTO dto) {
        try {
            userService.createUser(dto);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (UserServiceException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RepositoryException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }


    /**
     * Метод получения данных всех пользователей
     *
     * @return 200 List<UserDTO> при успехе
     * 500 - в случае ошибки обращения к БД
     */
    @GetMapping(produces = "application/json;charset=UTF-8")
    public ResponseEntity<?> getAllUsers() {
        try {
            return ResponseEntity.ok(userService.getAllUsers());
        } catch (RepositoryException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }


    /**
     * Метод получения данных пользователя по ID
     *
     * @param id ID пользователя
     * @return 200 UserDTO - при успехе
     * 404 - если пользователя с указанным ID не нашлось
     * 400 - в случае неправильных данных
     * 500 - в случае ошибки обращения к БД
     */
    @GetMapping(value = "/{id}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<?> getUserById(@PathVariable("id") long id) {
        try {
            return userService.getUserById(new RequestUserDTO(id))
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RepositoryException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }


    /**
     * Метод обновления пользователя
     *
     * @param dto UserDTO с обновленными данными
     * @return 200 - при успехе
     * 400 - в случае неправильных данных
     * 500 - в случае ошибки обращения к БД
     */
    @PutMapping(value = "/{id}", consumes = "application/json;charset=UTF-8", produces = "text/plain;charset=UTF-8")
    public ResponseEntity<?> updateUser(@PathVariable("id") long id, @RequestBody UserDTO dto) {

        if (id != dto.getId()) {
            return ResponseEntity.badRequest().body("ID в пути и теле не совпадают.");
        }

        try {
            userService.updateUser(dto);
            return ResponseEntity.ok().build();
        } catch (UserServiceException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RepositoryException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }


    /**
     * Метод удаления пользователя
     *
     * @param id ID пользователя для удаления
     * @return 204 - при успехе
     * 400 - в случае неправильных данных
     * 500 - в случае ошибки обращения к БД
     */
    @DeleteMapping(value = "/{id}", produces = "text/plain;charset=UTF-8")
    public ResponseEntity<?> deleteUser(@PathVariable("id") long id) {
        try {
            userService.deleteUser(new RequestUserDTO(id));
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RepositoryException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}