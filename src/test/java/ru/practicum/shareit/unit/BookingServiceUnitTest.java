package ru.practicum.shareit.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.TestUtility;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.exceptions.BookingAlreadyApprovedException;
import ru.practicum.shareit.exceptions.BookingInvalidTimeException;
import ru.practicum.shareit.exceptions.BookingUnavailableException;
import ru.practicum.shareit.exceptions.InvalidIdException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceUnitTest {
    private static final Long BOOKING_ID = 1L;
    private static final Long BOOKER_ID = 2L;
    private static final Long OWNER_ID = 1L;
    private static final Long OTHER_USER_ID = 3L;
    private static final Long ITEM_ID = 1L;
    @InjectMocks
    private BookingServiceImpl bookingService;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private Booking booking;

    private static BookingRequestDto requestBooking;
    private static BookingResponseDto responseBooking;
    private Item bookedItem;
    private User owner;
    private User booker;
    private User otherUser;
    private PageRequest pageRequest;

    @BeforeEach
    void setUp() {
        owner = TestUtility.getUser1();
        booker = TestUtility.getUser2();
        otherUser = User.builder()
                .id(OTHER_USER_ID)
                .email("user3@yandex.ru")
                .name("User3")
                .build();

        bookedItem = Item.builder()
                .id(ITEM_ID)
                .name("Дрель")
                .description("Чтобы доставать соседей")
                .available(true)
                .owner(owner)
                .request(null)
                .build();

        requestBooking = TestUtility.getNextBookingRequestDto();
        responseBooking = TestUtility.getNextBookingResponseDto();
        pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "start"));
    }


    @Test
    @DisplayName("Не должен создать Booking при корректных данных времени бронирования")
    void shouldNotCreateBookingWithInvalidTime() {
        requestBooking.setStart(TestUtility.NEXT_BOOKING_END);
        requestBooking.setEnd(TestUtility.NEXT_BOOKING_START);

        assertThrows(BookingInvalidTimeException.class,
                () -> bookingService.createBooking(requestBooking, BOOKER_ID));
    }

    @Test
    @DisplayName("Не должен создать Booking недоступного для бронирования Item")
    void shouldNotCreateBookingUnavailableItem() {
        bookedItem.setAvailable(false);

        when(userRepository.validateUser(BOOKER_ID)).thenReturn(booker);
        when(itemRepository.validateItem(anyLong())).thenReturn(bookedItem);

        assertThrows(BookingUnavailableException.class,
                () -> bookingService.createBooking(requestBooking, BOOKER_ID));
    }

    @Test
    @DisplayName("Не должен создать Booking собственного предмета собственником")
    void shouldNotCreateBookingOwnerItem() {
        when(userRepository.validateUser(OWNER_ID)).thenReturn(owner);
        when(itemRepository.validateItem(anyLong())).thenReturn(bookedItem);

        assertThrows(InvalidIdException.class,
                () -> bookingService.createBooking(requestBooking, OWNER_ID));
    }

    @Test
    @DisplayName("Не должен подтвердить Booking не собственником")
    void shouldNotApproveBookingByOtherUser() {
        when(bookingRepository.validateBooking(BOOKING_ID)).thenReturn(booking);
        when(userRepository.validateUser(OTHER_USER_ID)).thenReturn(otherUser);

        when(booking.getItem()).thenReturn(bookedItem);

        assertThrows(InvalidIdException.class,
                () -> bookingService.approveBooking(BOOKING_ID, OTHER_USER_ID, true));
    }

    @Test
    @DisplayName("Не должен подтвердить уже подтверпжденный Booking")
    void shouldNotApproveApprovedBooking() {
        when(bookingRepository.validateBooking(BOOKING_ID)).thenReturn(booking);
        when(userRepository.validateUser(OWNER_ID)).thenReturn(owner);

        when(booking.getItem()).thenReturn(bookedItem);
        when(booking.getStatus()).thenReturn(BookingStatus.APPROVED);

        assertThrows(BookingAlreadyApprovedException.class,
                () -> bookingService.approveBooking(BOOKING_ID, OWNER_ID, true));
    }
}