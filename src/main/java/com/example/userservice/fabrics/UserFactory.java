package com.example.userservice.fabrics;

import com.example.userservice.model.User;

import java.util.Date;


public class UserFactory {
    private UserFactory() {
    }

    public static User createUser(String name, String surname, int age, String phone, String email) {
        return createUser(name, surname, age, phone, email, false);
    }


    public static User createUser(String name, String surname, int age, String phone, String email, Boolean show) {
        User user = User.builder()
                .name(name)
                .surname(surname)
                .age(age)
                .phone(phone)
                .email(email)
                .createdAt(new Date())
                .build();
        if (show) System.out.println(user.toString() + "\n\n");
        return user;
    }
}
