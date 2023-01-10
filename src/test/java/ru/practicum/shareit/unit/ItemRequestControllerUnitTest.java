package ru.practicum.shareit.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.controller.BookingController;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.request.controller.ItemRequestController;
import ru.practicum.shareit.request.service.ItemRequestService;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureMockMvc
@WebMvcTest({ItemRequestController.class})
class ItemRequestControllerUnitTest {
    private static final String PATH = "/requests";
    private static final String PATH_WITH_ID = "/bookings/1";
    public static final String X_SHADER_USER_ID = "X-Sharer-User-Id";
    private static final Long REQUEST_1_ID = 1L;
    private static final Long USER_1_ID = 1L;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ItemRequestService requestService;

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("Должен создать Request при корректных данных")
    void createRequest() {
    }

    @Test
    void getUserAllRequests() {
    }

    @Test
    void getRequestById() {
    }

    @Test
    void getAllRequestsByOtherUsers() {
    }
}