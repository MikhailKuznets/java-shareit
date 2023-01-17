package ru.practicum.shareit.user.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class UserRequestDto {
    @NotBlank(message = "Необходимо указать имя или login.")
    @Size(min = 3, max = 255)
    private String name;

    @Email(message = "Email должен быть корректным адресом электронной почты.")
    @NotNull(message = "Email не должен быть NULL.")
    private String email;
}
