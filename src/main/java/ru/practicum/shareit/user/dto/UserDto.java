package ru.practicum.shareit.user.dto;


import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Null;

@Data
@RequiredArgsConstructor
@Builder
public class UserDto {
    @Null
    private final Long id;
    private final String name;
    private final String email;
}
