package ru.practicum.shareit.booking.dto;

import org.mapstruct.Mapper;
import ru.practicum.shareit.booking.model.Booking;

@Mapper(componentModel = "spring")
public interface BookingMapper {
    Booking toBookingFromBookingRequestDto(BookingRequestDto requestDto);

    BookingResponseDto toBookingResponseDto(Booking booking);


    // По аналогии со сгенерированным классом BookingMapperImpl
    default BookingDtoForItems toBookingDtoForItem(Booking booking) {
        if (booking == null) {
            return null;
        }

        BookingDtoForItems bookingDtoForItems = BookingDtoForItems.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .status(booking.getStatus())
                .build();

        if (booking.getBooker() != null) {
            bookingDtoForItems.setBookerId(booking.getBooker().getId());
        }

        return bookingDtoForItems;
    }
}
