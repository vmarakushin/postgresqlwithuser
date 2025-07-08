package com.example.userservice.dao;

import com.example.userservice.model.User;

import java.util.List;

/**
 * Класс {@code UserDAO} представляет собой сервис для взаимодействия с базой данных
 * Класс {@link TransactionalExecutor} - для исполнения однообразных участков кода
 * Можно подставить любой TransactionalExecutor, по умолчанию возьмет TransactionExecutor с HibernateUtil
 *
 * @author vmarakushin
 * @version 2.0
 */
public class UserDAO {

    private final  TransactionalExecutor transactionalExecutor;


    public UserDAO() {
        this(new TransactionalExecutor());
    }
    public UserDAO(TransactionalExecutor transactionalExecutor) {
        this.transactionalExecutor = transactionalExecutor;
    }

    /**
     * Сохраняет переданного пользователя в базу данных
     *
     * @param user пользователь для сохранения
     */
    public void saveUser(User user) {
        transactionalExecutor.execute(session -> {
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
    public User getUserById(int id) {
        return transactionalExecutor.execute(session -> session.get(User.class, id));
    }

    /**
     * Возвращает список всех пользователей
     *
     * @return список всех пользователей
     */
    public List<User> getAllUsers() {
        return transactionalExecutor.execute(session -> session.createQuery("from User").list());
    }

    /**
     * Обновляет указанного пользователя
     *
     * @param user пользователь с обновленными данными
     */
    public void updateUser(User user) {
        transactionalExecutor.execute(session -> {
            session.update(user);
            return null;
        });
    }

    /**
     * Удаляет пользователя с указанным ID
     *
     * @param id ID пользователя для удаления
     */
    public void deleteUser(int id) {
        transactionalExecutor.execute(session -> {
            User user = session.get(User.class, id);
            if (user != null) {
                session.delete(user);
            }
            return user;
        });
    }
}