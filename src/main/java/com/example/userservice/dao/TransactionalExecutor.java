package com.example.userservice.dao;

import com.example.userservice.config.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.function.Function;

/**
 * Утилитный класс для управления транзакциями.
 * @author ChatGPT
 * @version 1.0
 */
public class TransactionalExecutor {

    /**
     * Выполняет переданную операцию в рамках транзакции, возвращая результат.
     */
    public static <R> R execute(Function<Session, R> action) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            R result = action.apply(session);
            transaction.commit();
            return result;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }

    /**
     * Выполняет переданную операцию без возвращаемого результата.
     */
    public static void executeVoid(Function<Session, Void> action) {
        execute(action);
    }
}
