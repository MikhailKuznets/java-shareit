package ru.practicum.shareit.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.practicum.shareit.TestUtility;
import ru.practicum.shareit.user.controller.UserController;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//Вебинар
@AutoConfigureMockMvc
@WebMvcTest({UserController.class})
class UserControllerUnitTest {
    private static final String PATH = "/users";
    private static final String PATH_WITH_ID = "/users/1";
    private static final Long USER_1_ID = 1L;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private UserService userService;

    private User requestUser1;
    private UserDto responseUser1;


    @BeforeEach
    void setUp() {
        requestUser1 = TestUtility.getUser();

        responseUser1 = TestUtility.getUserDto();
    }


    @Test
    @DisplayName("Должен создать User при корректных данных")
    void shouldCreateCorrectUser() throws Exception {
        when(userService.createUser(any(User.class))).thenReturn(responseUser1);

        mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestUser1))
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(
                        objectMapper.writeValueAsString(responseUser1)
                ));

        verify(userService, times(1)).createUser(requestUser1);
    }

    @Test
    @DisplayName("Должен удалить User при корректном userId")
    void shouldDeleteUserByCorrectId() throws Exception {
        // Не нашел в виде: when(userService.deleteUserById(any())).***  такого не существует?;
        doNothing().when(userService).deleteUserById(anyLong());

        mockMvc.perform(MockMvcRequestBuilders.delete(PATH_WITH_ID, USER_1_ID)
                )
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(userService, times(1)).deleteUserById(USER_1_ID);
    }

    @Test
    @DisplayName("Должен вернуть User при корректном userId")
    void shouldFindUserByCorrectId() throws Exception {
        when(userService.findUserById(anyLong())).thenReturn(responseUser1);

        mockMvc.perform(MockMvcRequestBuilders.get(PATH_WITH_ID, USER_1_ID)
                )
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(userService, times(1)).findUserById(USER_1_ID);
    }

    @Test
    @DisplayName("Должен обновить User при корректных данных и корректном userId")
    void shouldUpdateUserByCorrectId() throws Exception {
        when(userService.updateUser(anyLong(), any(User.class))).thenReturn(responseUser1);

        mockMvc.perform(MockMvcRequestBuilders.patch(PATH_WITH_ID, USER_1_ID)
                        .content(objectMapper.writeValueAsString(requestUser1))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(
                        objectMapper.writeValueAsString(responseUser1)
                ));

        verify(userService, times(1)).updateUser(USER_1_ID, requestUser1);
    }

    @Test
    @DisplayName("Должен вернуть список User'ов")
    void shouldFindAllUsers() throws Exception {
        UserDto responseUser2 = UserDto.builder()
                .id(2L)
                .name("User2")
                .email("user2@yandex.ru")
                .build();

        List<UserDto> users = List.of(responseUser1, responseUser2);

        when(userService.findAllUsers()).thenReturn(users);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        objectMapper.writeValueAsString(users)
                ));

        verify(userService, times(1)).findAllUsers();
    }

}