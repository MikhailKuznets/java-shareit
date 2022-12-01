package ru.practicum.shareit.item.dto;

import lombok.*;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * TODO Sprint add-controllers.
 */
@Data
//@RequiredArgsConstructor
//@AllArgsConstructor
//@NoArgsConstructor
@Builder
public class ItemDto {
    private Long id;

    //    @NotBlank(message = "Необходимо указать название предмета.")
    @Size(min = 3, max = 100)
    private String name;

    @Size(max = 500)
    private String description;

    @NotNull
    private Boolean available;

//    @NotNull
    private User owner;

    private ItemRequest request;
}
