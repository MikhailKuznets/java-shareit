package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;

    @Override
    public Collection<UserDto> findAllUsers() {
        return userStorage.getAllUsers()
                .stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<UserDto> findUserById(Long userId) {
        return UserMapper.toOptionalUserDto(userStorage.findUserById(userId));
    }

    @Override
    public UserDto createUser(User user) {
        return UserMapper.toUserDto(userStorage.createUser(user));
    }

    @Override
    public UserDto updateUser(Long userId, User user) {
        return UserMapper.toUserDto(userStorage.updateUser(userId, user));
    }

    @Override
    public void deleteUserById(Long userId) {
        userStorage.deleteUser(userId);
    }
}
