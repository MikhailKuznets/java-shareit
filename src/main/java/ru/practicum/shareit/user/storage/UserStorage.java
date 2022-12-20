package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.model.User;

import java.util.Collection;

public interface UserStorage {

    Boolean isContainsUserId(Long userId);

    Collection<User> getAllUsers();

    User getUserById(Long userId);

    User createUser(User user);

    User updateUser(Long userId, User user);

    void deleteUser(Long userId);
}
