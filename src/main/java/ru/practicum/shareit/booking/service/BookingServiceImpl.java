package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.controller.BookingState;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.InvalidIdException;
import ru.practicum.shareit.exceptions.InvalidStatusException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {

    private static final Sort SORT_BY_START_DESC = Sort.by(Sort.Direction.DESC, "start");
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
    public Collection<BookingDto> getUserAllBookings(Long bookerId, BookingState state) {
        userRepository.validateUser(bookerId);
        LocalDateTime now = LocalDateTime.now();
        Collection<Booking> bookings;

        switch (state) {
            case ALL:
                bookings = bookingRepository.findAllByBooker_Id(bookerId,
                        SORT_BY_START_DESC);
                break;
            case CURRENT:
                bookings = bookingRepository.findByBooker_IdAndStartIsBeforeAndEndIsAfter(
                        bookerId, now, now, SORT_BY_START_DESC);
                break;
            case FUTURE:
                bookings = bookingRepository.findByBooker_IdAndStartIsAfterAndEndIsAfter(
                        bookerId, now, now, SORT_BY_START_DESC);
                break;
            case PAST:
                bookings = bookingRepository.findByBooker_IdAndStartIsBeforeAndEndIsBefore(
                        bookerId, now, now, SORT_BY_START_DESC);
                break;
            case WAITING:
                bookings = bookingRepository.findByBooker_IdAndStatusIs(
                        bookerId, BookingStatus.WAITING, SORT_BY_START_DESC);
                break;
            case REJECTED:
                bookings = bookingRepository.findByBooker_IdAndStatusIs(
                        bookerId, BookingStatus.REJECTED, SORT_BY_START_DESC);
                break;
            default:
                throw new InvalidStatusException("Неподдерживаемый BookingState: " + state.name());
        }
        return bookings.stream().map(bookingMapper::toBookingDto).collect(Collectors.toList());
    }

    @Override
    public Collection<BookingDto> getOwnerItemAllBookings(Long userId, BookingState state) {
        User user = userRepository.validateUser(userId);
        LocalDateTime now = LocalDateTime.now();
        Collection<Booking> bookings = null;


        switch (state) {
            case ALL:
                bookings = bookingRepository.findAllByBooker_Id(userId,
                        SORT_BY_START_DESC);
                break;
            case CURRENT:
                bookings = bookingRepository.findByBooker_IdAndStartIsBeforeAndEndIsAfter(
                        userId, now, now, SORT_BY_START_DESC);
                break;
            case FUTURE:
                bookings = bookingRepository.findByBooker_IdAndStartIsAfterAndEndIsAfter(
                        userId, now, now, SORT_BY_START_DESC);
                break;
            case PAST:
                bookings = bookingRepository.findByBooker_IdAndStartIsBeforeAndEndIsBefore(
                        userId, now, now, SORT_BY_START_DESC);
                break;
            case WAITING:
                bookings = bookingRepository.findByBooker_IdAndStatusIs(
                        userId, BookingStatus.WAITING, SORT_BY_START_DESC);
                break;
            case REJECTED:
                bookings = bookingRepository.findByBooker_IdAndStatusIs(
                        userId, BookingStatus.REJECTED, SORT_BY_START_DESC);
                break;
            default:
                throw new InvalidStatusException("Неподдерживаемый BookingState: " + state.name());
        }
        return bookings.stream().map(bookingMapper::toBookingDto).collect(Collectors.toList());
    }

}
