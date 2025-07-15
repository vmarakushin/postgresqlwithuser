package com.example.userservice.service;

import com.example.userservice.dto.RequestUserDTO;
import com.example.userservice.dto.UserDTO;

import java.util.List;
import java.util.Optional;


/**
 * Маркерный интерфейс
 *
 * @author vmarakushin
 * @version 1.0
 */
public interface UserService {
    void createUser(UserDTO dto);

    List<UserDTO> getAllUsers();

    Optional<UserDTO> getUserById(RequestUserDTO requestUserDTO);

    void updateUser(UserDTO userDTO);

    void deleteUser(RequestUserDTO requestUserDTO);
}
