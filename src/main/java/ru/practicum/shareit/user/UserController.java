package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exceptions.InvalidIdException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.Collection;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping(path = "/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public Collection<UserDto> findAllUsers() {
        log.info("Получен запрос Get /users. Получить всех пользователей.");
        return userService.findAllUsers();
    }

    @GetMapping("/{userId}")
    public UserDto findUserById(@PathVariable @Positive Long userId) {
        log.info("Получен запрос Get /users/{}. Найти пользователя по userId {}.", userId, userId);
        return userService.findUserById(userId).orElseThrow(
                () -> new InvalidIdException("К сожалению, пользователя с id " + userId + " нет."));
    }

    @PostMapping
    public UserDto createUser(@RequestBody @Valid User user) {
        log.info("Получен запрос Post /users. Создать пользователя {}.", user);
        return userService.createUser(user);
    }

    @PatchMapping("/{userId}")
    public UserDto updateUser(@RequestBody User user,
                              @PathVariable @Positive Long userId) {
        log.info("Получен запрос Patch /users/{}. Обновить данные пользователя {}.", userId, user);
        return userService.updateUser(userId, user);
    }

    @DeleteMapping("/{userId}")
    public void deleteUserById(@PathVariable @Positive Long userId) {
        log.info("Получен запрос Delete /users/{}. Удалить пользователя по userId {}.", userId, userId);
        userService.findUserById(userId).orElseThrow(
                () -> new InvalidIdException("К сожалению, пользователя с id " + userId + " нет."));
        userService.deleteUserById(userId);
    }
}
