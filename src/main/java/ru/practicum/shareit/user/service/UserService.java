package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;
import java.util.Optional;

public interface UserService {
    Collection<UserDto> findAllUsers();

    Optional<UserDto> findUserById(Long userId);

    UserDto createUser(User user);

    UserDto updateUser(Long userId, User user);

    void deleteUserById(Long userId);
}
