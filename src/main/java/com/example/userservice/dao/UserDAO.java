package com.example.userservice.dao;

import com.example.userservice.exception.DaoException;
import com.example.userservice.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Класс {@code UserDAO} представляет собой сервис для взаимодействия с базой данных
 *
 *  @author vmarakushin
 * @version 1.1
 */
public class UserDAO {
    private final static Logger logger = LoggerFactory.getLogger(UserDAO.class);

    /**
     * Сохраняет переданного пользователя в базу данных
     *
     * @param user пользователь для сохранения
     */
    public static void saveUser(User user){
        try {
            TransactionalExecutor.execute(session -> {
                        session.save(user);
                        return null;
            });
        }catch(Exception e){
            throw new DaoException("Создать пользователя не удалось");
            }


    }

    /**
     * Возвращает пользователя по указанному ID
     *
     * @param id айди искомого пользователя
     * @return искомый пользователь
     */
    public static User getUserById(int id) {
        try {
            return TransactionalExecutor.execute(session -> session.get(User.class, id));
        }catch(DaoException e){
            throw new DaoException("Получить пользователя не удалось");
        }


    }

    /**
     * Возвращает список всех пользователей
     *
     * @return список всех пользователей
     */
    public static List<User> getAllUsers() {
        try {
            return TransactionalExecutor.execute(session -> session.createQuery("from User").list());
        }catch (DaoException e){
            throw new DaoException("Получить пользователей не удалось");
        }
    }

    /**
     * Обновляет указанного пользователя
     *
     * @param user пользователь с обновленными данными
     */
    public static void updateUser(User user) {
        try {
            TransactionalExecutor.execute(session -> {
                session.update(user);
                return null;
            });
        }catch(DaoException e){
            throw new DaoException("Обновить пользователя не удалось");
        }
    }

    /**
     * Удаляет пользователя с указанным ID
     *
     * @param id ID пользователя для удаления
     */
    public static void deleteUser(int id) {
        try {
            TransactionalExecutor.execute(session -> {
                User user = session.get(User.class, id);
                if (user != null) {
                    session.delete(user);
                }
                return user;
            });
        }catch(DaoException e){
            throw new DaoException("Удалить пользователя не удалось");
        }
    }
}