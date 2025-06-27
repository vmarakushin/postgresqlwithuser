package com.example.userservice.dao;

import com.example.userservice.config.HibernateUtil;
import com.example.userservice.exception.DaoException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Function;

/**
 * Утилитный класс для управления транзакциями.
 * Можно подсунуть любую фабрику сессий в метод execute()
 *
 * @author ChatGPT
 * @author vmarakushin
 * @version 1.3
 */
public class TransactionalExecutor {
    private static final Logger logger = LoggerFactory.getLogger(TransactionalExecutor.class);
    private static final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    private TransactionalExecutor() {
    }

    /**
     * Выполняет переданную операцию в рамках транзакции, возвращая результат.
     *
     */
    public static <R> R execute (SessionFactory factory, Function<Session, R> action){
        Transaction transaction = null;
        try (Session session = factory.openSession()) {
            transaction = session.beginTransaction();
            R result = action.apply(session);
            transaction.commit();
            return result;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            logger.error(e.getMessage(), e);
            throw new DaoException();
        }
    }
    public static <R> R execute(Function<Session, R> action) {
        return execute(sessionFactory, action);
    }
}
