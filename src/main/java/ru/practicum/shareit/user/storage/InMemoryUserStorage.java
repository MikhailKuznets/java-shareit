package ru.practicum.shareit.user.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exceptions.EmailAlreadyExistException;
import ru.practicum.shareit.user.User;

import java.util.*;

@Repository
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private long currentUserId = 1L;
    private final Map<Long, User> users = new HashMap<>();

    private void validateEmail(String email) {
        if (users.values().stream().map(User::getEmail).anyMatch(email::equals)) {
            throw new EmailAlreadyExistException("Пользователь с данным Email уже существует");
        }
    }

    @Override
    public Boolean isContainsUserId(Long userId) {
        return users.containsKey(userId);
    }

    @Override
    public Collection<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public Optional<User> getUserById(Long userId) {
        if (users.containsKey(userId)) {
            return Optional.of(users.get(userId));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public User createUser(User user) {
        validateEmail(user.getEmail());
        user.setId(currentUserId);
        users.put(currentUserId++, user);
        return user;
    }

    @Override
    public User updateUser(Long userId, User user) {
        User selectedUser = users.get(userId);

        String updatedName = user.getName();
        if (updatedName != null) {
            selectedUser.setName(updatedName);
        }

        String updatedEmail = user.getEmail();
        if (updatedEmail != null) {
            validateEmail(updatedEmail);
            selectedUser.setEmail(updatedEmail);
        }

        users.put(userId, selectedUser);
        return selectedUser;
    }

    @Override
    public void deleteUser(Long userId) {
        users.remove(userId);
    }
}
