package com.example.userservice.config;

import com.example.userservice.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import static com.example.userservice.app.UserConsoleApp.logger;

/**
 * Класс {@code HibernateUtil}  представляет собой фабрику сессий
 * Используется для конфигурации и получения сессий взаимодействия с базой данных.
 * @author vmarakushin
 * @version 1.0
 */

public class HibernateUtil {

    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            return new Configuration()
                    .configure("hibernate.cfg.xml")
                    .addAnnotatedClass(User.class)
                    .buildSessionFactory();
        } catch (Throwable ex) {
            logger.warn("HIBERNATE_UTIL: Ошибка инициализации SessionFactory: " + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        getSessionFactory().close();
    }
}