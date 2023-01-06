package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;

@RestController
@RequestMapping(path = "/bookings")
@Slf4j
@RequiredArgsConstructor
@Validated
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<BookingResponseDto> createBooking(@RequestHeader("X-Sharer-User-Id")
                                                            @Positive Long userId,
                                                            @RequestBody @Valid BookingRequestDto dto) {
        log.info("Получен запрос POST /bookings . " +
                "От пользователя с userId = {}. Создать бронирование: {}.", userId, dto);
        return new ResponseEntity<>(bookingService.createBooking(dto, userId), HttpStatus.OK);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<BookingResponseDto> approveBooking(@RequestHeader("X-Sharer-User-Id")
                                                             @Positive Long userId,
                                                             @PathVariable @Positive Long bookingId,
                                                             @RequestParam(name = "approved") Boolean isApproved) {
        log.info("Получен запрос Patch /bookings/{}?approved={} . " +
                "От пользователя с userId = {}. " +
                "Подтвердить/отклонить бронирование с bookingId = {}.", bookingId, isApproved, userId, bookingId);
        return new ResponseEntity<>(bookingService.approveBooking(bookingId, userId, isApproved), HttpStatus.OK);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<BookingResponseDto> getBookingById(@RequestHeader("X-Sharer-User-Id")
                                                             @Positive Long userId,
                                                             @PathVariable @Positive Long bookingId) {
        log.info("Получен запрос Get /bookings/{} . " +
                "От пользователя с userId = {}. Найти бронирование с bookingId = {}.", bookingId, userId, bookingId);
        return new ResponseEntity<>(bookingService.getBookingById(bookingId, userId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Collection<BookingResponseDto>> getUserAllBookings(
            @RequestHeader("X-Sharer-User-Id") @Positive Long bookerId,
            @RequestParam(required = false, defaultValue = "ALL") BookingState state,
            @RequestParam(defaultValue = "0", required = false) @PositiveOrZero Integer from,
            @RequestParam(defaultValue = "10", required = false) @Positive Integer size) {
        log.info("Получен запрос Get /bookings?state={} . " +
                "От пользователя с userId = {}. " +
                "Найти его бронирования с статусом = {}.", state, bookerId, state);
        return new ResponseEntity<>(bookingService.getUserAllBookings(
                bookerId, state, from, size),
                HttpStatus.OK);
    }

    @GetMapping("/owner")
    public ResponseEntity<Collection<BookingResponseDto>> getOwnerItemAllBookings(
            @RequestHeader("X-Sharer-User-Id") @Positive Long userId,
            @RequestParam(required = false, defaultValue = "ALL") BookingState state,
            @RequestParam(defaultValue = "0", required = false) @PositiveOrZero Integer from,
            @RequestParam(defaultValue = "10", required = false) @Positive Integer size) {
        log.info("Получен запрос Get /bookings/owner?state={} . " +
                "От пользователя с userId = {}. " +
                "Найти бронирования с статусом = {} для всех его предметов.", state, userId, state);
        return new ResponseEntity<>(bookingService.getOwnerItemAllBookings(
                userId, state, from, size),
                HttpStatus.OK);
    }

}
