package ru.practicum.shareit.item.model;

import lombok.Data;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * TODO Sprint add-controllers.
 */

@Data
public class Item {
    private final Long id;

    @NotBlank(message = "Необходимо указать название предмета.")
    @Size(min = 3, max = 100)
    private String name;

    @Size(max = 500)
    private String description;

    @NotNull
    private Boolean available;

    @NotNull
    private final User owner;

    private final ItemRequest request;
}
