package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingDtoForItems;

import javax.validation.constraints.Size;

@Data
public class ItemResponseDto {
    private Long id;

    @Size(max = 255)
    private String name;

    @Size(max = 255)
    private String description;

    private Boolean available;

    private BookingDtoForItems lastBooking;

    private BookingDtoForItems nextBooking;
}
