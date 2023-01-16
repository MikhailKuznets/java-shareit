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
import ru.practicum.shareit.request.controller.ItemRequestController;
import ru.practicum.shareit.request.dto.ItemReqRequestDto;
import ru.practicum.shareit.request.dto.ItemReqResponseDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@AutoConfigureMockMvc
@WebMvcTest({ItemRequestController.class})
class ItemRequestControllerRestTest {
    private static final String PATH = "/requests";
    private static final String PATH_WITH_ID = "/requests/1";
    public static final String X_SHADER_USER_ID = "X-Sharer-User-Id";
    private static final Long REQUEST_1_ID = 1L;
    private static final Long USER_1_ID = 1L;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ItemRequestService requestService;

    private ItemReqRequestDto requestItemReq;
    private ItemReqResponseDto responseItemReq;


    @BeforeEach
    void setUp() {
        requestItemReq = TestUtility.getItemReqRequestDto();
        responseItemReq = TestUtility.getItemReqResponseDto();
    }

    @Test
    @DisplayName("Должен создать Request при корректных данных")
    void shouldCreateRequest() throws Exception {
        when(requestService.createRequest(any(ItemReqRequestDto.class), anyLong())).thenReturn(responseItemReq);

        mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .header(X_SHADER_USER_ID, USER_1_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestItemReq))
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(
                        objectMapper.writeValueAsString(responseItemReq)
                ));

        verify(requestService, times(1)).createRequest(requestItemReq, USER_1_ID);
    }

    @Test
    @DisplayName("Должен вернуть список Request пользователя userId при корректных данных")
    void shouldGetUserAllRequests() throws Exception {
        List<ItemReqResponseDto> requests = List.of(responseItemReq);

        when(requestService.getUserAllRequests(anyLong())).thenReturn(requests);

        mockMvc.perform(MockMvcRequestBuilders.get(PATH)
                        .header(X_SHADER_USER_ID, USER_1_ID)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(
                        objectMapper.writeValueAsString(requests)
                ));

        verify(requestService, times(1)).getUserAllRequests(USER_1_ID);
    }

    @Test
    @DisplayName("Должен вернуть Request по id при корректных данных")
    void shouldGetRequestById() throws Exception {
        when(requestService.getRequestById(anyLong(), anyLong())).thenReturn(responseItemReq);

        mockMvc.perform(MockMvcRequestBuilders.get(PATH_WITH_ID)
                        .header(X_SHADER_USER_ID, USER_1_ID)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(
                        objectMapper.writeValueAsString(responseItemReq)
                ));

        verify(requestService, times(1)).getRequestById(REQUEST_1_ID, USER_1_ID);
    }

    @Test
    @DisplayName("Должен вернуть список Request созданных лругими пользователями при корректных данных")
    void shouldGetAllRequestsByOtherUsers() throws Exception {
        List<ItemReqResponseDto> requests = List.of(responseItemReq);
        Long otherUserId = 2L;

        when(requestService.getAllRequestByOtherUsers(anyLong(), anyInt(), anyInt())).thenReturn(requests);

        mockMvc.perform(MockMvcRequestBuilders.get(PATH + "/all?from=0&size=")
                        .header(X_SHADER_USER_ID, otherUserId)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(
                        objectMapper.writeValueAsString(requests)
                ));

        verify(requestService, times(1)).getAllRequestByOtherUsers(otherUserId, 0, 10);
    }
}