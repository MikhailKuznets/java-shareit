package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exceptions.InvalidIdException;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.Collection;

/**
 * TODO Sprint add-controllers.
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public Collection<User> findAllUsers() {
        log.debug("Получен запрос Get /users. Получить всех пользователей.");
        return userService.findAllUsers();
    }

    @GetMapping("/{userId}")
    public User findUserById(@PathVariable @Positive Long userId) {
        log.debug("Получен запрос Get /users/{}. Найти пользователя по userId {}.", userId, userId);
        return userService.findUserById(userId).orElseThrow(
                () -> new InvalidIdException("К сожалению, пользователя с id " + userId + " нет."));
    }

    @PostMapping
    public User createUser(@RequestBody @Valid User user) {
        log.debug("Получен запрос Post /users. Создать пользователя {}.", user);
        return userService.createUser(user);
    }

    @PatchMapping("/{userId}")
    public User updateUser(@RequestBody User user, @PathVariable @Positive Long userId) {
        log.debug("Получен запрос Patch /users/{}. Обновить данные пользователя {}.", userId, user);
        return userService.updateUser(userId, user);
    }

    @DeleteMapping("/{userId}")
    public void deleteUserById(@PathVariable Long userId) {
        log.debug("Получен запрос Delete /users/{}. Удалить пользователя по userId {}.", userId, userId);
        userService.findUserById(userId).orElseThrow(
                () -> new InvalidIdException("К сожалению, пользователя с id " + userId + " нет."));
        userService.deleteUserById(userId);
    }
}
