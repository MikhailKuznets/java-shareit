package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exceptions.InvalidIdException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;

    private static User requestUser1;
    private static User user1;
    private static UserDto responseUser1Dto;


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
    }

    @Test
    void shouldCreateCorrectUser() {
        when(userRepository.save(requestUser1)).thenReturn(user1);
        when(userMapper.toUserDto(user1)).thenReturn(responseUser1Dto);

        UserDto resultUser1Dto = userService.createUser(requestUser1);

        assertEquals(responseUser1Dto, resultUser1Dto);
    }

    @Test
    void shouldFindAllUsers() {
        User user2 = User.builder()
                .id(2L)
                .name("User2")
                .email("user2@yandex.ru")
                .build();

        UserDto responseUser2Dto = UserDto.builder()
                .id(2L)
                .name("User2")
                .email("user2@yandex.ru")
                .build();

        List<User> resultUsers = List.of(user1, user2);
        List<UserDto> responseUsersDto = List.of(responseUser1Dto, responseUser2Dto);

        when(userRepository.findAll()).thenReturn(resultUsers);
        when(userMapper.toUserDto(user1)).thenReturn(responseUser1Dto);
        when(userMapper.toUserDto(user2)).thenReturn(responseUser2Dto);


        Collection<UserDto> resultUsersDto = userService.findAllUsers();

        assertEquals(responseUsersDto, resultUsersDto);
    }

    @Test
    void shouldFindUserByCorrectId() {
        Long userId = 1L;

        when(userRepository.validateUser(userId)).thenReturn(user1);
        when(userMapper.toUserDto(user1)).thenReturn(responseUser1Dto);

        UserDto resultUser1Dto = userService.findUserById(userId);

        assertEquals(responseUser1Dto, resultUser1Dto);
    }

    @Test
    void shouldNotFindUserByInvalidId() {
        Long userId = 999L;

        when(userRepository.validateUser(userId)).thenThrow(InvalidIdException.class);
//        doThrow(InvalidIdException.class).when(userRepository).validateUser(userId);
        assertThrows(InvalidIdException.class,
                () -> userService.findUserById(999L));
    }

    @Test
    void shouldUpdateCorrectUser() {
        User requestUpdatedUser1 = User.builder()
                .name("Updated User1")
                .email("updateduser1@yandex.ru")
                .build();

        User updatedUser1 = User.builder()
                .id(1L)
                .name("Updated User1")
                .email("updateduser1@yandex.ru")
                .build();

        UserDto responseUpdatedUser1Dto = UserDto.builder()
                .id(1L)
                .name("Updated User1")
                .email("updateduser1@yandex.ru")
                .build();

        Long userId = 1L;

        when(userRepository.validateUser(1L)).thenReturn(user1);
        when(userMapper.toUserDto(any())).thenReturn(responseUpdatedUser1Dto);

        UserDto resultUpdatedUser = userService.updateUser(userId, requestUpdatedUser1);

        assertEquals(responseUpdatedUser1Dto, resultUpdatedUser);
    }

    @Test
    void shouldDeleteUserByCorrectId() {
        Long userId = 1L;
        when(userRepository.validateUser(userId)).thenReturn(user1);
        userService.deleteUserById(userId);
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    void shouldDeleteUserByInvalidId() {
        Long userId = 999L;
        when(userRepository.validateUser(userId)).thenThrow(InvalidIdException.class);
//        doThrow(InvalidIdException.class).when(userRepository).validateUser(userId);
        assertThrows(InvalidIdException.class,
                () -> userService.deleteUserById(999L));
        verify(userRepository, never()).deleteById(999L);
    }
}