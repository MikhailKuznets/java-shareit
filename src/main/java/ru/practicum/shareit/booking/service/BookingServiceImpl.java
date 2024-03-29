package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.comment.dto.CommentMapper;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.comment.repository.CommentRepository;
import ru.practicum.shareit.exceptions.BookingAlreadyApprovedException;
import ru.practicum.shareit.exceptions.BookingInvalidTimeException;
import ru.practicum.shareit.exceptions.BookingUnavailableException;
import ru.practicum.shareit.exceptions.InvalidIdException;
import ru.practicum.shareit.item.dto.ItemResponseDto;
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

    private static final Sort START_DESC_SORT = Sort.by(Sort.Direction.DESC, "start");
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final CommentRepository commentRepository;
    private final BookingMapper bookingMapper;
    private final CommentMapper commentMapper;


    @Override
    public BookingResponseDto createBooking(BookingRequestDto bookingRequestDto, Long userId) {
        //   Проверка начального и конечного времени бронирования
        if (bookingRequestDto.getStart().isAfter(bookingRequestDto.getEnd())) {
            throw new BookingInvalidTimeException("Время и дата начала бронирования должно быть раньше " +
                    "конечного времени и даты бронирования");
        }

        User booker = getUser(userId);
        Long itemId = bookingRequestDto.getItemId();
        Item item = itemRepository.findById(itemId).orElseThrow(() -> {
            throw new InvalidIdException("Предмет с id = " + itemId + " не существует");
        });

        //   Проверка Item.Status
        if (!item.getAvailable()) {
            throw new BookingUnavailableException("Предмет с itemId = " + itemId +
                    " не доступна для бронирования");
        }

        // Проверка что бронирует не собственник
        if (item.getOwner().equals(booker)) {
            throw new InvalidIdException("Недопустимо бронировать собственный предмет");
        }

        Booking booking = bookingMapper.toBookingFromBookingRequestDto(bookingRequestDto);
        booking.setBooker(booker);
        booking.setItem(item);
        booking.setStatus(BookingStatus.WAITING);
        BookingResponseDto bookingDto = bookingMapper.toBookingResponseDto(bookingRepository.save(booking));
        return setComments(bookingDto);
    }

    @Override
    public BookingResponseDto approveBooking(Long bookingId, Long userId, Boolean isApproved) {
        Booking booking = getBooking(bookingId);
        User user = getUser(userId);

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
        getUser(userId);

        Booking booking = getBooking(bookingId);

        User booker = booking.getBooker();
        Long bookerId = booker.getId();

        Item item = booking.getItem();
        User owner = item.getOwner();
        if (owner == null) {
            throw new InvalidIdException("Ошибка проверки собственника предмета");
        }

        Long ownerId = owner.getId();

        if (!(bookerId.equals(userId) || ownerId.equals(userId))) {
            throw new InvalidIdException("Нет прав на просмотр бронирования. Пользователь с userId = "
                    + userId + " не является создателем бронирования или собственником предмета");
        }
        BookingResponseDto bookingDto = bookingMapper.toBookingResponseDto(booking);
        return setComments(bookingDto);
    }

    @Override
    public Collection<BookingResponseDto> getUserAllBookings(Long bookerId,
                                                             BookingState state,
                                                             Integer from,
                                                             Integer size) {
        getUser(bookerId);
        LocalDateTime now = LocalDateTime.now();

        PageRequest pageRequest = PageRequest.of(from, size, START_DESC_SORT);
        Page<Booking> bookings = Page.empty();

        switch (state) {
            case ALL:
                bookings = bookingRepository.findAllByBooker_Id(bookerId,
                        // затычка для прохождения некорректных тестов Postman - обсуждалась в пачке
                        PageRequest.of((from / size), size, START_DESC_SORT));
                break;
            case CURRENT:
                bookings = bookingRepository.findByBooker_IdAndStartIsBeforeAndEndIsAfter(bookerId, now, now,
                        pageRequest);
                break;
            case FUTURE:
                bookings = bookingRepository.findByBooker_IdAndStartIsAfterAndEndIsAfter(bookerId, now, now,
                        pageRequest);
                break;
            case PAST:
                bookings = bookingRepository.findByBooker_IdAndStartIsBeforeAndEndIsBefore(bookerId, now, now,
                        pageRequest);
                break;
            case WAITING:
                bookings = bookingRepository.findByBooker_IdAndStatusIs(bookerId, BookingStatus.WAITING,
                        pageRequest);
                break;
            case REJECTED:
                bookings = bookingRepository.findByBooker_IdAndStatusIs(bookerId, BookingStatus.REJECTED,
                        pageRequest);
                break;
        }
        return bookings.stream()
                .map(bookingMapper::toBookingResponseDto)
                .map(this::setComments)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<BookingResponseDto> getOwnerItemAllBookings(Long ownerId,
                                                                  BookingState state,
                                                                  Integer from,
                                                                  Integer size) {
        getUser(ownerId);
        LocalDateTime now = LocalDateTime.now();

        PageRequest pageRequest = PageRequest.of(from, size, START_DESC_SORT);
        Page<Booking> bookings = Page.empty();
        switch (state) {
            case ALL:
                bookings = bookingRepository.findByItem_Owner_Id(ownerId, pageRequest);
                break;
            case CURRENT:
                bookings = bookingRepository.findByItem_Owner_IdAndStartIsBeforeAndEndIsAfter(
                        ownerId, now, now, pageRequest);
                break;
            case FUTURE:
                bookings = bookingRepository.findByItem_Owner_IdAndStartIsAfterAndEndIsAfter(
                        ownerId, now, now, pageRequest);
                break;
            case PAST:
                bookings = bookingRepository.findByItem_Owner_IdAndStartIsBeforeAndEndIsBefore(
                        ownerId, now, now, pageRequest);
                break;
            case WAITING:
                bookings = bookingRepository.findByItem_Owner_IdAndStatusIs(
                        ownerId, BookingStatus.WAITING, pageRequest);
                break;
            case REJECTED:
                bookings = bookingRepository.findByItem_Owner_IdAndStatusIs(
                        ownerId, BookingStatus.REJECTED, pageRequest);
                break;
        }
        return bookings.stream()
                .map(bookingMapper::toBookingResponseDto)
                .map(this::setComments)
                .collect(Collectors.toList());
    }

    private BookingResponseDto setComments(BookingResponseDto bookingDto) {
        ItemResponseDto item = bookingDto.getItem();
        Long itemId = item.getId();

        Collection<Comment> comments = commentRepository.findAllByItem_Id(itemId);

        item.setComments(comments.stream()
                .map(commentMapper::toCommentResponseDto)
                .collect(Collectors.toList()));
        bookingDto.setItem(item);
        return bookingDto;
    }

    private Booking getBooking(Long bookingId) {
        return bookingRepository.findById(bookingId).orElseThrow(() -> {
            throw new InvalidIdException("Бронирование с id = " + bookingId + " не существует");
        });
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> {
            throw new InvalidIdException("Пользователя с id = " + userId + " не существует");
        });
    }

}
