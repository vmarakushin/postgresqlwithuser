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
 * Класс для управления транзакциями.
 * Можно подсунуть любую фабрику сессий в конструктор, по умолчанию возьмет из HibernateUtil
 *
 * @author vmarakushin
 * @version 2.0
 */
public class TransactionalExecutor {

    private final Logger logger = LoggerFactory.getLogger(TransactionalExecutor.class);

    private final SessionFactory sessionFactory;

    public TransactionalExecutor() {
        this(HibernateUtil.getSessionFactory());
    }
    public TransactionalExecutor(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * Выполняет переданную операцию в рамках транзакции, возвращая результат.
     */
    public <R> R execute(Function<Session, R> action) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
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

}
