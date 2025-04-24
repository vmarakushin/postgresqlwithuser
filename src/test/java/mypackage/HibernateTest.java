package mypackage;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateTest {

    public static void main(String[] args) {
        // Создание сессии Hibernate
        SessionFactory factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(User.class)
                .buildSessionFactory();

        Session session = factory.getCurrentSession();

        try {
            // Создание нового пользователя
            User newUser = new User("Vasya", "Petrov", 28, "+79991118822", "vasyapetrov@mail.lalal");

            // Начало транзакции
            session.beginTransaction();

            // Сохранение пользователя
            session.save(newUser);

            // Завершение транзакции
            session.getTransaction().commit();

            // Извлечение пользователя из базы данных
            session = factory.getCurrentSession();
            session.beginTransaction();

            // Извлечение пользователя с id = 1
            User retrievedUser = session.get(User.class, newUser.getId());

            System.out.println("Retrieved User: " + retrievedUser);

            // Завершение транзакции
            session.getTransaction().commit();
        } finally {
            factory.close();
        }
    }
}