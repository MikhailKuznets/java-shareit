package ru.practicum.shareit.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.exceptions.InvalidIdException;
import ru.practicum.shareit.user.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    default User validateUser(Long userId) {
        return findById(userId).orElseThrow(() -> {
            throw new InvalidIdException("Пользователя с id = " + userId + " не существует");
        });
    }
}
