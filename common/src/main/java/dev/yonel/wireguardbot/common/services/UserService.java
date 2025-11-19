package dev.yonel.wireguardbot.common.services;

import java.util.Optional;

import dev.yonel.wireguardbot.common.dtos.UserDto;
import dev.yonel.wireguardbot.common.exceptions.DuplicateUserException;
import dev.yonel.wireguardbot.common.exceptions.NotExistsException;

public interface UserService {

    Optional<UserDto> createUser(UserDto user) throws DuplicateUserException,IllegalArgumentException, InternalError;

    Optional<UserDto> getUser(Long id) throws IllegalArgumentException;

    Optional<UserDto> getUserByUserId(Long userId) throws IllegalArgumentException;

    Optional<UserDto> updateUser(UserDto user) throws NotExistsException;

    void deleteUser(UserDto user);

    boolean userExistsByUserId(Integer userId);
}
