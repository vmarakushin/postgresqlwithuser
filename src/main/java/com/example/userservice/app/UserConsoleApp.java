package com.example.userservice.app;

import com.example.userservice.exception.DaoException;
import com.example.userservice.model.User;
import com.example.userservice.dao.UserDAO;
import com.example.userservice.service.Reader;
import com.example.userservice.service.Validator;

import java.util.Date;
import java.util.List;
import java.util.Scanner;


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
 * @version 3.0
 */
public class UserConsoleApp {
    private final static Reader reader = new Reader(new Scanner(System.in));

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


            int choice = Validator.validInt("Выбор: ", reader);
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

        User user = User.builder()
                .name(Validator.validStringCreate("Введите имя: ", Validator::name))
                .surname(Validator.validStringCreate("Введите фамилию: ", Validator::surname))
                .age(Validator.validIntCreate("Введите возраст: ", Validator::age))
                .phone(Validator.validStringCreate("Введите телефон: ", Validator::phone))
                .email(Validator.validStringCreate("Введите Email: ", Validator::email))
                .createdAt(new Date())
                .build();



        user.show();

        try {
            UserDAO.saveUser(user);
        }catch (DaoException e) {
            System.out.println("ОШИБКА! Создать пользователя не удалось.");
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
            System.out.println("ОШИБКА! Получить список пользователей не удалось.");
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
        int id = Validator.validInt("ID пользователя: ", reader);
        User user = null;
        try {
            user = UserDAO.getUserById(id);
        }catch (DaoException e) {
            System.out.println("ОШИБКА!  Получить пользователя не удалось.");
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

        int id = Validator.validInt("ID пользователя для обновления: ", reader);

        User user;
        try {
            user = UserDAO.getUserById(id);

        }catch(DaoException e) {
            System.out.println("ОШИБКА! Получить пользователя не удалось.");
            return;
        }
        if (user == null) {
            System.out.println("Пользователь не найден.");
            return;
        }

        user.show();
        User oldUser = null;
        try {
            oldUser = user.clone();
        }catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        System.out.println("Введите новые данные или оставьте ввод пустым");

        user.setName(Validator.validStringUpdate("Введите новое имя: ", Validator::name, user.getName()))
                .setSurname(Validator.validStringUpdate("Введите новую фамилию: ", Validator::surname, user.getSurname()))
                .setAge(Validator.validIntUpdate("Введите новый возраст или 0:", Validator::age, user.getAge()))
                .setEmail(Validator.validStringUpdate("Введите новый Email: ", Validator::email, user.getEmail()))
                .setPhone(Validator.validStringUpdate("Введите новый телефон: ", Validator::phone, user.getPhone()));

        System.out.println("Старые данные:");
        oldUser.show();
        System.out.println("Новые данные:");
        user.show();
        int choice = Validator.validInt("Обновить пользователя? 1 - да, остальное - нет ", reader);
        switch (choice) {
            case 1 -> {
                try {
                    UserDAO.updateUser(user);
                }catch (DaoException e) {
                    System.out.println("ОШИБКА! Обновить пользователя не удалось.");
                    return;
                }
                System.out.println("Данные обновлены.");
            }
            default -> System.out.println("Отмена");
        }

    }

    /**
     * Удаление пользователя
     */
    private static void deleteUser() {
        int id = Validator.validInt("ID пользователя для удаления: ", reader);

        User user = null;

        try {
            user = UserDAO.getUserById(id);

        }catch (DaoException e) {
            System.out.println("ОШИБКА! Получить пользователя не удалось.");
        }
        if (user == null) {
            System.out.println("Пользователь не найден.");
            return;
        }

        user.show();

        int choice = Validator.validInt("Удалить пользователя? 1 - да, остальное - нет ", reader);

        switch (choice){

            case 1 ->{
                try {
                UserDAO.deleteUser(id);
                System.out.println("Пользователь удалён.");
                }catch (DaoException e) {
                System.out.println("ОШИБКА! Удалить пользователя не удалось");
                }
            }
            default -> {
                System.out.println("Отмена");
            }
        }
    }
}