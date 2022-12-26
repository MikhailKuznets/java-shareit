package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.controller.BookingState;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;

import java.util.Collection;

public interface BookingService {

    BookingDto createBooking(Booking booking, Long userId);

    BookingDto approveBooking(Long bookingId, Long userId, Boolean isApproved);

    BookingDto getBookingById(Long bookingId, Long userId);

    Collection<BookingDto> getUserAllBookings(Long userId, BookingState state);

    Collection<BookingDto> getOwnerItemAllBookings(Long userId, BookingState state);
}

