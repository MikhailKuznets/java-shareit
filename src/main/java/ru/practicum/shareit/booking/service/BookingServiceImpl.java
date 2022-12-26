package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.controller.BookingState;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.InvalidIdException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final BookingMapper bookingMapper;


    @Override
    public BookingDto createBooking(Booking booking, Long userId) {
        User user = userRepository.validateUser(userId);
        booking.setBooker(user);
        return bookingMapper.toBookingDto(bookingRepository.save(booking));
    }

    @Override
    public BookingDto approveBooking(Long bookingId, Long userId, Boolean isApproved) {
        return null;
    }

    @Override
    public BookingDto getBookingById(Long bookingId, Long userId) {
        User user = userRepository.validateUser(userId);
        Booking booking = bookingRepository.validateBooking(bookingId);
        User itemOwner = booking.getItem().getOwner();

        if (booking.getBooker().equals(user) || itemOwner.equals(user)) {
            throw new InvalidIdException("Нет прав на просмотр бронирования. Пользователь с userId = "
                    + userId + "не является создателем бронирования или собственником предмета");
        }
        return bookingMapper.toBookingDto(booking);
    }

    @Override
    public Collection<BookingDto> getUserAllBookings(Long userId, BookingState state) {
        User user = userRepository.validateUser(userId);
        LocalDateTime now = LocalDateTime.now();

        switch (state) {
            case ALL:

            case CURRENT:

            case FUTURE:

            case PAST:

            case WAITING:

            case REJECTED:
        }


        return null;
    }

    @Override
    public Collection<BookingDto> getOwnerItemAllBookings(Long userId, BookingState state) {
        User user = userRepository.validateUser(userId);
        LocalDateTime now = LocalDateTime.now();

        switch (state) {
            case ALL:

            case CURRENT:

            case FUTURE:

            case PAST:

            case WAITING:

            case REJECTED:
        }
        return null;
    }

}
