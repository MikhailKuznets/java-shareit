package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;

    @Override
    public Collection<User> findAllUsers() {
        return userStorage.getAllUsers();
    }

    @Override
    public Optional<User> findUserById(Long userId) {
        return userStorage.findUserById(userId);
    }

    @Override
    public User createUser(User user) {
        return userStorage.createUser(user);
    }

    @Override
    public User updateUser(Long userId, User user) {
        return userStorage.updateUser(userId, user);
    }

    @Override
    public void deleteUserById(Long userId) {
        userStorage.deleteUser(userId);
    }
}
