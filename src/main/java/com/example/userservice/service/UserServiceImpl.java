package com.example.userservice.service;


import com.example.userservice.dto.RequestUserDTO;
import com.example.userservice.dto.UserDTO;
import com.example.userservice.exception.RepositoryException;
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
 * @version 2.0
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final Validator validator;
    private final Logger logger;

    private static final String REPOSITORY_EXCEPTION_MESSAGE = "Ошибка при обращении к репозиторию.";


    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, Validator validator) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.validator = validator;
        this.logger = LoggerFactory.getLogger(UserServiceImpl.class);
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

        validator.fullValidation(dto, Validator.Scope.CREATE);

        User user = userMapper.toEntity(dto);
        try {
            userRepository.save(user);
        } catch (Exception e) {
            logger.error(REPOSITORY_EXCEPTION_MESSAGE, e);
            throw new RepositoryException(REPOSITORY_EXCEPTION_MESSAGE);
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

        validator.id(dto.getId());

        try {
            return userRepository.findById(dto.getId())
                    .map(userMapper::toDto);

        } catch (Exception e) {
            logger.error(REPOSITORY_EXCEPTION_MESSAGE, e);
            throw new RepositoryException(REPOSITORY_EXCEPTION_MESSAGE);
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
            logger.error(REPOSITORY_EXCEPTION_MESSAGE, e);
            throw new RepositoryException(REPOSITORY_EXCEPTION_MESSAGE);
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

        validator.fullValidation(dto, Validator.Scope.UPDATE);

        try {
            userRepository.save(userMapper.toEntity(dto));
            userRepository.flush();
        } catch (Exception e) {
            logger.error(REPOSITORY_EXCEPTION_MESSAGE, e);
            throw new RepositoryException(REPOSITORY_EXCEPTION_MESSAGE);
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

        validator.id(dto.getId());

        try {
            userRepository.deleteById(dto.getId());
            userRepository.flush();
        } catch (Exception e) {
            logger.error(REPOSITORY_EXCEPTION_MESSAGE, e);
            throw new RepositoryException(REPOSITORY_EXCEPTION_MESSAGE);
        }
    }
}