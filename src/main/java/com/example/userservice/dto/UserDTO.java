package com.example.userservice.dto;

import lombok.*;


import java.util.Date;


/**
 * @author vmarakushin
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserDTO {
    private long id;
    private String name;
    private String surname;
    private int age;
    private String phone;
    private String email;
    private long money;
    private Date createdAt;
}