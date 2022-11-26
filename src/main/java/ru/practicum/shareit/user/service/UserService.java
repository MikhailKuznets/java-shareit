package ru.practicum.shareit.user.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.User;

import java.util.Collection;
import java.util.Optional;

public interface UserService {
    Collection<User> findAllUsers();

    Optional<User> findUserById(Long userId);

    User createUser(User user);

    User updateUser(Long userId, User user);

    void deleteUserById(Long userId);
}
