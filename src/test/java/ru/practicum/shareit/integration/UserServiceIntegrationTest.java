package ru.practicum.shareit.integration;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.TestUtility;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
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

    private static User requestUser;
    private static UserDto expectResponseUser;
    private Collection<UserDto> expectResponseUsers;
    private UserDto responseUser;
    private Collection<UserDto> responseUsers;


    @BeforeAll
    static void setUp() {
        requestUser = TestUtility.getUser1();
        expectResponseUser = TestUtility.getUser1Dto();
        assertNotNull(requestUser);
        assertNotNull(expectResponseUser);
    }

    @Test
    @DisplayName("Должен создать и вернуть User при корректных данных пользователя")
    void shouldCreateCorrectUser() {
        responseUser = userService.createUser(requestUser);
        assertNotNull(responseUser);
        assertEquals(expectResponseUser, responseUser);
    }

    @Test
    @DisplayName("Должен найти и вернуть User при корректнму useId")
    void shouldFindUserByCorrectId() {
        responseUser = userService.createUser(requestUser);
        assertNotNull(responseUser);
        assertEquals(expectResponseUser, responseUser);

        responseUser = userService.findUserById(1L);
        assertNotNull(responseUser);
        assertEquals(expectResponseUser, responseUser);
    }

    @Test
    @DisplayName("Должен вернуть список User'ов")
    void shouldFindAllUsers() {
        responseUsers = userService.findAllUsers();
        assertNotNull(responseUsers);
        assertEquals(0, responseUsers.size());

        responseUser = userService.createUser(requestUser);
        assertNotNull(responseUser);
        assertEquals(expectResponseUser, responseUser);

        expectResponseUsers = List.of(expectResponseUser);
        responseUsers = userService.findAllUsers();
        assertNotNull(responseUsers);
        assertEquals(1, responseUsers.size());
        assertEquals(expectResponseUsers, responseUsers);
    }

    @Test
    @DisplayName("Должен обновить и вернуть User при корректных данных пользователя")
    void shouldUpdateCorrectUser() {
        responseUser = userService.createUser(requestUser);
        assertNotNull(responseUser);
        assertEquals(expectResponseUser, responseUser);

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
        responseUser = userService.createUser(requestUser);
        assertNotNull(responseUser);
        assertEquals(expectResponseUser, responseUser);

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

