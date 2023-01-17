package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {
    private final UserClient userClient;


    @PostMapping
    public ResponseEntity<Object> createUser(@RequestBody @Valid UserRequestDto requestDto) {
        log.info("Получен запрос Post /users . Создать пользователя: {}.", requestDto);
        return userClient.createUser(requestDto);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> findUserById(@PathVariable @Positive Long userId) {
        log.info("Получен запрос Get /users/{} . Найти пользователя по userId {}.", userId, userId);
        return userClient.findUserById(userId);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> updateUser(@RequestBody UserRequestDto requestDto,
                                             @PathVariable @Positive Long userId) {
        log.info("Получен запрос Patch /users/{} . Обновить данные пользователя: {}.", userId, requestDto);
        return userClient.updateUser(userId, requestDto);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUserById(@PathVariable @Positive Long userId) {
        log.info("Получен запрос Delete /users/{} . Удалить пользователя имеющего userId {}.", userId, userId);
        return userClient.deleteUserById(userId);
    }

    @GetMapping
    public ResponseEntity<Object> findAllUsers() {
        log.info("Получен запрос Get /users . Получить всех пользователей.");
        return userClient.findAllUsers();
    }

}
