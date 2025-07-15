package com.example.userservice.repository;

import com.example.userservice.model.User;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Тесты для Repository слоя
 *
 * @author vmarakushin
 * @version 1.0
 */
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @DisplayName("Проверка записи в БД")
    @Test
    public void testSaveUser() {
        User user = User.builder()
                .id(1L)
                .name("John")
                .surname("Smith")
                .age(35)
                .phone("+78005553535")
                .email("luchshe@pozvonit.chemukogotozanomat")
                .money(999_999_999_999L)
                .build();
        userRepository.save(user);

        Map<String, Object> row = jdbcTemplate.queryForMap("SELECT * FROM users WHERE id = 1");


        assertEquals(user.getName(), row.get("name"));
        assertEquals(user.getSurname(), row.get("surname"));
        assertEquals(user.getAge(), row.get("age"));
        assertEquals(user.getPhone(), row.get("phone"));
        assertEquals(user.getEmail(), row.get("email"));
        assertEquals(user.getMoney(), ((Number) row.get("money")).longValue());
    }

    @DisplayName("Проверка получения из БД по айди")
    @Sql(statements = """
    INSERT INTO users (id, name, surname, age, phone, email, money)
    VALUES (1, 'John', 'Smith', 35, '+78005553535', 'luchshe@pozvonit.chemukogotozanomat', 999999999999);
    """)
    @Test
    public void testGetUser() {
        User expected = User.builder()
                .id(1L)
                .name("John")
                .surname("Smith")
                .age(35)
                .phone("+78005553535")
                .email("luchshe@pozvonit.chemukogotozanomat")
                .money(999_999_999_999L)
                .build();

        User actual = userRepository.findById(1L).get();

        assertEquals(expected, actual);
    }


    @DisplayName("Проверка получения несуществующего юзера")
    @Test
    public void testGetUserNotFound() {
        Optional actual = userRepository.findById(2L);
        assertTrue(actual.isEmpty());
    }



    @DisplayName("Проверка метода получения всех юзеров")
    @Sql(statements = """
    INSERT INTO users (id, name, surname, age, phone, email, money)
    VALUES(1, 'John', 'Smith', 35, '+78005553535', 'luchshe@pozvonit.chemukogotozanomat', 999999999999),
      (2,'Vasya','Petrov',25,'+71111111111','vasya@petrov.com',1000),
      (3, 'Petr', 'Ivanov', 76, '+72222222222', 'petr@ivanov.com', 320),
      (4, 'Sam', 'Moyer', 43, '+73333333333', 'sam@moyaer.com', 65536);
    """)
    @Test
    public void testGetAllUsers() {

        User user0 = User.builder()
                .id(1L)
                .name("John")
                .surname("Smith")
                .age(35)
                .phone("+78005553535")
                .email("luchshe@pozvonit.chemukogotozanomat")
                .money(999_999_999_999L)
                .build();

        User user1 = User.builder()
                .id(2L)
                .name("Vasya")
                .surname("Petrov")
                .age(25)
                .phone("+71111111111")
                .email("vasya@petrov.com")
                .money(1000L)
                .build();

        User user2 = User.builder()
                .id(3L)
                .name("Petr")
                .surname("Ivanov")
                .age(76)
                .phone("+72222222222")
                .email("petr@ivanov.com")
                .money(320)
                .build();

        User user3 = User.builder()
                .id(4L)
                .name("Sam")
                .surname("Moyer")
                .age(43)
                .phone("+73333333333")
                .email("sam@moyaer.com")
                .money(65536)
                .build();


        List<User> users = userRepository.findAll();

        assertEquals(4, users.size());
        assertEquals(user0, users.get(0));
        assertEquals(user1, users.get(1));
        assertEquals(user2, users.get(2));
        assertEquals(user3, users.get(3));
    }

    @DisplayName("Проверка обновления юзера")
    @Sql(statements = """
    INSERT INTO users (id, name, surname, age, phone, email, money)
    VALUES (1, 'John', 'Smith', 35, '+78005553535', 'luchshe@pozvonit.chemukogotozanomat', 999999999999),
       (2,'Vasya','Petrov',25,'+71111111111','vasya@petrov.com',1000);
    """)
    @Test
    public void testUpdateUser() {

        User expected = User.builder()
                .id(2L)
                .name("Valentin")
                .surname("Dunaev")
                .age(36)
                .phone("+72222222222")
                .email("nezanimaite@zhivite.posredstvam")
                .money(0)
                .build();

        userRepository.save(expected);
        userRepository.flush();

        Map<String, Object> row = jdbcTemplate.queryForMap("SELECT * FROM users WHERE id = 2");

        assertEquals(expected.getName(), row.get("name"));
        assertEquals(expected.getSurname(), row.get("surname"));
        assertEquals(expected.getAge(), row.get("age"));
        assertEquals(expected.getPhone(), row.get("phone"));
        assertEquals(expected.getEmail(), row.get("email"));
        assertEquals(expected.getMoney(), ((Number) row.get("money")).longValue());
    }

    @DisplayName("Проверка удаления юзера")
    @Sql(statements = """
    INSERT INTO users (id, name, surname, age, phone, email, money)
    VALUES (1, 'John', 'Smith', 35, '+78005553535', 'luchshe@pozvonit.chemukogotozanomat', 999999999999);
    """)
    @Test
    public void testDeleteUser() {

        userRepository.deleteById(1L);
        userRepository.flush();

        assertThrows(EmptyResultDataAccessException.class, () -> jdbcTemplate.queryForMap("SELECT * FROM users WHERE id = 1"));
    }

    @DisplayName("Проверка проверки email на уникальность")
    @Sql(statements = """
    INSERT INTO users (id, name, surname, age, phone, email, money)
    VALUES (1, 'John', 'Smith', 35, '+78005553535', 'luchshe@pozvonit.chemukogotozanomat', 999999999999);
    """)
    @Test
    public void testCheckEmail() {
        assertTrue(userRepository.existsByEmailAndIdNot("luchshe@pozvonit.chemukogotozanomat",2));
    }

    @DisplayName("Проверка проверки email на уникальность для того же юзера")
    @Sql(statements = """
    INSERT INTO users (id, name, surname, age, phone, email, money)
    VALUES (1, 'John', 'Smith', 35, '+78005553535', 'luchshe@pozvonit.chemukogotozanomat', 999999999999);
    """)
    @Test
    public void testCheckEmailSameUser() {
        assertFalse(userRepository.existsByEmailAndIdNot("luchshe@pozvonit.chemukogotozanomat",1));
    }

    @DisplayName("Проверка неуникального email")
    @Sql(statements = """
    INSERT INTO users (id, name, surname, age, phone, email, money)
    VALUES (1, 'John', 'Smith', 35, '+78005553535', 'luchshe@pozvonit.chemukogotozanomat', 999999999999);
    """)
    @Test
    public void testCheckEmailNotUnique() {
        assertFalse(userRepository.existsByEmailAndIdNot("abc@hhh.fff",2));
    }

    @DisplayName("Проверка проверки phone на уникальность")
    @Sql(statements = """
    INSERT INTO users (id, name, surname, age, phone, email, money)
    VALUES (1, 'John', 'Smith', 35, '+78005553535', 'luchshe@pozvonit.chemukogotozanomat', 999999999999);
    """)
    @Test
    public void testCheckPhone() {
        assertTrue(userRepository.existsByPhoneAndIdNot("+78005553535",2));
    }

    @DisplayName("Проверка проверки phone на уникальность для того же юзера")
    @Sql(statements = """
    INSERT INTO users (id, name, surname, age, phone, email, money)
    VALUES (1, 'John', 'Smith', 35, '+78005553535', 'luchshe@pozvonit.chemukogotozanomat', 999999999999);
    """)
    @Test
    public void testCheckPhoneSameUser() {
        assertFalse(userRepository.existsByPhoneAndIdNot("+78005553535",1));
    }

    @DisplayName("Проверка неуникального phone")
    @Sql(statements = """
    INSERT INTO users (id, name, surname, age, phone, email, money)
    VALUES (1, 'John', 'Smith', 35, '+78005553535', 'luchshe@pozvonit.chemukogotozanomat', 999999999999);
    """)
    @Test
    public void testCheckPhoneNotUnique() {
        assertFalse(userRepository.existsByPhoneAndIdNot("+71111111111",2));
    }
}
