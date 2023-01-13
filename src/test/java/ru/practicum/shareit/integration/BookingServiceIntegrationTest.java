package ru.practicum.shareit.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.TestUtility;
import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static ru.practicum.shareit.TestUtility.NEXT_BOOKING_END;
import static ru.practicum.shareit.TestUtility.NEXT_BOOKING_START;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BookingServiceIntegrationTest {
    private static final Long OWNER_ID = 1L;
    private static final Long BOOKER_ID = 2L;
    private static final Long BOOKING_ID = 1L;

    @Autowired
    private BookingServiceImpl bookingService;
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private ItemServiceImpl itemService;

    // Booking
    private BookingRequestDto requestBooking;
    private BookingResponseDto responseBooking;
    private BookingResponseDto expectedResponseBooking;
    private Collection<BookingResponseDto> bookings;
    private Collection<BookingResponseDto> expectedBookings;


    @BeforeEach
    void setUp() {
        // Users
        User requestOwner = TestUtility.getUser1();
        User requestBooker = TestUtility.getUser2();
        assertNotNull(requestOwner);
        assertNotNull(requestBooker);

        UserDto responseOwner = userService.createUser(requestOwner);
        UserDto responseBooker = userService.createUser(requestBooker);
        assertNotNull(responseOwner);
        assertNotNull(responseBooker);

        // Item
        ItemRequestDto requestItem = TestUtility.getItemRequestDto();
        assertNotNull(requestItem);

        ItemResponseDto responseItem = itemService.createItem(requestItem, OWNER_ID);
        assertNotNull(responseItem);

        // Booking
        requestBooking = TestUtility.getNextBookingRequestDto();
        assertNotNull(requestBooking);

        expectedResponseBooking = BookingResponseDto.builder()
                .id(1L)
                .start(NEXT_BOOKING_START)
                .end(NEXT_BOOKING_END)
                .item(responseItem)
                .booker(responseBooker)
                .status(BookingStatus.WAITING)
                .build();
        assertNotNull(expectedResponseBooking);
    }

    @Test
    @DisplayName("Должен создать и вернуть Booking при корректных данных")
    void createBooking() {
        responseBooking = bookingService.createBooking(requestBooking, BOOKER_ID);

        assertNotNull(responseBooking);
        assertEquals(expectedResponseBooking, responseBooking);
    }

    @Test
    @DisplayName("Должен создать и вернуть Booking при корректных данных")
    void approveBooking() {
        responseBooking = bookingService.createBooking(requestBooking, BOOKER_ID);
        assertNotNull(responseBooking);
        assertEquals(expectedResponseBooking, responseBooking);

        responseBooking = bookingService.approveBooking(BOOKING_ID, OWNER_ID, true);
        assertEquals(BookingStatus.APPROVED, responseBooking.getStatus());
    }

    @Test
    @DisplayName("Должен найти и вернуть Booking по bookingId при корректных данных")
    void getBookingById() {
        responseBooking = bookingService.createBooking(requestBooking, BOOKER_ID);
        assertNotNull(responseBooking);
        assertEquals(expectedResponseBooking, responseBooking);

        responseBooking = bookingService.getBookingById(BOOKING_ID, OWNER_ID);
        assertEquals(expectedResponseBooking, responseBooking);
    }

    @Test
    @DisplayName("Должен вернуть список Booking's пользователя при корректных данных, BookingState.ALL")
    void shouldGetUserAllBookingsStateAll() {
        responseBooking = bookingService.createBooking(requestBooking, BOOKER_ID);
        assertNotNull(responseBooking);
        assertEquals(expectedResponseBooking, responseBooking);

        int from = 0;
        int size = 10;
        expectedBookings = List.of(expectedResponseBooking);
        bookings = bookingService.getUserAllBookings(BOOKER_ID, BookingState.ALL, from, size);
        assertNotNull(expectedBookings);
        assertEquals(1, expectedBookings.size());
        assertEquals(expectedBookings, bookings);
    }

    @Test
    @DisplayName("Должен вернуть список Booking's пользователя при корректных данных, BookingState.ALL")
    void shouldGetUserAllBookingsStateWaiting() {
        responseBooking = bookingService.createBooking(requestBooking, BOOKER_ID);
        assertNotNull(responseBooking);
        assertEquals(expectedResponseBooking, responseBooking);

        int from = 0;
        int size = 10;
        expectedBookings = List.of(expectedResponseBooking);
        bookings = bookingService.getUserAllBookings(BOOKER_ID, BookingState.WAITING, from, size);
        assertNotNull(expectedBookings);
        assertEquals(1, expectedBookings.size());
        assertEquals(expectedBookings, bookings);
    }

    @Test
    @DisplayName("Должен вернуть список Booking's пользователя при корректных данных, BookingState.FUTURE")
    void shouldGetUserAllBookingsStateFuture() {
        responseBooking = bookingService.createBooking(requestBooking, BOOKER_ID);
        assertNotNull(responseBooking);
        assertEquals(expectedResponseBooking, responseBooking);

        int from = 0;
        int size = 10;
        expectedBookings = List.of(expectedResponseBooking);
        bookings = bookingService.getUserAllBookings(BOOKER_ID, BookingState.FUTURE, from, size);
        assertNotNull(expectedBookings);
        assertEquals(1, expectedBookings.size());
        assertEquals(expectedBookings, bookings);
    }

    @Test
    @DisplayName("Должен вернуть список Booking's пользователя при корректных данных, BookingState.REJECTED")
    void shouldGetUserAllBookingsStateRejected() {
        responseBooking = bookingService.createBooking(requestBooking, BOOKER_ID);
        assertNotNull(responseBooking);
        assertEquals(expectedResponseBooking, responseBooking);

        bookingService.approveBooking(BOOKING_ID, OWNER_ID, false);
        expectedResponseBooking.setStatus(BookingStatus.REJECTED);

        int from = 0;
        int size = 10;
        expectedBookings = List.of(expectedResponseBooking);
        bookings = bookingService.getUserAllBookings(BOOKER_ID, BookingState.REJECTED, from, size);
        assertNotNull(expectedBookings);
        assertEquals(1, expectedBookings.size());
        assertEquals(expectedBookings, bookings);
    }

    @Test
    @DisplayName("Должен вернуть список бронирований Booking's всех предметов собственника при корректных данных" +
            "BookingState.ALL")
    void shouldGetOwnerItemAllBookingsStateAll() {
        responseBooking = bookingService.createBooking(requestBooking, BOOKER_ID);
        assertNotNull(responseBooking);
        assertEquals(expectedResponseBooking, responseBooking);

        int from = 0;
        int size = 10;
        expectedBookings = List.of(expectedResponseBooking);
        bookings = bookingService.getOwnerItemAllBookings(OWNER_ID, BookingState.ALL, from, size);
        assertNotNull(expectedBookings);
        assertEquals(1, expectedBookings.size());
        assertEquals(expectedBookings, bookings);
    }

    @Test
    @DisplayName("Должен вернуть список бронирований Booking's всех предметов собственника при корректных данных" +
            "BookingState.WAITING")
    void shouldGetOwnerItemAllBookingsStateWaiting() {
        responseBooking = bookingService.createBooking(requestBooking, BOOKER_ID);
        assertNotNull(responseBooking);
        assertEquals(expectedResponseBooking, responseBooking);

        int from = 0;
        int size = 10;
        expectedBookings = List.of(expectedResponseBooking);
        bookings = bookingService.getOwnerItemAllBookings(OWNER_ID, BookingState.WAITING, from, size);
        assertNotNull(expectedBookings);
        assertEquals(1, expectedBookings.size());
        assertEquals(expectedBookings, bookings);
    }

    @Test
    @DisplayName("Должен вернуть список бронирований Booking's всех предметов собственника при корректных данных" +
            "BookingState.FUTURE")
    void shouldGetOwnerItemAllBookingsStateFuture() {
        responseBooking = bookingService.createBooking(requestBooking, BOOKER_ID);
        assertNotNull(responseBooking);
        assertEquals(expectedResponseBooking, responseBooking);

        int from = 0;
        int size = 10;
        expectedBookings = List.of(expectedResponseBooking);
        bookings = bookingService.getOwnerItemAllBookings(OWNER_ID, BookingState.FUTURE, from, size);
        assertNotNull(expectedBookings);
        assertEquals(1, expectedBookings.size());
        assertEquals(expectedBookings, bookings);
    }

    @Test
    @DisplayName("Должен вернуть список бронирований Booking's всех предметов собственника при корректных данных" +
            "BookingState.REJECTED")
    void shouldGetOwnerItemAllBookingsStateRejected() {
        responseBooking = bookingService.createBooking(requestBooking, BOOKER_ID);
        assertNotNull(responseBooking);
        assertEquals(expectedResponseBooking, responseBooking);

        bookingService.approveBooking(BOOKING_ID, OWNER_ID, false);
        expectedResponseBooking.setStatus(BookingStatus.REJECTED);

        int from = 0;
        int size = 10;
        expectedBookings = List.of(expectedResponseBooking);
        bookings = bookingService.getOwnerItemAllBookings(OWNER_ID, BookingState.REJECTED, from, size);
        assertNotNull(expectedBookings);
        assertEquals(1, expectedBookings.size());
        assertEquals(expectedBookings, bookings);
    }


}