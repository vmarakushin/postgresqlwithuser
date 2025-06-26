package com.example.userservice.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

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
 * @version 1.1
 */
@Entity(name = "User")
@Table(name = "users")
@ToString
@EqualsAndHashCode
@Accessors(chain = true, fluent = true)
public class User {

    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private int id;

    /**
     * Имя
     */
    @Getter
    @Setter
    private String name;

    /**
     * Фамилия
     */
    @Getter
    @Setter
    private String surname;

    /**
     * Возраст
     */
    @Getter
    @Setter
    private int age;

    /**
     * Номер телефона
     */
    @Getter
    @Setter
    private String phone;

    /**
     * Адрес электронки
     */
    @Getter
    @Setter
    private String email;

    @Getter
    /** Баланс */
    private long money;

    /**
     * Время создания
     */
    @Column(updatable = false)
    @Getter
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
        money = Math.abs(money);
        this.money += money;
    }


    /**
     * Уменьшает баланс пользователя на указанное число.
     * Число преобразуется в модуль для избежания багов.
     *
     * @param money сумма для уменьшения.
     */
    public void decreaseMoney(long money) {
        money = Math.abs(money);
        this.money -= money;
    }


    public void show() {
        System.out.println(this);
    }
}


