package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;

public interface UserService {
    Collection<UserDto> findAllUsers();

    UserDto findUserById(Long userId);

    UserDto createUser(User user);

    UserDto updateUser(Long userId, User user);

    void deleteUserById(Long userId);
}
