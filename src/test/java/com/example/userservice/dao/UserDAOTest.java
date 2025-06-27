package com.example.userservice.dao;

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
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Тест для БД
 * @author vmarakushin
 * @version 1.0
 */
@Testcontainers
public class UserDAOTest {

    @Container
    private final static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    private static SessionFactory sessionFactory;


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

        TransactionalExecutor.execute(sessionFactory, session -> session.save(expected));

        int id = expected.getId();

        User actual = TransactionalExecutor.execute(sessionFactory ,session -> session.get(User.class, id));

        assertEquals(expected, actual);
    }
}