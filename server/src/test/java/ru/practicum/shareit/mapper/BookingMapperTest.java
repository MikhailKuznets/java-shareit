package ru.practicum.shareit.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.TestUtility;
import ru.practicum.shareit.booking.dto.BookingDtoForItems;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static ru.practicum.shareit.TestUtility.NEXT_BOOKING_END;
import static ru.practicum.shareit.TestUtility.NEXT_BOOKING_START;

@SpringBootTest
class BookingMapperTest {
    private static final Long BOOKING_ID = 1L;
    private static final Long ITEM_ID = 1L;

    @Autowired
    private BookingMapper bookingMapper;

    @Test
    void toBookingDtoForItem() {

        User owner = TestUtility.getUser1();
        User booker = TestUtility.getUser2();

        Item item = Item.builder()
                .id(ITEM_ID)
                .name("Дрель")
                .description("Чтобы доставать соседей")
                .available(true)
                .owner(owner)
                .request(null)
                .build();

        Booking booking = Booking.builder()
                .id(BOOKING_ID)
                .start(NEXT_BOOKING_START)
                .end(NEXT_BOOKING_END)
                .item(item)
                .booker(booker)
                .status(BookingStatus.WAITING)
                .build();

        BookingDtoForItems bookingDto = bookingMapper.toBookingDtoForItem(booking);
        BookingDtoForItems expectedBookingDto = TestUtility.getNextBookingDtoForItems();
        assertNotNull(bookingDto);
        assertEquals(expectedBookingDto, bookingDto);
    }
}