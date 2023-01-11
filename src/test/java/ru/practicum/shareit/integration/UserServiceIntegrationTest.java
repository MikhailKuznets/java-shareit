package ru.practicum.shareit.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.TestUtility;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest()
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)

public class UserServiceIntegrationTest {
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;


    private User requestUser1;
    private User requestUser2;
    private UserDto savedUser1;
    private UserDto expectResponseUser1;
    private UserDto expectResponseUser2;
    private Collection<UserDto> expectResponseUsers;
    private UserDto responseUser;
    private Collection<UserDto> responseUsers;


    @BeforeEach
    void setUp() {
        requestUser1 = TestUtility.getUser1();
        requestUser2 = TestUtility.getUser2();

        expectResponseUser1 = TestUtility.getUser1Dto();
        expectResponseUser2 = TestUtility.getUser2Dto();

        assertNotNull(requestUser1);
        assertNotNull(requestUser2);
        assertNotNull(expectResponseUser1);
        assertNotNull(expectResponseUser2);
    }

    @Test
    @DisplayName("Должен создать и вернуть User при корректных данных пользователя")
    void shouldCreateCorrectUser() {
        responseUser = userService.createUser(requestUser1);
        assertNotNull(responseUser);
        assertEquals(expectResponseUser1, responseUser);
    }

    @Test
    @DisplayName("Должен найти и вернуть User при корректнму useId")
    void shouldFindUserByCorrectId() {
        responseUser = userService.createUser(requestUser1);
        assertNotNull(responseUser);
        assertEquals(expectResponseUser1, responseUser);

        responseUser = userService.findUserById(1L);
        assertNotNull(responseUser);
        assertEquals(expectResponseUser1, responseUser);
    }

    @Test
    @DisplayName("Должен вернуть список User'ов")
    void shouldFindAllUsers() {
        responseUsers = userService.findAllUsers();
        assertNotNull(responseUsers);
        assertEquals(0, responseUsers.size());

        responseUser = userService.createUser(requestUser1);
        assertNotNull(responseUser);
        assertEquals(expectResponseUser1, responseUser);

        expectResponseUsers = List.of(expectResponseUser1);
        responseUsers = userService.findAllUsers();
        assertNotNull(responseUsers);
        assertEquals(1, responseUsers.size());
        assertEquals(expectResponseUsers, responseUsers);
    }

    @Test
    @DisplayName("Должен обновить и вернуть User при корректных данных пользователя")
    void shouldUpdateCorrectUser() {
        responseUser = userService.createUser(requestUser1);
        assertNotNull(responseUser);
        assertEquals(expectResponseUser1, responseUser);

        User updateUser = User.builder()
                .name("Updated User1 Name")
                .email("updateduser1@yandex.ru")
                .build();

        UserDto responseUpdateUser = UserDto.builder()
                .id(1L)
                .name("Updated User1 Name")
                .email("updateduser1@yandex.ru")
                .build();

        responseUser = userService.updateUser(1L, updateUser);
        assertNotNull(responseUpdateUser);

        responseUser = userService.findUserById(1L);
        assertNotNull(responseUser);
        assertEquals(responseUpdateUser, responseUser);

        expectResponseUsers = List.of(responseUpdateUser);
        responseUsers = userService.findAllUsers();
        assertNotNull(responseUsers);
        assertEquals(1, responseUsers.size());
        assertEquals(expectResponseUsers, responseUsers);

    }

    @Test
    @DisplayName("Должен удалить User при корректных данных пользователя")
    void shouldDeleteCorrectUser() {
        responseUser = userService.createUser(requestUser1);
        assertNotNull(responseUser);
        assertEquals(expectResponseUser1, responseUser);

        expectResponseUsers = List.of(responseUser);
        responseUsers = userService.findAllUsers();
        assertNotNull(responseUsers);
        assertEquals(1, responseUsers.size());
        assertEquals(expectResponseUsers, responseUsers);

        userService.deleteUserById(1L);

        responseUsers = userService.findAllUsers();
        assertEquals(0, responseUsers.size());
        assertEquals(Collections.EMPTY_LIST, responseUsers);
    }

}

