package com.example.userservice.repository;

import com.example.userservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Стандартный CRUD репозиторий
 *
 * @author vmarakushin
 * @version 1.0
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {


    /**
     * Для проверки уникальности email
     * ID позволяет избежать бага, когда при обновлении юзера
     * нам укажут на существующий email этого же юзера
     *
     * @param email email для проверки
     * @param id    id пользователя
     * @return есть такой email или нет
     */
    boolean existsByEmailAndIdNot(String email, long id);

    /**
     * Для проверки уникальности phone
     * ID позволяет избежать бага, когда при обновлении юзера
     * нам укажут на существующий phone этого же юзера
     *
     * @param phone phone для проверки
     * @param id    id пользователя
     * @return есть такой phone или нет
     */
    boolean existsByPhoneAndIdNot(String phone, long id);

}