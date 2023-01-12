package ru.practicum.shareit.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.TestUtility;
import ru.practicum.shareit.request.dto.ItemReqRequestDto;
import ru.practicum.shareit.request.dto.ItemReqResponseDto;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest()
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ItemRequestServiceIntegrationTest {
    @Autowired
    ItemRequestService requestService;
    @Autowired
    UserService userService;
    private final static Long REQUEST_ID = 1L;
    private final static Long OWNER_ID = 1L;
    private final static Long REQUESTER_ID = 2L;
    private final static String DESCRIPTION = "Нужна дрель";

    private ItemReqRequestDto requestItemRequest;
    private ItemReqResponseDto responseItemRequest;
    private List<ItemReqResponseDto> requests;
    private List<ItemReqResponseDto> expectedRequests;


    @BeforeEach
    void setUp() {
        // Users
        User ownerRequest = TestUtility.getUser1();
        User bookerRequest = TestUtility.getUser2();
        assertNotNull(ownerRequest);
        assertNotNull(bookerRequest);

        UserDto ownerResponse = userService.createUser(ownerRequest);
        UserDto bookerResponse = userService.createUser(bookerRequest);
        assertNotNull(ownerResponse);
        assertNotNull(bookerResponse);

        UserDto expectedOwnerResponse = TestUtility.getUser1Dto();
        UserDto expectedBookerResponse = TestUtility.getUser2Dto();
        assertEquals(expectedOwnerResponse, ownerResponse);
        assertEquals(expectedBookerResponse, bookerResponse);
        // Booking
        requestItemRequest = TestUtility.getItemReqRequestDto();
    }

    @Test
    @DisplayName("Должен создать и вернуть Request при корректных данных")
    void shouldCreateRequest() {
        LocalDateTime now = LocalDateTime.now().withNano(0);

        responseItemRequest = requestService.createRequest(requestItemRequest, REQUESTER_ID);
        LocalDateTime created = responseItemRequest.getCreated().withNano(0);

        assertNotNull(responseItemRequest);
        assertEquals(REQUEST_ID, responseItemRequest.getId());
        assertEquals(DESCRIPTION, responseItemRequest.getDescription());
        assertEquals(Collections.EMPTY_LIST, responseItemRequest.getItems());
        assertEquals(now, created);
    }

    @Test
    @DisplayName("Должен вернуть Request по requestId при корректных данных")
    void shouldGetRequestById() {
        // Создаем Request
        LocalDateTime now = LocalDateTime.now().withNano(0);
        responseItemRequest = requestService.createRequest(requestItemRequest, REQUESTER_ID);
        LocalDateTime created = responseItemRequest.getCreated().withNano(0);
        assertNotNull(responseItemRequest);
        assertEquals(REQUEST_ID, responseItemRequest.getId());
        assertEquals(DESCRIPTION, responseItemRequest.getDescription());
        assertEquals(Collections.EMPTY_LIST, responseItemRequest.getItems());
        assertEquals(now, created);

        responseItemRequest = requestService.getRequestById(REQUEST_ID, REQUESTER_ID);
        assertNotNull(responseItemRequest);
        assertEquals(REQUEST_ID, responseItemRequest.getId());
        assertEquals(DESCRIPTION, responseItemRequest.getDescription());
        assertEquals(Collections.EMPTY_LIST, responseItemRequest.getItems());
        assertEquals(now, created);
    }

    @Test
    @DisplayName("Должен вернуть список собственных Requests определенного User при корректных данных")
    void shouldGetUserAllRequests() {
        // Создаем Request
        LocalDateTime now = LocalDateTime.now().withNano(0);
        responseItemRequest = requestService.createRequest(requestItemRequest, REQUESTER_ID);
        LocalDateTime created = responseItemRequest.getCreated().withNano(0);
        assertNotNull(responseItemRequest);
        assertEquals(REQUEST_ID, responseItemRequest.getId());
        assertEquals(DESCRIPTION, responseItemRequest.getDescription());
        assertEquals(Collections.EMPTY_LIST, responseItemRequest.getItems());
        assertEquals(now, created);

        requests = new ArrayList<>(requestService.getUserAllRequests(REQUESTER_ID));
        expectedRequests = List.of(responseItemRequest);

        assertNotNull(requests);
        assertEquals(1, requests.size());
        assertNotNull(expectedRequests.get(0));
        assertEquals(REQUEST_ID, expectedRequests.get(0).getId());
        assertEquals(DESCRIPTION, expectedRequests.get(0).getDescription());
        assertEquals(Collections.EMPTY_LIST, expectedRequests.get(0).getItems());
        assertEquals(now, expectedRequests.get(0).getCreated().withNano(0));
    }

    @Test
    @DisplayName("Должен вернуть список Requests других Users при корректных данных")
    void shouldGetAllRequestByOtherUsers() {
        // Создаем Request
        LocalDateTime now = LocalDateTime.now().withNano(0);
        responseItemRequest = requestService.createRequest(requestItemRequest, REQUESTER_ID);
        LocalDateTime created = responseItemRequest.getCreated().withNano(0);
        assertNotNull(responseItemRequest);
        assertEquals(REQUEST_ID, responseItemRequest.getId());
        assertEquals(DESCRIPTION, responseItemRequest.getDescription());
        assertEquals(Collections.EMPTY_LIST, responseItemRequest.getItems());
        assertEquals(now, created);

        requests = new ArrayList<>(requestService.getAllRequestByOtherUsers(OWNER_ID, 0, 20));
        expectedRequests = List.of(responseItemRequest);

        assertNotNull(requests);
        assertEquals(1, requests.size());
        assertNotNull(expectedRequests.get(0));
        assertEquals(REQUEST_ID, expectedRequests.get(0).getId());
        assertEquals(DESCRIPTION, expectedRequests.get(0).getDescription());
        assertEquals(Collections.EMPTY_LIST, expectedRequests.get(0).getItems());
        assertEquals(now, expectedRequests.get(0).getCreated().withNano(0));
    }
}