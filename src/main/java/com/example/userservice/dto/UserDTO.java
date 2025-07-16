package com.example.userservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;


import java.util.Date;


/**
 * @author vmarakushin
 * @version 2.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserDTO {
    @Schema(description = "Уникальный идентификатор",example = "Будет выбран автоматически")
    private long id;
    @Schema(description = "Имя пользователя", example = "Василий")
    private String name;
    @Schema(description = "Фамилия пользователя", example = "Иванов")
    private String surname;
    @Schema(description = "Возраст пользователя", example = "От 1 до 150")
    private int age;
    @Schema(description = "Телефонный номер пользователя", example = "+71112223344")
    private String phone;
    @Schema(description = "Email пользователя", example = "example@example.com")
    private String email;
    @Schema(description = "Баланс пользователя", example = "1000000")
    private long money;
    @Schema(description = "Дата создания пользователя",example = "Будет выбрана автоматически")
    private Date createdAt;
}