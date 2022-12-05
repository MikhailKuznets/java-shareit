package ru.practicum.shareit.user.dto;


import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Null;

@Data
@Builder
public class UserDto {
    @Null
    private final Long id;
    private final String name;
    private final String email;
}
