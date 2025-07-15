package com.example.userservice.app;


import com.example.userservice.dto.RequestUserDTO;
import com.example.userservice.dto.UserDTO;
import com.example.userservice.exception.RepositoryException;
import com.example.userservice.exception.UserServiceException;
import com.example.userservice.service.UserService;
import com.example.userservice.service.Validator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.Consumer;


/**
 * Представляет собой пользовательский интерфейс.
 * Предоставляет методы для:
 * Создания нового пользователя
 * Вывода информации обо всех пользователях
 * Поиска пользователя по ID
 * Обновления пользователя
 * Удаления пользователя
 *
 * @author vmarakushin
 * @version 4.0
 */
@Component
public class UserConsoleApp implements CommandLineRunner {

    private final Validator validator;

    private final Scanner scanner;

    private final UserService userService;


    public UserConsoleApp(@Qualifier("userServiceKafka")UserService userService, Validator validator) {
        this.userService = userService;
        this.scanner = new Scanner(System.in);
        this.validator = validator;
    }


    /**
     * Главное меню консольного интерфейса
     */
    public void run(String[] args) {
        while (true) {

            System.out.println("\n=== Меню пользователя ===");
            System.out.println("1. Создать нового пользователя");
            System.out.println("2. Показать всех пользователей");
            System.out.println("3. Найти пользователя по ID");
            System.out.println("4. Обновить пользователя");
            System.out.println("5. Удалить пользователя");
            System.out.println("0. Выход");


            int choice = inputCycle("Выбор: ", validator::validInt);

            switch (choice) {
                case 1 -> createUser();
                case 2 -> showAllUsers();
                case 3 -> findUserById();
                case 4 -> updateUser();
                case 5 -> deleteUser();
                case 0 -> {
                    System.out.println("До встречи!");
                    System.exit(1);
                }
                default -> System.out.println("Неверный ввод. Попробуй снова.");
            }
        }
    }


    /**
     * Создает нового пользователя на основании введенных данных
     */
    private void createUser() {

        System.out.println("##################################");
        System.out.println("## Создать нового пользователя ##");
        System.out.println("##################################");


        String name = inputCycle("Введите имя: ", input -> validator.validStringCreate(input, validator::name));
        String surname = inputCycle("Введите фамилию: ", input -> validator.validStringCreate(input, validator::surname));
        int age = inputCycle("Введите возраст: ", input -> validator.validIntCreate(input, validator::age));
        String phone = inputCycle("Введите телефон: ", input -> validator.validStringCreate(input, validator::phone));
        String email = inputCycle("Введите email: ", input -> validator.validStringCreate(input, validator::email));


        UserDTO dto = new UserDTO(0L, name, surname, age, phone, email, 0L, new Date());


        System.out.println(dto);


        int choice = inputCycle("Создать пользователя с указанными данными? 1 - да, остальное - нет", validator::validInt);

        if (choice == 1) {
            try {
                userService.createUser(dto);
                System.out.println("Пользователь создан!");
            } catch (UserServiceException | IllegalArgumentException e) {
                System.out.println(e.getMessage());
            } catch (RepositoryException e) {
                System.out.println("ОШИБКА! Создать пользователя не удалось.");
            }
        } else {
            System.out.println("Отмена");
        }

    }

    /**
     * Вывод информации обо всех пользователях
     */
    private void showAllUsers() {

        System.out.println("#################################");
        System.out.println("## Показать всех пользователей ##");
        System.out.println("#################################");


        List<UserDTO> users;
        try {
            users = userService.getAllUsers();
        } catch (RepositoryException e) {
            System.out.println("ОШИБКА! Получить список пользователей не удалось.");
            return;
        }
        if (users.isEmpty()) {
            System.out.println("Нет пользователей.");
        } else {
            users.forEach(System.out::println);
        }

    }

    /**
     * Вывод информации о пользователе по ID
     */
    private void findUserById() {

        System.out.println("##############################");
        System.out.println("## Найти пользователя по ID ##");
        System.out.println("##############################");

        long id = inputCycle("ID пользователя: ", input -> validator.validIdCreate(input, validator::id));

        Optional<UserDTO> user;

        try {
            user = userService.getUserById(new RequestUserDTO(id));
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return;
        } catch (RepositoryException e) {
            System.out.println("ОШИБКА!  Получить пользователя не удалось.");
            return;
        }

        if (user.isPresent()) {
            System.out.println(user.get());
        } else {
            System.out.println("Пользователь не найден.");
        }

    }

    /**
     * Обновление данных пользователя
     */
    private void updateUser() {

        System.out.println("###########################");
        System.out.println("## Обновить пользователя ##");
        System.out.println("###########################");


        long id = inputCycle("ID пользователя: ", input -> validator.validIdCreate(input, validator::id));
        Optional<UserDTO> user;

        try {
            user = userService.getUserById(new RequestUserDTO(id));
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return;
        } catch (RepositoryException e) {
            System.out.println("ОШИБКА! Получить пользователя не удалось.");
            return;
        }

        if (user.isEmpty()) {
            System.out.println("Пользователь не найден.");
            return;
        }

        UserDTO userDTO = user.get();
        System.out.println(userDTO);

        UserDTO oldUser = new UserDTO(
                userDTO.getId(),
                userDTO.getName(),
                userDTO.getSurname(),
                userDTO.getAge(),
                userDTO.getPhone(),
                userDTO.getEmail(),
                userDTO.getMoney(),
                userDTO.getCreatedAt()
        );


        updateField("Введите новое имя: ", userDTO::setName, input -> validator.validStringCreate(input, validator::name));
        updateField("Введите новую фамилию: ", userDTO::setSurname, input -> validator.validStringCreate(input, validator::surname));
        updateField("Введите новый возраст: ", userDTO::setAge, input -> validator.validIntCreate(input, validator::age));
        updateField("Введите новый email: ", userDTO::setEmail, input -> validator.validStringCreate(input, validator::email));
        updateField("Введите новый телефон: ", userDTO::setPhone, input -> validator.validStringCreate(input, validator::phone));

        System.out.println("Старые данные:" + oldUser);

        System.out.println("Новые данные:" + userDTO);

        int choice = inputCycle("Обновить пользователя? 1 - да, остальное - нет ", validator::validInt);
        if (choice == 1) {
            try {
                userService.updateUser(userDTO);
            } catch (IllegalArgumentException | UserServiceException e) {
                System.out.println(e.getMessage());
                return;
            } catch (RepositoryException e) {
                System.out.println("ОШИБКА! Обновить пользователя не удалось.");
            }
            System.out.println("Данные обновлены.");

        } else System.out.println("Отмена");

    }

    /**
     * Удаление пользователя
     */
    private void deleteUser() {

        System.out.println("##########################");
        System.out.println("## Удалить пользователя ##");
        System.out.println("##########################");


        long id = inputCycle("ID пользователя: ", input -> validator.validIdCreate(input, validator::id));


        Optional<UserDTO> user;

        try {
            user = userService.getUserById(new RequestUserDTO(id));
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return;
        } catch (RepositoryException e) {
            System.out.println("ОШИБКА! Получить пользователя не удалось.");
            return;
        }
        if (user.isEmpty()) {
            System.out.println("Пользователь не найден.");
            return;
        }

        System.out.println(user.get());


        int choice = inputCycle("Удалить пользователя? 1 - да, остальное - нет ", validator::validInt);

        if (choice == 1) {
            try {
                userService.deleteUser(new RequestUserDTO(id));
                System.out.println("Пользователь удалён.");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            } catch (RepositoryException e) {
                System.out.println("ОШИБКА! Удалить пользователя не удалось");
            }
        } else {
            System.out.println("Отмена");
        }
    }


    /**
     * Цикл ввода с валидацией для сеттера
     *
     * @param label  сообщение пользователю
     * @param setter необходимый сеттер
     * @param parser валидатор
     * @param <R>    результат парсера для сеттера
     */
    private <R> void updateField(String label, Consumer<R> setter, ThrowingFunction<String, R> parser) {
        while (true) {
            try {
                System.out.print(label);
                String input = scanner.nextLine();
                if (!input.isBlank()) {
                    R value = parser.apply(input);
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
     * @param <R>    результат парсинга
     * @return валидированное значение, тип - в зависимости от парсера
     */
    public <R> R inputCycle(String prompt, ThrowingFunction<String, R> parser) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine();
                return parser.apply(input);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}