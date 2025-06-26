package com.example.userservice.app;

import com.example.userservice.exception.DaoException;
import com.example.userservice.model.User;
import com.example.userservice.dao.UserDAO;
import com.example.userservice.service.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


/**
 * Класс {@code UserConsoleApp} представляет собой пользовательский интерфейс
 * Предоставляет методы для:
 * Создания нового пользователя
 * Вывода информации обо всех пользователях
 * Поиска пользователя по ID
 * Обновления пользователя
 * Удаления пользователя
 * Использует {@link UserDAO}
 *
 * @author vmarakushin
 * @version 2.1
 */
public class UserConsoleApp {
    private static final Logger logger = LoggerFactory.getLogger(UserConsoleApp.class);


    /**
     * Главное меню консольного интерфейса
     */
    public static void main(String[] args) {
        while (true) {


            System.out.println("\n=== Меню пользователя ===");
            System.out.println("1. Создать нового пользователя");
            System.out.println("2. Показать всех пользователей");
            System.out.println("3. Найти пользователя по ID");
            System.out.println("4. Обновить пользователя");
            System.out.println("5. Удалить пользователя");
            System.out.println("0. Выход");


            int choice = Validator.validInt("Выбор: ");
            switch (choice) {
                case 1 -> createUser();
                case 2 -> showAllUsers();
                case 3 -> findUserById();
                case 4 -> updateUser();
                case 5 -> deleteUser();
                case 0 -> {
                    System.out.println("До встречи!");
                    return;
                }
                default -> System.out.println("Неверный ввод. Попробуй снова.");
            }
        }
    }


    /**
     * Создает нового пользователя на основании введенных данных
     */
    private static void createUser() {

        User user = new User()
                .name(Validator.validStringCreate("Введите имя: ", Validator::name))
                .surname(Validator.validStringCreate("Введите фамилию: ", Validator::surname))
                .age(Validator.validIntCreate("Введите возраст: ", Validator::age))
                .phone(Validator.validStringCreate("Введите телефон: ", Validator::phone))
                .email(Validator.validStringCreate("Введите Email: ", Validator::email));

        user.show();

        try {
            UserDAO.saveUser(user);
        }catch (DaoException e) {
            System.out.println("ОШИБКА! "+e.getMessage());
            return;
        }
        System.out.println("Пользователь создан!");
    }

    /**
     * Вывод информации обо всех пользователях
     */
    private static void showAllUsers() {
        List<User> users;
        try {
            users = UserDAO.getAllUsers();
        }catch (DaoException e) {
            System.out.println("ОШИБКА! "+e.getMessage());
            return;
        }
        if (users.isEmpty()) {
            System.out.println("Нет пользователей.");
        } else {
                users.forEach(User::show);
        }

    }

    /**
     * Вывод информации о пользователе по ID
     */
    private static void findUserById() {
        int id = Validator.validInt("ID пользователя: ");
        User user = null;
        try {
            user = UserDAO.getUserById(id);
        }catch (DaoException e) {
            System.out.println("ОШИБКА! "+e.getMessage());
        }
        if (user != null) {
            user.show();
        } else {
            System.out.println("Пользователь не найден.");
        }

    }

    /**
     * Обновление данных пользователя
     */
    private static void updateUser() {

        int id = Validator.validInt("ID пользователя для обновления: ");

        User user;
        try {
            user = UserDAO.getUserById(id);

        }catch(DaoException e) {
            System.out.println("ОШИБКА! "+e.getMessage());
            return;
        }
        if (user == null) {
            System.out.println("Пользователь не найден.");
            return;
        }

        user.show();

        System.out.println("Введите новые данные или оставьте ввод пустым");

        user.name(Validator.validStringUpdate("Введите новое имя: ", Validator::name, user.name()))
                .surname(Validator.validStringUpdate("Введите новую фамилию: ", Validator::surname, user.surname()))
                .age(Validator.validIntUpdate("Введите новый возраст или 0:", Validator::age, user.age()))
                .email(Validator.validStringUpdate("Введите новый Email: ", Validator::email, user.email()))
                .phone(Validator.validStringUpdate("Введите новый телефон: ", Validator::phone, user.phone()));

        try {
            UserDAO.updateUser(user);
        }catch (DaoException e) {
            System.out.println("ОШИБКА! "+e.getMessage());
            return;
        }
            System.out.println("Данные обновлены.");
    }

    /**
     * Удаление пользователя
     */
    private static void deleteUser() {
        int id = Validator.validInt("ID пользователя для удаления: ");

        User user = null;

        try {
            user = UserDAO.getUserById(id);

        }catch (DaoException e) {
            System.out.println("ОШИБКА! "+e.getMessage());
        }
        if (user == null) {
            System.out.println("Пользователь не найден.");
            return;
        }

        try {
            UserDAO.deleteUser(id);
        }catch (DaoException e) {
            System.out.println("ОШИБКА! "+e.getMessage());
        }

        System.out.println("Пользователь удалён.");
    }
}