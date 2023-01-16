package ru.practicum.shareit.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.practicum.shareit.TestUtility;
import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.booking.controller.BookingController;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exceptions.BookingAlreadyApprovedException;
import ru.practicum.shareit.exceptions.BookingInvalidTimeException;
import ru.practicum.shareit.exceptions.BookingUnavailableException;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;


@AutoConfigureMockMvc
@WebMvcTest({BookingController.class})
class BookingControllerRestTest {
    private static final String PATH = "/bookings";
    private static final String PATH_WITH_ID = "/bookings/1";
    public static final String X_SHADER_USER_ID = "X-Sharer-User-Id";
    private static final Long BOOKING_1_ID = 1L;
    private static final Long USER_1_ID = 1L;


    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private BookingService bookingService;
    @MockBean
    private User otherUser;

    private BookingRequestDto requestBooking;
    private BookingResponseDto responseBooking;

    @BeforeEach
    void setUp() {
        requestBooking = TestUtility.getNextBookingRequestDto();
        responseBooking = TestUtility.getNextBookingResponseDto();
        responseBooking.setId(BOOKING_1_ID);
    }


    @Test
    @DisplayName("Должен создать Booking при корректных данных")
    void shouldCreateCorrectBooking() throws Exception {
        when(bookingService.createBooking(any(BookingRequestDto.class), anyLong())).thenReturn(responseBooking);

        mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .header(X_SHADER_USER_ID, USER_1_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBooking))
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(
                        objectMapper.writeValueAsString(responseBooking)
                ));

        verify(bookingService, times(1)).createBooking(requestBooking, USER_1_ID);
    }

    @Test
    @DisplayName("Должен подтвердить бронирование Booking при корректных данных")
    void shouldApproveBooking() throws Exception {
        responseBooking.setStatus(BookingStatus.APPROVED);

        when(bookingService.approveBooking(anyLong(), anyLong(), anyBoolean())).thenReturn(responseBooking);

        mockMvc.perform(MockMvcRequestBuilders.patch(PATH_WITH_ID + "?approved=true")
                        .header(X_SHADER_USER_ID, USER_1_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBooking))
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(
                        objectMapper.writeValueAsString(responseBooking)
                ));

        verify(bookingService, times(1)).approveBooking(BOOKING_1_ID, USER_1_ID, true);
    }

    @Test
    @DisplayName("Должен вернуть бронирование Booking по Id при корректных данных Owner/Booker")
    void shouldGetBookingById() throws Exception {
        when(bookingService.getBookingById(anyLong(), anyLong())).thenReturn(responseBooking);

        mockMvc.perform(MockMvcRequestBuilders.get(PATH_WITH_ID)
                        .header(X_SHADER_USER_ID, USER_1_ID)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(
                        objectMapper.writeValueAsString(responseBooking)
                ));

        verify(bookingService, times(1)).getBookingById(BOOKING_1_ID, USER_1_ID);
    }

    @Test
    @DisplayName("Должен вернуть список бронирований Booking текущего Booker при корректных данных")
    void shouldGetUserAllBookings() throws Exception {
        List<BookingResponseDto> bookings = List.of(responseBooking);

        when(bookingService.getUserAllBookings(anyLong(), any(BookingState.class),
                anyInt(), anyInt())).thenReturn(bookings);

        mockMvc.perform(MockMvcRequestBuilders.get(PATH + "?state=ALL")
                        .header(X_SHADER_USER_ID, USER_1_ID)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(
                        objectMapper.writeValueAsString(bookings)
                ));

        verify(bookingService, times(1))
                .getUserAllBookings(USER_1_ID, BookingState.ALL, 0, 10);
    }

    @Test
    @DisplayName("Должен бронирования список Booking всех вещей владельца Owner при корректных данных")
    void shouldGetOwnerItemAllBookings() throws Exception {
        List<BookingResponseDto> bookings = List.of(responseBooking);

        when(bookingService.getOwnerItemAllBookings(anyLong(), any(BookingState.class),
                anyInt(), anyInt())).thenReturn(bookings);

        mockMvc.perform(MockMvcRequestBuilders.get(PATH + "/owner?state=ALL")
                        .header(X_SHADER_USER_ID, USER_1_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(
                        objectMapper.writeValueAsString(bookings)
                ));

        verify(bookingService, times(1))
                .getOwnerItemAllBookings(USER_1_ID, BookingState.ALL, 0, 10);
    }

    @Test
    @DisplayName("Должен обработать BookingInvalidTimeException")
    void shouldNotCreateBookingByInvalidTime() throws Exception {
        when(bookingService.createBooking(any(BookingRequestDto.class), anyLong())).thenThrow(
                new BookingInvalidTimeException("Время и дата начала бронирования должно быть раньше " +
                        "конечного времени и даты бронирования"));

        mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .header(X_SHADER_USER_ID, USER_1_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBooking))
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
        verify(bookingService, times(1)).createBooking(requestBooking, USER_1_ID);

    }

    @Test
    @DisplayName("Должен обработать BookingUnavailableException")
    void shouldNotCreateBookingUnavailableItem() throws Exception {
        when(bookingService.createBooking(any(BookingRequestDto.class), anyLong())).thenThrow(
                new BookingUnavailableException("Предмет с itemId = 1 не доступна для бронирования"));

        mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .header(X_SHADER_USER_ID, USER_1_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBooking))
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
        verify(bookingService, times(1)).createBooking(requestBooking, USER_1_ID);

    }

    @Test
    @DisplayName("Должен обработать BookingAlreadyApprovedException")
    void shouldNotApproveApprovedBooking() throws Exception {
        responseBooking.setStatus(BookingStatus.APPROVED);

        when(bookingService.approveBooking(anyLong(), anyLong(), anyBoolean())).thenThrow(
                new BookingAlreadyApprovedException("Данное бронирование уже подтверждено"));

        mockMvc.perform(MockMvcRequestBuilders.patch(PATH_WITH_ID + "?approved=true")
                        .header(X_SHADER_USER_ID, USER_1_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBooking))
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
        verify(bookingService, times(1)).approveBooking(BOOKING_1_ID, USER_1_ID, true);
    }
}