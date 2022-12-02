package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.User;

import java.util.Collection;
import java.util.Optional;

public interface UserStorage {

    Boolean isContainsUserId(Long userId);

    Collection<User> getAllUsers();

    Optional<User> findUserById(Long userId);

    User createUser(User user);

    User updateUser(Long userId, User user);

    void deleteUser(Long userId);
}
