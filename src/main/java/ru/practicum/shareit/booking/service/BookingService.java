package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.controller.BookingState;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;

import java.util.Collection;

public interface BookingService {

    BookingResponseDto createBooking(BookingRequestDto dto, Long userId);

    BookingResponseDto approveBooking(Long bookingId, Long userId, Boolean isApproved);

    BookingResponseDto getBookingById(Long bookingId, Long userId);

    Collection<BookingResponseDto> getUserAllBookings(Long userId, BookingState state);

    Collection<BookingResponseDto> getOwnerItemAllBookings(Long userId, BookingState state);
}

