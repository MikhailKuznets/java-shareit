package ru.practicum.shareit.item.dto;

import lombok.*;

import javax.validation.constraints.Size;

/**
 * TODO Sprint add-controllers.
 */
@Data
@Builder
public class ItemDto {
    private Long id;

    @Size(max = 255)
    private String name;

    @Size(max = 255)
    private String description;

    private Boolean available;

}
