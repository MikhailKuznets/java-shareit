package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.booking.controller.BookingState;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.constraints.Null;
import java.time.LocalDateTime;

public class BookingDto {

    @Null
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;

    private ItemDto item;
    private UserDto booker;
    private BookingState status;
}
