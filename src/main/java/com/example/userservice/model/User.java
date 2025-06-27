package com.example.userservice.model;

import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

/**
 * Класс {@code User} представляет собой сущность пользователя.
 * Основные данные: ID, имя, фамилия, возраст, номер телефона, email, время создания и баланс.
 * <p>
 * Содержит в себе геттеры для всех полей
 * и сеттеры для: name, surname, age, phone, email, поддерживают fluent
 * а также методы для работы с балансом.
 *
 * @author vmarakushin
 * @version 3.0
 */
@Getter
@Accessors(chain = true)
@Entity(name = "User")
@Table(name = "users")
@ToString
@EqualsAndHashCode
@Builder
@AllArgsConstructor
public class User implements Cloneable {

    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

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


    /** Баланс */
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
        this.money -= money = Math.abs(money);
    }


    public void show() {
        System.out.println(this);
    }

    public User clone() throws CloneNotSupportedException {
        return (User) super.clone();
    }
}


