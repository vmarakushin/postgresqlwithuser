package mypackage;


import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Класс {@code User} представляет собой сущность пользователя.
 * Основные данные: ID, имя, фамилия, возраст, номер телефона, email, время создания и баланс.
 * <p>
 * Содержит в себе геттеры для всех полей
 * и сеттеры для: name, surname, age, phone, email
 * а также методы для работы с балансом.
 * </p>
 * @author vmarakushin
 * @version 1.0
 */
@Entity
@Table(name = "users")
public class User {

    /** ID */
    @Id
    private int id;
    /** Имя */
    private String name;
    /** Фамилия */
    private String surname;
    /** Возраст */
    private int age;
    /** Номер телефона */
    private String phone;
    /** Адрес электронки */
    private String email;
    /** Баланс */
    private long money;
    /** Время создания */
    private final Date createdAt;


    /**
     * Конструктор для создания нового пользователя.
     * Баланс по умолчанию 0.
     * ID выдается {@link UserIdService}
     * created_at записывает дату при создании экземпляра.
     * @param name Имя пользователя.
     * @param surname Фамилия пользователя.
     * @param phone Номер телефона пользователя.
     * @param email Адрес электронной почты пользователя.
     */
    public User(String name, String surname, int age, String phone, String email) {
        this.id = UserIdService.getId();
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.phone = phone;
        this.email = email;
        this.money = 0;
        this.createdAt = new Date();
        System.out.println("Создан пользователь:");
        this.show();
    }
    public User() {
        this.createdAt = null;
    }


    /**
     * Устанавливает имя пользователя.
     * @param name имя пользователя.
     */
    public void setName(String name){
        this.name = name;
    }


    /**
     * Возвращает имя пользователя.
     * @return имя пользователя.
     */
    public String getName(){
        return name;
    }


    /**
     * Устанавливает фамилию пользователя.
     * @param surname фамилия пользователя.
     */
    public void setSurname(String surname){
        this.surname = surname;
    }


    /**
     * Возвращает фамилию пользователя.
     * @return фамилия пользователя.
     */
    public String getSurname(){
        return surname;
    }


    /**
     * Устанавливает телефон пользователя.
     * @param phone телефон пользователя.
     */
    public void setPhone(String phone){
        this.phone = phone;
    }


    /**
     * Возвращает телефон пользователя.
     * @return телефон пользователя.
     */
    public String getPhone(){
        return phone;
    }


    /**
     * Устанавливает электронную почту пользователя.
     * @param email электронная почта пользователя.
     */
    public void setEmail(String email){
        this.email = email;
    }


    /**
     * Возвращает электронную почту пользователя.
     * @return электронная почта пользователя.
     */
    public String getEmail(){
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
    };


    /**
     * Устанавливает возраст пользователя
     * @param age возраст пользователя
     */
    public void setAge(int age){
        this.age = age;
    }


    /**
     * Возвращает возраст пользователя
     * @return возраст пользователя
     */
    public int getAge(){
        return age;
    }


    /**
     * Возвращает дату создания пользователя
     * @return дата создания пользователя
     */
    public Date getCreatedAt(){
        return createdAt;
    }


    /**
     * Метод для отображения пользователя
     */
    public void show() {
        System.out.println("Id: " + id);
        System.out.println("Имя: " + name);
        System.out.println("Фамилия: " + surname);
        System.out.println("Возраст: " + age);
        System.out.println("Телефон: " + phone);
        System.out.println("Email: " + email);
        System.out.println("Баланс: " + money);
        System.out.println("Создан: " + createdAt.toString());
    }
}


