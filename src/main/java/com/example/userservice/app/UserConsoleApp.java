package com.example.userservice.app;


import com.example.userservice.exception.DaoException;
import com.example.userservice.model.User;
import com.example.userservice.dao.UserDAO;
import com.example.userservice.service.Validator;

import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;


/**
 * Класс {@code UserConsoleApp} представляет собой пользовательский интерфейс
 * Предоставляет методы для:
 * Создания нового пользователя
 * Вывода информации обо всех пользователях
 * Поиска пользователя по ID
 * Обновления пользователя
 * Удаления пользователя
 *
 * @author vmarakushin
 * @version 3.7
 */
public class UserConsoleApp {

    private final static UserDAO userDAO = new UserDAO();

    private final static Scanner scanner = new Scanner(System.in);

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


            int choice = inputCycle("Выбор: ", Validator::validInt);

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

        System.out.println("##################################");
        System.out.println("## Создать нового пользователя ##");
        System.out.println("##################################");


        String name = inputCycle("Введите имя: ", input -> Validator.validStringCreate(input, Validator::name));
        String surname = inputCycle("Введите фамилию: ", input -> Validator.validStringCreate(input, Validator::surname));
        int age = inputCycle("Введите возраст: ", input -> Validator.validIntCreate(input, Validator::age));
        String phone = inputCycle("Введите телефон: ", input -> Validator.validStringCreate(input, Validator::phone));
        String email = inputCycle("Введите email: ", input -> Validator.validStringCreate(input, Validator::email));


        User user = User.builder()
                .name(name)
                .surname(surname)
                .age(age)
                .phone(phone)
                .email(email)
                .createdAt(new Date())
                .build();


        user.show();

        int choice = inputCycle("Создать пользователя с указанными данными? 1 - да, остальное - нет", Validator::validInt);

        if (choice == 1) {
            try {
                userDAO.saveUser(user);
                System.out.println("Пользователь создан!");
            } catch (DaoException e) {
                System.out.println("ОШИБКА! Создать пользователя не удалось.");
            }
        } else {
            System.out.println("Отмена");
        }

    }

    /**
     * Вывод информации обо всех пользователях
     */
    private static void showAllUsers() {

        System.out.println("#################################");
        System.out.println("## Показать всех пользователей ##");
        System.out.println("#################################");


        List<User> users;
        try {
            users = userDAO.getAllUsers();
        } catch (DaoException e) {
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

        System.out.println("##############################");
        System.out.println("## Найти пользователя по ID ##");
        System.out.println("##############################");

        int id = inputCycle("ID пользователя: ", Validator::validInt);
        User user;
        try {
            user = userDAO.getUserById(id);
        } catch (DaoException e) {
            System.out.println("ОШИБКА!  Получить пользователя не удалось.");
            return;
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

        System.out.println("###########################");
        System.out.println("## Обновить пользователя ##");
        System.out.println("###########################");


        int id = inputCycle("ID пользователя: ", Validator::validInt);
        User user;

        try {
            user = userDAO.getUserById(id);

        } catch (DaoException e) {
            System.out.println("ОШИБКА! Получить пользователя не удалось.");
            return;
        }
        if (user == null) {
            System.out.println("Пользователь не найден.");
            return;
        }

        user.show();

        User oldUser = user.clone();


        updateField("Введите новое имя: ", user::setName, input -> Validator.validStringCreate(input, Validator::name));
        updateField("Введите новую фамилию: ", user::setSurname, input -> Validator.validStringCreate(input, Validator::surname));
        updateField("Введите новый возраст: ", user::setAge, input -> Validator.validIntCreate(input, Validator::age));
        updateField("Введите новый email: ", user::setEmail, input -> Validator.validStringCreate(input, Validator::email));
        updateField("Введите новый телефон: ", user::setPhone, input -> Validator.validStringCreate(input, Validator::phone));

        System.out.println("Старые данные:");
        oldUser.show();
        System.out.println("Новые данные:");
        user.show();

        int choice = inputCycle("Обновить пользователя? 1 - да, остальное - нет ", Validator::validInt);
        if (choice == 1) {
            try {
                userDAO.updateUser(user);
            } catch (DaoException e) {
                System.out.println("ОШИБКА! Обновить пользователя не удалось.");
            }
            System.out.println("Данные обновлены.");

        } else System.out.println("Отмена");

    }

    /**
     * Удаление пользователя
     */
    private static void deleteUser() {

        System.out.println("##########################");
        System.out.println("## Удалить пользователя ##");
        System.out.println("##########################");


        int id = inputCycle("ID пользователя: ", Validator::validInt);


        User user;

        try {
            user = userDAO.getUserById(id);

        } catch (DaoException e) {
            System.out.println("ОШИБКА! Получить пользователя не удалось.");
            return;
        }
        if (user == null) {
            System.out.println("Пользователь не найден.");
            return;
        }

        user.show();


        int choice = inputCycle("Удалить пользователя? 1 - да, остальное - нет ", Validator::validInt);

        if (choice == 1) {
            try {
                userDAO.deleteUser(id);
                System.out.println("Пользователь удалён.");
            } catch (DaoException e) {
                System.out.println("ОШИБКА! Удалить пользователя не удалось");
            }
        } else {
            System.out.println("Отмена");
        }
    }


    private static <T> void updateField(String label, Consumer<T> setter, InputParser<T> parser) {
        while (true) {
            try {
                System.out.print(label);
                String input = scanner.nextLine();
                if (!input.isBlank()) {
                    T value = parser.parse(input);
                    setter.accept(value);
                }
                break;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Цикл ввода с валидацией
     *
     * @param prompt сообщение пользователю
     * @param parser валидатор
     * @param <T>    стринга для ввода
     * @return валидированное значение int или String
     */
    public static <T> T inputCycle(String prompt, InputParser<T> parser) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine();
                return parser.parse(input);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}