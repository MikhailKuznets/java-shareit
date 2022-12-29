package ru.practicum.shareit.booking.dto;

import org.mapstruct.Mapper;
import ru.practicum.shareit.booking.model.Booking;

import java.util.Objects;

@Mapper(componentModel = "spring")
public interface BookingMapper {
    Booking toBookingFromBookingRequestDto(BookingRequestDto requestDto);

    BookingResponseDto toBookingResponseDto(Booking booking);

    default BookingDtoForItems toBookingDtoForItem(Booking booking) {
        if (Objects.isNull(booking)) {
            return null;
        }
        BookingDtoForItems bookingDtoForItems = new BookingDtoForItems();
        bookingDtoForItems.setId(booking.getId());
        bookingDtoForItems.setStart(booking.getStart());
        bookingDtoForItems.setEnd(booking.getEnd());
        bookingDtoForItems.setBookerId(booking.getBooker().getId());
        bookingDtoForItems.setStatus(booking.getStatus());
        return bookingDtoForItems;
    }
}
