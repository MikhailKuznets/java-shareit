package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingDtoForItems;
import ru.practicum.shareit.comment.dto.CommentResponseDto;

import javax.validation.constraints.Size;
import java.util.Collection;

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
    private Collection<CommentResponseDto> comments;
}
