package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.controller.UserController;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
class UserControllerUnitTest {
    @MockBean
    private UserService userService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private User requestUser1;
    private UserDto responseUser1;


    @BeforeEach
    void setUp() {
        requestUser1 = User.builder()
                .name("User1")
                .email("user1@yandex.ru")
                .build();

        responseUser1 = UserDto.builder()
                .id(1L)
                .name("User1")
                .email("user1@yandex.ru")
                .build();
    }


    @Test
    @DisplayName("Должен создать User при корректных данных")
    void shouldCreateCorrectUser() throws Exception {
        when(userService.createUser(requestUser1)).thenReturn(responseUser1);

        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(requestUser1))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(responseUser1)));
    }

    @Test
    @DisplayName("Должен удалить User при корректном userId")
    void shouldDeleteUserByCorrectId() throws Exception {
        Long userId = 1L;

        mockMvc.perform(delete("/users/{userId}", userId))
                .andExpect(status().isOk());
        verify(userService, times(1)).deleteUserById(userId);
    }

    @Test
    @DisplayName("Должен вернуть User при корректном userId")
    void shouldFindUserByCorrectId() throws Exception {
        Long userId = 1L;

        mockMvc.perform(get("/users/{userId}", userId))
                .andExpect(status().isOk());
        verify(userService).findUserById(userId);
    }

    @Test
    @DisplayName("Должен обновить User при корректных данных и корректном userId")
    void shouldUpdateUserByCorrectId() throws Exception {
        Long userId = 1L;
        when(userService.updateUser(anyLong(), any(User.class))).thenReturn(responseUser1);

        mockMvc.perform(patch("/users/{userId}", userId)
                        .content(objectMapper.writeValueAsString(requestUser1))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(responseUser1)));
    }

    @Test
    @DisplayName("Должен вернуть список User'ов данных")
    void shouldFindAllUsers() throws Exception {
        UserDto responseUser2 = UserDto.builder()
                .id(2L)
                .name("User2")
                .email("user2@yandex.ru")
                .build();

        List<UserDto> users = new ArrayList<>();
        users.add(responseUser1);
        users.add(responseUser2);

        when(userService.findAllUsers()).thenReturn(users);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(users)));

    }

}