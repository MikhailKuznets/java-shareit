package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.InvalidIdException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public Collection<UserDto> findAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto findUserById(Long userId) {
        return userMapper.toUserDto(getUser(userId));
    }

    @Override
    public UserDto createUser(User user) {
        return userMapper.toUserDto(userRepository.save(user));
    }


    @Override
    public UserDto updateUser(Long userId, User user) {
        User selectedUser = getUser(userId);

        String updatedName = user.getName();
        if (updatedName != null) {
            selectedUser.setName(updatedName);
        }

        String updatedEmail = user.getEmail();
        if (updatedEmail != null) {
            selectedUser.setEmail(updatedEmail);
        }

        User updatedUser = userRepository.save(selectedUser);
        return userMapper.toUserDto(updatedUser);
    }

    @Override
    public void deleteUserById(Long userId) {
        userRepository.deleteById(userId);
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> {
            throw new InvalidIdException("Пользователя с id = " + userId + " не существует");
        });
    }
}
