package dev.yonel.wireguardbot.common.services;

import dev.yonel.wireguardbot.common.dtos.UserDto;

public interface UserService {

    UserDto createUser(UserDto user);

    UserDto getUser();

    UserDto updateUser();

    boolean deleteUser();

}
