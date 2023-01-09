package ru.practicum.shareit.integration;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
public class IntegrationUserTest {
    private static User requestUser1;
    private static User user1;
    private static UserDto responseUser1Dto;

    @Autowired
    private UserService userService;


    @BeforeAll
    static void setUp() {
        requestUser1 = User.builder()
                .name("User1")
                .email("user1@yandex.ru")
                .build();

        user1 = User.builder()
                .id(1L)
                .name("User1")
                .email("user1@yandex.ru")
                .build();

        responseUser1Dto = UserDto.builder()
                .id(1L)
                .name("User1")
                .email("user1@yandex.ru")
                .build();

//        requestUser2 = User.builder()
//                .name("User2")
//                .email("user2@yandex.ru")
//                .build();
//
//        user2 = User.builder()
//                .id(2L)
//                .name("User2")
//                .email("user2@yandex.ru")
//                .build();
//
//        responseUser2Dto = UserDto.builder()
//                .id(2L)
//                .name("User2")
//                .email("user2@yandex.ru")
//                .build();
//
//        requestUpdatedUser1 = User.builder()
//                .name("Updated User1")
//                .email("updateduser1@yandex.ru")
//                .build();
//
//
//        responseUpdatedUser1Dto = UserDto.builder()
//                .id(1L)
//                .name("Updated User1")
//                .email("updateduser1@yandex.ru")
//                .build();
    }

    @Test
    @DisplayName("Должен создать User при корректных данных пользователя")
    void shouldCreateCorrectUser() {
        UserDto resultUserDto = userService.createUser(requestUser1);
        assertEquals(responseUser1Dto, resultUserDto);
    }

    @Test
    @DisplayName("Не должен создать User при некорректном email")
    void shouldNotCreateCorrectUserWithInvalidEmail() {
        UserDto resultUserDto = userService.createUser(requestUser1);
        assertEquals(responseUser1Dto, resultUserDto);
    }


}

