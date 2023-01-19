package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> createBooking(@RequestHeader("X-Sharer-User-Id") Long bookerId,
                                                @RequestBody @Valid BookItemRequestDto bookingRequestDto) {
        log.info("Получен запрос POST /bookings . " +
                "От пользователя с userId = {}. Создать бронирование: {}.", bookerId, bookingRequestDto);
        return bookingClient.createBooking(bookerId, bookingRequestDto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> approveBooking(@RequestHeader("X-Sharer-User-Id") @Positive Long userId,
                                                 @PathVariable @Positive Long bookingId,
                                                 @RequestParam(name = "approved") Boolean isApproved) {
        log.info("Получен запрос Patch /bookings/{}?approved={} . " +
                "От пользователя с userId = {}. " +
                "Подтвердить/отклонить бронирование с bookingId = {}.", bookingId, isApproved, userId, bookingId);
        return bookingClient.approveBooking(userId, bookingId, isApproved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBookingById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                 @PathVariable Long bookingId) {
        log.info("Получен запрос Get /bookings/{} . " +
                "От пользователя с userId = {}. Найти бронирование с bookingId = {}.", bookingId, userId, bookingId);
        return bookingClient.getBookingById(userId, bookingId);
    }


    @GetMapping
    public ResponseEntity<Object> getUserAllBookings(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                     @RequestParam(name = "state", defaultValue = "all") String stateParam,
                                                     @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                     @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
        log.info("Получен запрос Get /bookings?state={} . " +
                "От пользователя с userId = {}. " +
                "Найти его бронирования с статусом = {}.", state, stateParam, state);
        return bookingClient.getUserAllBookings(userId, state, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getOwnerItemAllBookings(
            @RequestHeader("X-Sharer-User-Id") @Positive Long userId,
            @RequestParam(name = "state", defaultValue = "all") String stateParam,
            @RequestParam(defaultValue = "0", required = false) @PositiveOrZero Integer from,
            @RequestParam(defaultValue = "10", required = false) @Positive Integer size) {
        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
        log.info("Получен запрос Get /bookings/owner?state={} . " +
                "От пользователя с userId = {}. " +
                "Найти бронирования с статусом = {} для всех его предметов.", state, userId, state);
        return bookingClient.getOwnerItemAllBookings(userId, state, from, size);
    }

}
