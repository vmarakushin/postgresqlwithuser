package com.example.userservice.dao;

import com.example.userservice.fabrics.UserFactory;
import com.example.userservice.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;


import java.util.Date;
import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты для БД
 *
 * @author vmarakushin
 * @version 2.0
 */
@Testcontainers
public class UserDAOTest {

    @Container
    private final static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");


    @BeforeAll
    public static void setUp() {
        Properties props = new Properties();
        props.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver");
        props.setProperty("hibernate.connection.url", postgres.getJdbcUrl());
        props.setProperty("hibernate.connection.username", postgres.getUsername());
        props.setProperty("hibernate.connection.password", postgres.getPassword());
        props.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        props.setProperty("hibernate.hbm2ddl.auto", "create-drop");
        props.setProperty("hibernate.show_sql", "true");

        sessionFactory = new Configuration()
                .addProperties(props)
                .addAnnotatedClass(User.class)
                .buildSessionFactory();
    }


    private static SessionFactory sessionFactory;

    private final TransactionalExecutor transactionalExecutor = new TransactionalExecutor(sessionFactory);

    private final UserDAO userDAO = new UserDAO(transactionalExecutor);




    @DisplayName("Проверка работоспособности контейнера")
    @Test
    public void testContainerRunning() {
        assertTrue(postgres.isRunning());
    }

    @DisplayName("Проверка записи/чтения из бд")
    @Test
    public void testDBReadWrite() {
        User expected = User.builder()
                .name("Василий")
                .surname("Бумажкин")
                .age(33)
                .phone("+71112223344")
                .email("vasily_bumazshkin@gmail.com")
                .createdAt(new Date())
                .build();

        transactionalExecutor.execute(session -> session.save(expected));

        int id = expected.getId();

        User actual = transactionalExecutor.execute(session -> session.get(User.class, id));

        assertEquals(expected, actual);
    }

    @DisplayName("1000 и 00 юзеров в бд")
    @Test
    public void testDBWrite() {

        //10 имен
        List<String> names = List.of("Василий", "Дмитрий", "Олег", "Карен", "John", "Raphael", "Раджа", "Мария", "Лена", "Юлия");

        //10 фамилий
        List<String> surnames = List.of("Головач", "Дергач", "Прыгун", "Нырок", "Васюков", "Водкин", "Селедкина", "Smith", "Denver", "Караулова");

        //10 емейлов
        List<String> emails = List.of("gjkshj@kjgvbhksdj.svjhso", "lora.atkinson@gmail.gmail", "gmail@mail.smtp", "shashlykman69@rambler.relbmar", "golovach_lena@mail.ru", "123@fff.sss", "superwoman@woman.man", "krutoy_rybak@fish.com", "newmessage@fishing.lol", "ultimate_disk_in_the_dark@gtasa.ads");

        //1(0) возрастов
        //List<Integer> ages = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        List <Integer> ages = List.of(1);

        //1(0) телефонов
        //List<String> phones = List.of("+79991112233", "+71111111111", "+72222222222", "+73333333333", "+74444444444", "+75555555555", "+76666666666", "+77777777777", "+78888888888", "+79999999999");
        List<String> phones = List.of("+79991112233");


        int croudIndex = names.size() *
                surnames.size() *
                emails.size() *
                ages.size() *
                phones.size();

        System.out.println(
                "Тест " + croudIndex + " пользователей");

        long startTime = System.nanoTime();

        for (String name : names) {

            for (String surname : surnames) {

                for (String email : emails) {

                    for (Integer age : ages) {

                        for (String phone : phones) {

                            transactionalExecutor.execute(session ->
                                    session.save(UserFactory.createUser(name, surname, age, phone, email, true)));
                        }
                    }
                }
            }
        }

        long stopTime = System.nanoTime();
        double duration = (stopTime - startTime) / 1_000_000_000.0;
        System.out.printf("Пользователей: %d; Время выполнения: %.3f сек%n", croudIndex, duration);
    }

    @DisplayName("Test DAO Write/read")
    @Test
    public void testDAOWriteRead() {
        User user = User.builder()
                .name("Иван")
                .surname("Иванов")
                .age(33)
                .phone("+71112223344")
                .email("vasily_bumazshkin@gmail.com")
                .createdAt(new Date())
                .build();


        userDAO.saveUser(user);

        User fromDB;

        fromDB = userDAO.getUserById(1);

        user.show();
        fromDB.show();

        assertEquals(user, fromDB);
    }

    @DisplayName("DAO тест взять всех")
    @Test
    public void testDAOManyEntities(){
        //10 имен
        List<String> names = List.of("Василий", "Дмитрий", "Олег", "Карен", "John", "Raphael", "Раджа", "Мария", "Лена", "Юлия");

        //10 фамилий
        List<String> surnames = List.of("Головач", "Дергач", "Прыгун", "Нырок", "Васюков", "Водкин", "Селедкина", "Smith", "Denver", "Караулова");

        //10 емейлов
        List<String> emails = List.of("gjkshj@kjgvbhksdj.svjhso", "lora.atkinson@gmail.gmail", "gmail@mail.smtp", "shashlykman69@rambler.relbmar", "golovach_lena@mail.ru", "123@fff.sss", "superwoman@woman.man", "krutoy_rybak@fish.com", "newmessage@fishing.lol", "ultimate_disk_in_the_dark@gtasa.ads");

        //1(0) возрастов
        //List<Integer> ages = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        List <Integer> ages = List.of(1);

        //1(0) телефонов
        //List<String> phones = List.of("+79991112233", "+71111111111", "+72222222222", "+73333333333", "+74444444444", "+75555555555", "+76666666666", "+77777777777", "+78888888888", "+79999999999");
        List<String> phones = List.of("+79991112233");

        int croudIndex = names.size() *
                surnames.size() *
                emails.size() *
                ages.size() *
                phones.size();




        for (String name : names) {

            for (String surname : surnames) {

                for (String email : emails) {

                    for (Integer age : ages) {

                        for (String phone : phones) {

                            userDAO.saveUser(UserFactory.createUser(name, surname, age, phone, email, true));
                        }
                    }
                }
            }
        }

        List<User> users = userDAO.getAllUsers();

        users.forEach(User::show);

        System.out.println(
                "Тест " + croudIndex + " пользователей");

        assertEquals(croudIndex, users.size());

    }

    @DisplayName("Тест обнови пользователя")
    @Test
    public void testDAOUpdate() {

        User user = User.builder()
                .name("Иван")
                .surname("Иванов")
                .age(33)
                .phone("+71112223344")
                .email("vasily_bumazshkin@gmail.com")
                .createdAt(new Date())
                .build();

        userDAO.saveUser(user);


        user.setName("Лёха").setSurname("Петров");

        userDAO.updateUser(user);

        User result = userDAO.getUserById(user.getId());

        assertEquals(user, result);

    }

    @DisplayName("Тест удалиииииииии")
    @Test
    public void testDAODelete() {

        User user = User.builder()
                .name("Иван")
                .surname("Иванов")
                .age(33)
                .phone("+71112223344")
                .email("vasily_bumazshkin@gmail.com")
                .createdAt(new Date())
                .build();

        userDAO.saveUser(user);

        userDAO.deleteUser(user.getId());

        User result = userDAO.getUserById(user.getId());

        assertNull(result);

    }

}