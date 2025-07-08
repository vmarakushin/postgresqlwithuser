package com.example.userservice.config;

import com.example.userservice.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Класс {@code HibernateUtil} поставщик фабрики сессий
 * Используется для конфигурации и получения сессий взаимодействия с базой данных.
 *
 * @author vmarakushin
 * @version 1.0
 */

public class HibernateUtil {

    private HibernateUtil() {
    }

    private static final Logger logger = LoggerFactory.getLogger(HibernateUtil.class);
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            return new Configuration()
                    .configure("hibernate.cfg.xml")
                    .addAnnotatedClass(User.class)
                    .buildSessionFactory();
        } catch (Throwable ex) {
            logger.warn("Ошибка инициализации SessionFactory: " + ex);
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