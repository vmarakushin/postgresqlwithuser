package com.example.userservice.app;


import com.example.userservice.model.User;
import com.example.userservice.dao.UserDAO;
import com.example.userservice.vo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
 * @author vmarakushin
 * @version 2.0
 */
public class UserConsoleApp {
    public static final Logger logger = LoggerFactory.getLogger(UserConsoleApp.class);
    private static final Scanner scanner = new Scanner(System.in);
    private static final UserDAO userDAO = new UserDAO();


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


            int choice = readValidInt("Выбор: ");
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
     *
     *
     */
    private static void createUser() {

        User user = new User()
                .setName(readValidInput("Введите имя: ", Name::new))
                .setSurname(readValidInput("Введите фамилию: ", Surname::new))
                .setAge(readValidIntInput("Введите возраст: ", Age::new))
                .setPhone(readValidInput("Введите телефон: ", Phone::new))
                .setEmail(readValidInput("Введите Email: ", Email::new));

        userDAO.saveUser(user);

        logger.info("✅ Пользователь создан!");
    }

    /**
     * Вывод информации обо всех пользователях
     */
    private static void showAllUsers() {
        List<User> users = userDAO.getAllUsers();
        if (users.isEmpty()) {
            logger.info("Нет пользователей.");
        } else {
            users.forEach(User::show);
        }
    }

    /**
     * Вывод информации о пользователе по ID
     */
    private static void findUserById() {
        int id = readValidInt("ID пользователя: ");
        User user = userDAO.getUserById(id);
        if (user != null) {
            user.show();
        } else {
            logger.info("Пользователь не найден.");
        }
    }

    /**
     * Обновление данных пользователя
     */
    private static void updateUser() {

        int id = readValidInt("ID пользователя для обновления: ");

        User user = userDAO.getUserById(id);

        if (user == null) {
            logger.info("Пользователь не найден.");
            return;
        }

        user.show();

        System.out.println("Введите новые данные или оставьте ввод пустым");

        user.setName(readValidInputUpdate("Введите новое имя: ", Name::new, user.getName()))
                .setSurname(readValidInputUpdate("Введите новую фамилию: ", Surname::new, user.getSurname()))
                .setAge(readValidIntInputUpdate("Введите новый возраст или 0:", Age::new, user.getAge()))
                .setEmail(readValidInputUpdate("Введите новый Email: ", Email::new, user.getEmail()))
                .setPhone(readValidInputUpdate("Введите новый телефон: ", Phone::new, user.getPhone()));

        userDAO.updateUser(user);
        logger.info("✅ Данные обновлены.");
    }

    /**
     * Удаление пользователя
     */
    private static void deleteUser() {
        int id = readValidInt("ID пользователя для удаления: ");

        User user = userDAO.getUserById(id);
        if (user == null) {
            System.out.println("Пользователь не найден.");
            return;
        }

        userDAO.deleteUser(id);
        logger.info("✅ Пользователь удалён.");
    }

    /**
     * Проверяет ввод пользователя для int
     * @param message сообщение для пользователя при вводе(опционально)
     * @return валидированное целочисленное значение
     */
    private static int readValidInt(String message){
        while (true) {
            System.out.println(message);
            try {
                int x = Integer.parseInt(scanner.nextLine());
                return x;
            } catch (NumberFormatException e) {
                logger.info("Введите целое число!");
            }
        }
    }
    private static int readValidInt(){
        while (true) {
            try {
                int x = Integer.parseInt(scanner.nextLine());
                return x;
            } catch (NumberFormatException e) {
                logger.info("Введите целое число!");
            }
        }
    }


    /**
     * Валидатор данных для создания пользователя
     *
     * @param message сообщение в сосноль при вводе
     * @param parser ссылка на конструктор VO (в нашем случае)
     * @return обьект VO
     * @param <T> обьект VO
     */
    private static <T> T readValidInput(String message, ThrowingFunction<String, T> parser) {
        while (true) {
            System.out.print(message);
            String input = scanner.nextLine();
            try {
                return parser.apply(input);
            } catch (Exception e) {
                logger.warn("Ошибка валидации ввода: {}", e.getMessage());
            }
        }
    }

    /**
     * Валидатор данных для создания пользователя для возраста
     *
     * @param message сообщение в сосноль при вводе
     * @param parser ссылка на конструктор VO (в нашем случае)
     * @return обьект VO
     * @param <T> обьект VO
     */
    private static <T> T readValidIntInput(String message, ThrowingFunction<Integer, T> parser) {
        while (true) {
            System.out.print(message);
            int number = readValidInt();
            try {
                return parser.apply(number);
            } catch (Exception e) {
                logger.warn("Ошибка валидации ввода: {}", e.getMessage());
            }
        }
    }

    /**
     * Валидатор данных для обновления пользователя
     *
     * @param message сообщение в сосноль при вводе
     * @param parser ссылка на конструктор VO (в нашем случае)
     * @return обьект VO
     * @param <T> обьект VO
     */
    private static <T> T readValidInputUpdate(String message, ThrowingFunction<String, T> parser, T oldOne) {
        while (true) {
            System.out.print(message);
            String input = scanner.nextLine();
            if (input.isBlank()) return oldOne;
            try {
                return parser.apply(input);
            } catch (Exception e) {
                logger.warn("Ошибка валидации ввода: {}", e.getMessage());
            }
        }
    }

    /**
     * Валидатор данных для обновления пользователя для возраста
     *
     * @param message сообщение в сосноль при вводе
     * @param parser ссылка на конструктор VO (в нашем случае)
     * @return обьект VO
     * @param <T> обьект VO
     */
    private static <T> T readValidIntInputUpdate(String message, ThrowingFunction<Integer, T> parser, T oldAge) {
        while (true) {
            System.out.print(message);
            int number = readValidInt();
            if (number == 0) return oldAge;
            try {
                return parser.apply(number);
            } catch (Exception e) {
                logger.warn("Ошибка валидации ввода: {}", e.getMessage());
            }
        }
    }
}

