package ru.practicum.shareit.item.dto;

import lombok.*;
import ru.practicum.shareit.booking.dto.BookingDtoForItems;
import ru.practicum.shareit.comment.dto.CommentResponseDto;

import java.util.Collection;

@Data
@Builder
public class ItemResponseDto {
    private Long id;

    private String name;

    private String description;

    private Boolean available;

    private BookingDtoForItems lastBooking;

    private BookingDtoForItems nextBooking;

    private Collection<CommentResponseDto> comments;

    private Long requestId;
}
