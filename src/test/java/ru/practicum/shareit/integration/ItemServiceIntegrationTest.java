package ru.practicum.shareit.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.TestUtility;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.comment.dto.CommentRequestDto;
import ru.practicum.shareit.comment.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest()
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ItemServiceIntegrationTest {
    @Autowired
    private ItemServiceImpl itemService;
    @Autowired
    private UserServiceImpl userService;

    private final static Long OWNER_ID = 1L;
    private final static Long ITEM_ID = 1L;
    private final static Long COMMENT_ID = 1L;
    private final static Long BOOKING_ID = 1L;


    private ItemRequestDto requestItem;
    private ItemResponseDto item;
    private ItemResponseDto expectedResponseItem;

    private UserDto owner;
    private UserDto booker;

    private BookingResponseDto booking;

    private List<ItemResponseDto> items;
    private List<ItemResponseDto> expectedItems;


    @BeforeEach
    void setUp() {
        // Users
        User requestOwner = TestUtility.getUser1();
        assertNotNull(requestOwner);

        owner = userService.createUser(requestOwner);
        assertNotNull(owner);

        //Items
        requestItem = TestUtility.getItemRequestDto();
        assertNotNull(requestItem);

        expectedResponseItem = ItemResponseDto.builder()
                .id(ITEM_ID)
                .name(requestItem.getName())
                .description(requestItem.getDescription())
                .available(true)
                .comments(Collections.EMPTY_LIST)
                .build();
    }

    @Test
    @DisplayName("Должен создать и вернуть Item при корректных данных")
    void shouldCreateItem() {
        item = itemService.createItem(requestItem, OWNER_ID);
        assertNotNull(item);
        assertEquals(expectedResponseItem, item);
    }

    @Test
    @DisplayName("Должен вернуть Item по itemId при корректных данных")
    void shouldGetItemById() {
        // Создаем Item
        item = itemService.createItem(requestItem, OWNER_ID);
        assertNotNull(item);
        assertEquals(expectedResponseItem, item);

        item = itemService.getItemById(ITEM_ID, OWNER_ID);
        assertNotNull(item);
        assertEquals(expectedResponseItem, item);
    }

    @Test
    @DisplayName("Должен обновить и вернуть Item при корректных данных")
    void shouldUpdateItem() {
        // Создаем Item
        item = itemService.createItem(requestItem, OWNER_ID);
        assertNotNull(item);
        assertEquals(expectedResponseItem, item);
        ItemRequestDto requestUpdatedItem = ItemRequestDto.builder()
                .name("Обновленная Дрель")
                .description("Чтобы обновленно доставать соседей")
                .available(true)
                .build();

        item = itemService.updateItem(OWNER_ID, requestUpdatedItem, ITEM_ID);
        assertNotNull(item);

        ItemResponseDto expectedResponseItem = ItemResponseDto.builder()
                .id(ITEM_ID)
                .name(requestUpdatedItem.getName())
                .description(requestUpdatedItem.getDescription())
                .available(true)
                .build();
        assertEquals(expectedResponseItem, item);

    }

    @Test
    @DisplayName("Должен вернуть список Items пользователя при корректных данных")
    void shouldGetUserItems() {
        // Создаем Item
        item = itemService.createItem(requestItem, OWNER_ID);
        assertNotNull(item);
        assertEquals(expectedResponseItem, item);
        item = itemService.getItemById(ITEM_ID, OWNER_ID);
        assertNotNull(item);
        assertEquals(expectedResponseItem, item);

        items = new ArrayList<>(itemService.getUserItems(OWNER_ID, 0, 20));
        expectedItems = List.of(item);
        assertNotNull(items);
        assertEquals(1, items.size());
        assertEquals(expectedItems, items);
    }


    @Test
    @DisplayName("Должен вернуть список Items о запросу при корректных данных")
    void shouldSearchItem() {
        // Создаем Item
        item = itemService.createItem(requestItem, OWNER_ID);
        assertNotNull(item);
        assertEquals(expectedResponseItem, item);
        item = itemService.getItemById(ITEM_ID, OWNER_ID);
        assertNotNull(item);
        assertEquals(expectedResponseItem, item);

        items = new ArrayList<>(itemService.searchItem("Дрель", 0, 20));
        expectedItems = List.of(item);
        assertNotNull(items);
        assertEquals(1, items.size());
        assertEquals(expectedItems, items);
    }

    @Test
    void shouldCreateComment() {
        // Создаем Item
        item = itemService.createItem(requestItem, OWNER_ID);
        assertNotNull(item);
        assertEquals(expectedResponseItem, item);
        item = itemService.getItemById(ITEM_ID, OWNER_ID);
        assertNotNull(item);
        assertEquals(expectedResponseItem, item);

        CommentRequestDto requestComment = TestUtility.getCommentRequestDto();
        LocalDateTime created = LocalDateTime.now().withNano(0);
        CommentResponseDto comment = itemService.createComment(requestComment, ITEM_ID, OWNER_ID);

        assertNotNull(comment);
        assertEquals(COMMENT_ID, COMMENT_ID);
        assertEquals(requestComment.getText(), comment.getText());
        assertEquals(owner.getName(), comment.getAuthorName());
        assertEquals(created, comment.getCreated().withNano(0));


    }

    @Test
    void shouldSetBookings() {
        item = itemService.createItem(requestItem, OWNER_ID);
        assertNotNull(item);
        assertEquals(expectedResponseItem, item);
        item = itemService.getItemById(ITEM_ID, OWNER_ID);
        assertNotNull(item);
        assertEquals(expectedResponseItem, item);


    }

    @Test
    void shouldSetComments() {
    }
}