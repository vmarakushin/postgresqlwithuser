package com.example.userservice.model;

import lombok.*;
import lombok.experimental.Accessors;
import jakarta.persistence.*;

import java.util.Date;


/**
 * Класс {@code User} представляет собой сущность пользователя.
 * Основные данные: ID, имя, фамилия, возраст, номер телефона, email, время создания и баланс.
 * Содержит в себе геттеры для всех полей
 * и сеттеры для: name, surname, age, phone, email
 * а также методы для работы с балансом.
 *
 * @author vmarakushin
 * @version 3.0
 */
@Getter
@Accessors(chain = true)
@Entity
@Table(name = "users")
@ToString
@EqualsAndHashCode
@Builder
@AllArgsConstructor
public class User {

    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /**
     * Имя
     */
    @Setter
    private String name;

    /**
     * Фамилия
     */
    @Setter
    private String surname;

    /**
     * Возраст
     */
    @Setter
    private int age;

    /**
     * Номер телефона
     */
    @Setter
    private String phone;

    /**
     * Адрес электронки
     */
    @Setter
    private String email;


    /**
     * Баланс
     */
    private long money;

    /**
     * Время создания
     */
    @Column(updatable = false)
    private final Date createdAt;


    public User() {
        this.createdAt = new Date();
    }


    /**
     * Увеличивает баланс пользователя на указанное число.
     * Число преобразуется в модуль для избежания багов.
     *
     * @param money сумма для увеличения.
     */
    public void increaseMoney(long money) {
        this.money += Math.abs(money);
    }


    /**
     * Уменьшает баланс пользователя на указанное число.
     * Число преобразуется в модуль для избежания багов.
     *
     * @param money сумма для уменьшения.
     */
    public void decreaseMoney(long money) {
        this.money -= Math.abs(money);
    }


    public User clone() {
        return User.builder()
                .id(this.id)
                .name(this.name)
                .surname(this.surname)
                .age(this.age)
                .phone(this.phone)
                .email(this.email)
                .money(this.money)
                .createdAt(new Date(this.createdAt.getTime()))
                .build();
    }
}


