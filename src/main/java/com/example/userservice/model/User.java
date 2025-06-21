package com.example.userservice.model;
import com.example.userservice.vo.*;


import java.util.Date;
import javax.persistence.*;

/**
 * Класс {@code User} представляет собой сущность пользователя.
 * Основные данные: ID, имя, фамилия, возраст, номер телефона, email, время создания и баланс.
 *
 * Содержит в себе геттеры для всех полей
 * и сеттеры для: name, surname, age, phone, email, поддерживают fluent
 * а также методы для работы с балансом.
 *
 * @author vmarakushin
 * @version 1.1
 */
@Entity(name = "User")
@Table(name = "users")
public class User {

    /** ID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /** Имя */
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "name"))
    private Name name;
    /** Фамилия */
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "surname"))
    private Surname surname;
    /** Возраст */
    @Embedded
    private Age age;
    /** Номер телефона */
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "phone"))
    private Phone phone;
    /** Адрес электронки */
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "email"))
    private Email email;
    /** Баланс */
    private long money;
    /** Время создания */
    @Column(updatable = false)
    private final Date createdAt;



    public User() {
        this.createdAt = new Date();
    }

    /**
     * Устанавливает имя пользователя.
     * @param name имя пользователя.
     */
    public User setName(Name name){
        this.name = name;
        return this;
    }


    /**
     * Возвращает имя пользователя.
     * @return имя пользователя.
     */
    public Name getName(){
        return name;
    }


    /**
     * Устанавливает фамилию пользователя.
     * @param surname фамилия пользователя.
     */
    public User setSurname(Surname surname){
        this.surname = surname;
        return this;
    }


    /**
     * Возвращает фамилию пользователя.
     * @return фамилия пользователя.
     */
    public Surname getSurname(){
        return surname;
    }


    /**
     * Устанавливает телефон пользователя.
     * @param phone телефон пользователя.
     */
    public User setPhone(Phone phone){
        this.phone = phone;
        return this;
    }


    /**
     * Возвращает телефон пользователя.
     * @return телефон пользователя.
     */
    public Phone getPhone(){
        return phone;
    }


    /**
     * Устанавливает электронную почту пользователя.
     * @param email электронная почта пользователя.
     */
    public User setEmail(Email email){
        this.email = email;
        return this;
    }


    /**
     * Возвращает электронную почту пользователя.
     * @return электронная почта пользователя.
     */
    public Email getEmail(){
        return email;
    }


    /**
     * Увеличивает баланс пользователя на указанное число.
     * Число преобразуется в модуль для избежания багов.
     * @param money сумма для увеличения.
     */
    public void increaseMoney(long money){
        money = Math.abs(money);
        this.money += money;
    }


    /**
     * Уменьшает баланс пользователя на указанное число.
     * Число преобразуется в модуль для избежания багов.
     * @param money сумма для уменьшения.
     */
    public void decreaseMoney(long money){
        money = Math.abs(money);
        this.money -= money;
    }


    /**
     * Возвращает баланс пользователя.
     * @return баланс пользователя.
     */
    public long getMoney(){
        return money;
    }


    /**
     * Возвращает Id пользователя
     * @return Id пользователя
     */
    public int getId(){
        return id;
    }


    /**
     * Устанавливает возраст пользователя
     * @param age возраст пользователя
     */
    public User setAge(Age age){
        this.age = age;
        return this;
    }


    /**
     * Возвращает возраст пользователя
     * @return возраст пользователя
     */
    public Age getAge(){
        return age;
    }


    /**
     * Возвращает дату создания пользователя
     * @return дата создания пользователя
     */
    public Date getCreatedAt(){
        return createdAt;
    }



    public String toString() {
        return (    "Id: " + getId()
                + "\nИмя: " + getName()
                + "\nФамилия: " + getSurname()
                + "\nВозраст: " + getAge()
                + "\nТелефон: " + getPhone()
                + "\nEmail: " + getEmail()
                + "\nБаланс: " + getMoney()
                + "\nСоздан: " + getCreatedAt()
        );
    }

    public void show(){
        System.out.println(this);
    }
}


