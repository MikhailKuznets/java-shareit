package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.Collection;

@RestController
@RequestMapping(path = "/bookings")
@Slf4j
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<BookingDto> createBooking(@RequestHeader("X-Sharer-User-Id") @Positive Long userId,
                                                    @RequestBody @Valid Booking booking) {
        log.info("Получен запрос POST /bookings . " +
                "От пользователя с userId = {}. Создать бронирование: {}.", userId, booking);
        return new ResponseEntity<>(bookingService.createBooking(booking, userId), HttpStatus.OK);
    }

    @PatchMapping("/{bookingId}?approved={}")
    public ResponseEntity<BookingDto> approveBooking(@RequestHeader("X-Sharer-User-Id") @Positive Long userId,
                                                     @PathVariable @Positive Long bookingId,
                                                     @RequestParam(name = "approved") Boolean isApproved) {
        log.info("Получен запрос Patch /bookings/{}?approved={} . " +
                "От пользователя с userId = {}. " +
                "Подтвердить/отклонить бронирование с bookingId = {}.", bookingId, isApproved, userId, bookingId);
        return new ResponseEntity<>(bookingService.approveBooking(bookingId, userId, isApproved), HttpStatus.OK);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<BookingDto> getBookingById(@RequestHeader("X-Sharer-User-Id") @Positive Long userId,
                                                     @PathVariable @Positive Long bookingId) {
        log.info("Получен запрос Get /bookings/{} . " +
                "От пользователя с userId = {}. Найти бронирование с bookingId = {}.", bookingId, userId, bookingId);
        return new ResponseEntity<>(bookingService.getBookingById(bookingId, userId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Collection<BookingDto>> getUserAllBookings(@RequestHeader("X-Sharer-User-Id") @Positive Long userId,
                                                         @RequestParam(required = false, defaultValue = "ALL")
                                                         BookingState state) {
        log.info("Получен запрос Get /bookings?state={} . " +
                "От пользователя с userId = {}. " +
                "Найти его бронирования с статусом = {}.", state, userId, state);
        return new ResponseEntity<>(bookingService.getUserAllBookings(userId, state), HttpStatus.OK);
    }

    @GetMapping("/owner")
    public ResponseEntity<Collection<BookingDto>> getOwnerItemAllBookings(@RequestHeader("X-Sharer-User-Id") @Positive Long userId,
                                                                          @RequestParam(required = false, defaultValue = "ALL")
                                                                          BookingState state) {
        log.info("Получен запрос Get /bookings/owner?state={} . " +
                "От пользователя с userId = {}. " +
                "Найти бронирования с статусом = {} для всех его предметов.", state, userId, state);
        return new ResponseEntity<>(bookingService.getOwnerItemAllBookings(userId, state), HttpStatus.OK);
    }

}
