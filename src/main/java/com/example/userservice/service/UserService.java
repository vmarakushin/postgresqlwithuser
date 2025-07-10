package com.example.userservice.service;


import com.example.userservice.dto.RequestUserDTO;
import com.example.userservice.dto.UserDTO;
import com.example.userservice.exception.RepositoryException;
import com.example.userservice.exception.UserServiceException;
import com.example.userservice.mapper.UserMapper;
import com.example.userservice.model.User;
import com.example.userservice.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * Сервис для общения с базой данных
 *
 * @author vmarakushin
 * @version 1.0
 */
@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final Validator validator;
    private final Logger logger;


    public UserService(UserRepository userRepository, UserMapper userMapper, Validator validator) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.validator = validator;
        this.logger = LoggerFactory.getLogger(UserService.class);
    }


    /**
     * Метод создания пользователя
     * Проверит данные на валидность перед созданием
     * Проверит, что id == 0
     *
     * @param dto данные пользователя
     */
    @Transactional
    public void createUser(UserDTO dto) {

        validation(dto, Scope.CREATE);

        User user = userMapper.toEntity(dto);
        try {
            userRepository.save(user);

        } catch (Exception e) {
            logger.error("Ошибка репозитория. \n", e);
            throw new RepositoryException("Ошибка при обращении к репозиторию.");
        }

    }


    /**
     * Метод получения данных по ID
     * Проверит, что ID > 0
     *
     * @param dto RequestUserDTO с ID
     * @return Optional с данными в UserDTO
     */
    @Transactional(readOnly = true)
    public Optional<UserDTO> getUserById(RequestUserDTO dto) {

        validation(dto);

        try {
            return userRepository.findById(dto.getId())
                    .map(userMapper::toDto);

        } catch (Exception e) {
            logger.error("Ошибка репозитория. \n", e);
            throw new RepositoryException("Ошибка при обращении к репозиторию.");
        }

    }


    /**
     * Метод получения всех данных пользователей
     *
     * @return лист данных всех пользователей
     */
    @Transactional(readOnly = true)
    public List<UserDTO> getAllUsers() {

        try {
            return userRepository.findAll().stream()
                    .map(userMapper::toDto)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            logger.error("Ошибка репозитория. \n", e);
            throw new RepositoryException("Ошибка при обращении к репозиторию.");
        }
    }


    /**
     * Метод обновления данных пользователя
     * Проверит данные на валидность перед обновлением
     * Проверит, что id > 0
     *
     * @param dto обновленные данные пользователя
     */
    @Transactional
    public void updateUser(UserDTO dto) {

        validation(dto, Scope.UPDATE);

        try {
            userRepository.save(userMapper.toEntity(dto));
            userRepository.flush();

        } catch (Exception e) {
            logger.error("Ошибка репозитория. \n", e);
            throw new RepositoryException("Ошибка при обращении к репозиторию.");
        }

    }


    /**
     * Метод удаления пользователя
     * Проверит, что ID > 0
     *
     * @param dto UserRequestDTO с ID
     */
    @Transactional
    public void deleteUser(RequestUserDTO dto) {

        validation(dto);

        try {
            userRepository.deleteById(dto.getId());
            userRepository.flush();
        } catch (Exception e) {
            logger.error("Ошибка репозитория. \n", e);
            throw new RepositoryException("Ошибка при обращении к репозиторию.");
        }


    }


    /**
     * Валидация для ID при получении и удалении пользователя
     *
     * @param dto UserRequestDTO с ID
     */
    private void validation(RequestUserDTO dto) {
        if (dto.getId() < 1L) {
            String message = "Id должен быть больше 0";
            logger.warn(message);
            throw new IllegalArgumentException(message);
        }
    }


    private enum Scope {
        CREATE, UPDATE
    }

    /**
     * Валидация данных
     * Scope.CREATE - проверка ID == 0 при создании
     * Scope.UPDATE - проверка ID > 0 при обновлении
     * Проверка полей общая для обоих случаев
     *
     * @param dto   UserDTO с данными
     * @param scope применение метода
     */
    private void validation(UserDTO dto, Scope scope) {


        switch (scope) {


            case CREATE -> {
                if (dto.getId() != 0L) {
                    String message = "Id должен быть равен 0";
                    logger.warn(message);
                    throw new IllegalArgumentException(message);
                }
            }


            case UPDATE -> {
                if (dto.getId() < 1L) {
                    String message = "Id должен быть больше 0";
                    logger.warn(message);
                    throw new IllegalArgumentException(message);
                }
            }
        }


        try {
            validator.name(dto.getName());
            validator.surname(dto.getSurname());
            validator.age(dto.getAge());
            validator.phone(dto.getPhone());
            validator.email(dto.getEmail());
        } catch (IllegalArgumentException e) {
            logger.warn(e.getMessage());
            throw e;
        }


        if (userRepository.existsByEmailAndIdNot(dto.getEmail(), dto.getId())) {
            String message = "Этот email уже используется!";
            logger.warn(message);
            throw new UserServiceException(message);
        }


        if (userRepository.existsByPhoneAndIdNot(dto.getPhone(), dto.getId())) {
            String message = "Этот телефон уже используется!";
            logger.warn(message);
            throw new UserServiceException(message);
        }
    }
}