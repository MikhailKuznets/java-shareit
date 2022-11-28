package ru.practicum.shareit.user.dto;

import ru.practicum.shareit.user.User;

import java.util.Optional;

public class UserMapper {
    public static UserDto toUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail()).
                build();
    }

    public static Optional<UserDto> toOptionalUserDto(Optional<User> user) {
        return user.map(UserMapper::toUserDto);
    }
}
