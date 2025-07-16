package com.example.userservice.controller;

import com.example.userservice.dto.RequestUserDTO;
import com.example.userservice.dto.UserDTO;
import com.example.userservice.service.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


/**
 * Контроллер для REST взаимодействия с UserService
 *
 * @author vmarakushin
 * @version 2.0
 */
@Tag(name = "User API", description = "Операции с пользователями")
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserServiceImpl userService;

    public UserController(UserServiceImpl userService) {
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
    @Operation(summary = "Создать нового пользователя")
    @PostMapping(consumes = "application/json;charset=UTF-8", produces = "text/plain;charset=UTF-8")
    public ResponseEntity<?> createUser(@RequestBody UserDTO dto) {
        userService.createUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    /**
     * Метод получения данных всех пользователей
     *
     * @return 200 List<UserDTO> при успехе
     * 500 - в случае ошибки обращения к БД
     */
    @Operation(summary = "Получить список всех пользователей")
    @GetMapping(produces = "application/json;charset=UTF-8")
    public ResponseEntity<?> getAllUsers() {
        var users = userService.getAllUsers();
        var userModels = users.stream()
                .map(user -> EntityModel.of(user,
                        linkTo(methodOn(UserController.class).getUserById(user.getId())).withSelfRel(),
                        linkTo(methodOn(UserController.class).deleteUser(user.getId())).withRel("delete")
                ))
                .toList();
        var collectionModel = CollectionModel.of(
                userModels,
                linkTo(methodOn(UserController.class).getAllUsers()).withSelfRel()
        );
        return ResponseEntity.ok(collectionModel);
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
    @Operation(summary = "Получить пользователя по ID")
    @GetMapping(value = "/{id}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<?> getUserById(@PathVariable("id") long id) {
        return userService.getUserById(new RequestUserDTO(id))
                .map(user -> {
                    EntityModel<UserDTO> model = EntityModel.of(user);
                    model.add(linkTo(methodOn(UserController.class).getUserById(id)).withSelfRel());
                    model.add(linkTo(methodOn(UserController.class).getAllUsers()).withRel("users"));
                    model.add(linkTo(methodOn(UserController.class).deleteUser(id)).withRel("delete"));
                    return ResponseEntity.ok(model);
                })
                .orElse(ResponseEntity.notFound().build());
    }


    /**
     * Метод обновления пользователя
     *
     * @param dto UserDTO с обновленными данными
     * @return 200 - при успехе
     * 400 - в случае неправильных данных
     * 500 - в случае ошибки обращения к БД
     */
    @Operation(summary = "Обновить пользователя")
    @PutMapping(value = "/{id}", consumes = "application/json;charset=UTF-8", produces = "text/plain;charset=UTF-8")
    public ResponseEntity<?> updateUser(@PathVariable("id") long id, @RequestBody UserDTO dto) {

        if (id != dto.getId()) {
            return ResponseEntity.badRequest().body("ID в пути и теле не совпадают.");
        }
        userService.updateUser(dto);
        return ResponseEntity.ok().build();
    }


    /**
     * Метод удаления пользователя
     *
     * @param id ID пользователя для удаления
     * @return 204 - при успехе
     * 400 - в случае неправильных данных
     * 500 - в случае ошибки обращения к БД
     */
    @Operation(summary = "Удалить пользователя")
    @DeleteMapping(value = "/{id}", produces = "text/plain;charset=UTF-8")
    public ResponseEntity<?> deleteUser(@PathVariable("id") long id) {
        userService.deleteUser(new RequestUserDTO(id));
        return ResponseEntity.noContent().build();
    }
}