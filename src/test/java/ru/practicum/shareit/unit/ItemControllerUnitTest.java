package ru.practicum.shareit.unit;

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
import ru.practicum.shareit.comment.dto.CommentRequestDto;
import ru.practicum.shareit.comment.dto.CommentResponseDto;
import ru.practicum.shareit.item.controller.ItemController;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@AutoConfigureMockMvc
@WebMvcTest({ItemController.class})
class ItemControllerUnitTest {
    private static final String PATH = "/items";
    private static final String PATH_WITH_ID = "/items/1";
    public static final String X_SHADER_USER_ID = "X-Sharer-User-Id";
    private static final Long ITEM_1_ID = 1L;
    private static final Long USER_1_ID = 1L;


    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ItemService itemService;

    private ItemRequestDto requestItem;
    private ItemResponseDto responseItem;


    @BeforeEach
    void setUp() {
        requestItem = TestUtility.getItemRequestDto();
        responseItem = TestUtility.getItemResponseDto();
    }

    @Test
    @DisplayName("Должен создатьи вернуть Item при корректных данных")
    void shouldCreateItem() throws Exception {
        when(itemService.createItem(any(ItemRequestDto.class), anyLong())).thenReturn(responseItem);

        mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .header(X_SHADER_USER_ID, USER_1_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestItem))
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(
                        objectMapper.writeValueAsString(responseItem)
                ));

        verify(itemService, times(1)).createItem(requestItem, USER_1_ID);
    }

    @Test
    @DisplayName("Должен вернуть предмет по itemId при корректных данных")
    void shouldGetItemById() throws Exception {
        when(itemService.getItemById(anyLong(), anyLong())).thenReturn(responseItem);

        mockMvc.perform(MockMvcRequestBuilders.get(PATH_WITH_ID)
                        .header(X_SHADER_USER_ID, USER_1_ID)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(
                        objectMapper.writeValueAsString(responseItem)
                ));

        verify(itemService, times(1)).getItemById(ITEM_1_ID, USER_1_ID);
    }

    @Test
    @DisplayName("Должен обновить и вернуть Item при корректных данных")
    void shouldUpdateItem() throws Exception {
        when(itemService.updateItem(anyLong(), any(ItemRequestDto.class), anyLong())).thenReturn(responseItem);

        mockMvc.perform(MockMvcRequestBuilders.patch(PATH_WITH_ID)
                        .header(X_SHADER_USER_ID, USER_1_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestItem))
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(
                        objectMapper.writeValueAsString(responseItem)
                ));

        verify(itemService, times(1)).updateItem(USER_1_ID, requestItem, ITEM_1_ID);
    }

    @Test
    @DisplayName("Должен вернуть список предметов Owner при корректных данных")
    void shouldGetUserItems() throws Exception {
        List<ItemResponseDto> items = List.of(responseItem);

        when(itemService.getUserItems(anyLong(), anyInt(), anyInt())).thenReturn(items);

        mockMvc.perform(MockMvcRequestBuilders.get(PATH)
                        .header(X_SHADER_USER_ID, USER_1_ID)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(
                        objectMapper.writeValueAsString(items)
                ));

        verify(itemService, times(1)).getUserItems(USER_1_ID, 0, 10);
    }


    @Test
    @DisplayName("Должен венуть список предметов по поисковому запросу")
    void shouldSearchItem() throws Exception {
        List<ItemResponseDto> items = List.of(responseItem);

        when(itemService.searchItem(anyString(), anyInt(), anyInt())).thenReturn(items);

        mockMvc.perform(MockMvcRequestBuilders.get(PATH + "/search?text=item")
                        .header(X_SHADER_USER_ID, USER_1_ID)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(
                        objectMapper.writeValueAsString(items)
                ));

        verify(itemService, times(1)).searchItem("item", 0, 10);
    }


    @Test
    @DisplayName("Должен создать и венуть Comment при корректных данных")
    void shouldCreateComment() throws Exception {
        CommentRequestDto requestComment = TestUtility.getCommentRequestDto();
        CommentResponseDto responseComment = TestUtility.getCommentResponseDto();

        when(itemService.createComment(any(CommentRequestDto.class), anyLong(), anyLong())).thenReturn(responseComment);

        mockMvc.perform(MockMvcRequestBuilders.post(PATH_WITH_ID + "/comment")
                        .header(X_SHADER_USER_ID, USER_1_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestComment))
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(
                        objectMapper.writeValueAsString(responseComment)
                ));

        verify(itemService, times(1)).createComment(requestComment, ITEM_1_ID, USER_1_ID);
    }
}