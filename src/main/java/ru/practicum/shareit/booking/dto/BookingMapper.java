package ru.practicum.shareit.booking.dto;

import org.mapstruct.Mapper;
import ru.practicum.shareit.booking.model.Booking;

@Mapper(componentModel = "spring")
public interface BookingMapper {
    Booking toBookingFromBookingRequestDto(BookingRequestDto requestDto);

    BookingResponseDto toBookingResponseDto(Booking booking);
}
