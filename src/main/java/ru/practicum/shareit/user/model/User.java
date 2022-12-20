package ru.practicum.shareit.user.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * TODO Sprint add-controllers.
 */
@Data
@Builder
@Valid
public class User {
    private Long id;

    @NotBlank(message = "Необходимо указать имя или login.")
    @Size(min = 3, max = 50)
    private String name;

    @Email(message = "Email должен быть корректным адресом электронной почты.")
    @NotNull(message = "Email не должен быть NULL.")
    private String email;
}
