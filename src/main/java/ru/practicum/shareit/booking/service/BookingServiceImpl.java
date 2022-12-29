package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.controller.BookingState;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
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
    private final ItemRepository itemRepository;
    private final BookingMapper bookingMapper;


    @Override
    public BookingResponseDto createBooking(BookingRequestDto dto, Long userId) {
        //   Проверка начального и конечного времени бронирования
        if (dto.getStart().isAfter(dto.getEnd())) {
            throw new BookingInvalidTimeException("Время и дата начала бронирования должно быть раньше " +
                    "конечного времени и даты бронирования");
        }

        User booker = userRepository.validateUser(userId);
        Item item = itemRepository.validateItem(dto.getItemId());

        //   Проверка Item.Status
        if (!item.getAvailable()) {
            throw new BookingUnavailableException("Предмет с itemId = " + item.getId() +
                    " не доступна для бронирования");
        }

        // Проверка что бронирует не собственник
        if (item.getOwner().equals(booker)) {
            throw new InvalidIdException("Недопустимо бронировать собственный предмет");
        }

        Booking booking = bookingMapper.toBookingFromBookingRequestDto(dto);
        booking.setBooker(booker);
        booking.setItem(item);
        return bookingMapper.toBookingResponseDto(bookingRepository.save(booking));
    }

    @Override
    public BookingResponseDto approveBooking(Long bookingId, Long userId, Boolean isApproved) {
        Booking booking = bookingRepository.validateBooking(bookingId);
        User user = userRepository.validateUser(userId);

        Item item = booking.getItem();
        if (!item.getOwner().equals(user)) {
            throw new InvalidIdException("Нет прав на подтверждение бронирования. Пользователь с userId = "
                    + userId + "не является собственником предмета");
        }
        if (booking.getStatus().equals(BookingStatus.APPROVED)) {
            throw new BookingAlreadyApprovedException("Данное бронирование уже подтверждено");
        }

        if (isApproved) {
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }

        return bookingMapper.toBookingResponseDto(bookingRepository.save(booking));
    }

    @Override
    public BookingResponseDto getBookingById(Long bookingId, Long userId) {
        userRepository.validateUser(userId);

        Booking booking = bookingRepository.validateBooking(bookingId);
        User booker = booking.getBooker();
        User owner = booking.getItem().getOwner();

        Long bookerId = booker.getId();
        Long ownerId = owner.getId();

        if (!(bookerId.equals(userId) || ownerId.equals(userId))) {
            throw new InvalidIdException("Нет прав на просмотр бронирования. Пользователь с userId = "
                    + userId + " не является создателем бронирования или собственником предмета");
        }
        return bookingMapper.toBookingResponseDto(booking);
    }

    @Override
    public Collection<BookingResponseDto> getUserAllBookings(Long bookerId, BookingState state) {
        userRepository.validateUser(bookerId);
        LocalDateTime now = LocalDateTime.now();
        Collection<Booking> bookings;

        switch (state) {
            case ALL:
                bookings = bookingRepository.findAllByBooker_Id(bookerId,
                        SORT_BY_START_DESC);
                break;
            case CURRENT:
                bookings = bookingRepository.findByBooker_IdAndStartIsBeforeAndEndIsAfter(bookerId, now, now,
                        SORT_BY_START_DESC);
                break;
            case FUTURE:
                bookings = bookingRepository.findByBooker_IdAndStartIsAfterAndEndIsAfter(bookerId, now, now,
                        SORT_BY_START_DESC);
                break;
            case PAST:
                bookings = bookingRepository.findByBooker_IdAndStartIsBeforeAndEndIsBefore(bookerId, now, now,
                        SORT_BY_START_DESC);
                break;
            case WAITING:
                bookings = bookingRepository.findByBooker_IdAndStatusIs(bookerId, BookingStatus.WAITING,
                        SORT_BY_START_DESC);
                break;
            case REJECTED:
                bookings = bookingRepository.findByBooker_IdAndStatusIs(bookerId, BookingStatus.REJECTED,
                        SORT_BY_START_DESC);
                break;
            default:
                throw new InvalidStatusException("Неподдерживаемый BookingState: " + state.name());
        }
        return bookings.stream().map(bookingMapper::toBookingResponseDto).collect(Collectors.toList());
    }

    @Override
    public Collection<BookingResponseDto> getOwnerItemAllBookings(Long ownerId, BookingState state) {
        User user = userRepository.validateUser(ownerId);
        LocalDateTime now = LocalDateTime.now();
        Collection<Booking> bookings;

        switch (state) {
            case ALL:
                bookings = bookingRepository.findByItem_Owner_Id(ownerId, SORT_BY_START_DESC);
                break;
            case CURRENT:
                bookings = bookingRepository.findByItem_Owner_IdAndStartIsBeforeAndEndIsAfter(
                        ownerId, LocalDateTime.now(), LocalDateTime.now(), SORT_BY_START_DESC);
                break;
            case FUTURE:
                bookings = bookingRepository.findByItem_Owner_IdAndStartIsAfterAndEndIsAfter(
                        ownerId, LocalDateTime.now(), LocalDateTime.now(), SORT_BY_START_DESC);
                break;
            case PAST:
                bookings = bookingRepository.findByItem_Owner_IdAndStartIsBeforeAndEndIsBefore(
                        ownerId, LocalDateTime.now(), LocalDateTime.now(), SORT_BY_START_DESC);
                break;
            case WAITING:
                bookings = bookingRepository.findByItem_Owner_IdAndStatusIs(
                        ownerId, BookingStatus.WAITING, SORT_BY_START_DESC);
                break;
            case REJECTED:
                bookings = bookingRepository.findByItem_Owner_IdAndStatusIs(
                        ownerId, BookingStatus.REJECTED, SORT_BY_START_DESC);
                break;
            default:
                throw new InvalidStatusException("Неподдерживаемый BookingState: " + state.name());
        }
        return bookings.stream().map(bookingMapper::toBookingResponseDto).collect(Collectors.toList());
    }

}
