package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.Collection;

@RestController
@RequestMapping(path = "/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<Collection<UserDto>> findAllUsers() {
        log.info("Получен запрос Get /users . Получить всех пользователей.");
        return new ResponseEntity<>(userService.findAllUsers(), HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> findUserById(@PathVariable @Positive Long userId) {
        log.info("Получен запрос Get /users/{} . Найти пользователя по userId {}.", userId, userId);
        return new ResponseEntity<>(userService.findUserById(userId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody @Valid User user) {
        log.info("Получен запрос Post /users . Создать пользователя: {}.", user);
        return new ResponseEntity<>(userService.createUser(user), HttpStatus.OK);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(@RequestBody User user,
                                              @PathVariable @Positive Long userId) {
        log.info("Получен запрос Patch /users/{} . Обновить данные пользователя: {}.", userId, user);
        return new ResponseEntity<>(userService.updateUser(userId, user), HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")
    public void deleteUserById(@PathVariable @Positive Long userId) {
        log.info("Получен запрос Delete /users/{} . Удалить пользователя по userId {}.", userId, userId);
        userService.deleteUserById(userId);
    }
}
