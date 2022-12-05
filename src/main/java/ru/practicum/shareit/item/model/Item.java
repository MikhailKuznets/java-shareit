package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * TODO Sprint add-controllers.
 */

@Data
@Builder
@Valid

@Getter
@AllArgsConstructor
public class Item {
    private Long id;

    @NotBlank(message = "Необходимо указать название предмета.")
    @Size(min = 3, max = 100)
    private String name;

    @NotBlank(message = "Необходимо указать описание предмета.")
    @Size(min = 3, max = 500)
    private String description;

    @NotNull
    private Boolean available;

    private User owner;

    private ItemRequest request;
}
