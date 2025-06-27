package com.example.userservice.dao;

import com.example.userservice.model.User;

import java.util.List;

/**
 * Класс {@code UserDAO} представляет собой сервис для взаимодействия с базой данных
 *
 *  @author vmarakushin
 * @version 1.1
 */
public class UserDAO {


    /**
     * Сохраняет переданного пользователя в базу данных
     *
     * @param user пользователь для сохранения
     */
    public static void saveUser(User user){
            TransactionalExecutor.execute(session -> {
                        session.save(user);
                        return null;
            });
    }

    /**
     * Возвращает пользователя по указанному ID
     *
     * @param id айди искомого пользователя
     * @return искомый пользователь
     */
    public static User getUserById(int id) {
            return TransactionalExecutor.execute(session -> session.get(User.class, id));
    }

    /**
     * Возвращает список всех пользователей
     *
     * @return список всех пользователей
     */
    public static List<User> getAllUsers() {
            return TransactionalExecutor.execute(session -> session.createQuery("from User").list());
    }

    /**
     * Обновляет указанного пользователя
     *
     * @param user пользователь с обновленными данными
     */
    public static void updateUser(User user) {
        TransactionalExecutor.execute(session -> {
            session.update(user);
            return null;
        });
    }
    /**
     * Удаляет пользователя с указанным ID
     *
     * @param id ID пользователя для удаления
     */
    public static void deleteUser(int id) {
        TransactionalExecutor.execute(session -> {
            User user = session.get(User.class, id);
            if (user != null) {
                session.delete(user);
            }
            return user;
        });
    }
}