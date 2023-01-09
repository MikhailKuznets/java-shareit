package ru.practicum.shareit.serialization;

import ru.practicum.shareit.booking.dto.BookingDtoForItems;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.comment.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

class SerializationTestConstant {
    // Установки даты и времени
    static final LocalDateTime BOOKING_START_1 =
            LocalDateTime.of(2023, 1, 9, 10, 10, 10);
    static final LocalDateTime BOOKING_END_1 = BOOKING_START_1.plusDays(1);
    static final LocalDateTime COMMENT_TIME = BOOKING_END_1.plusHours(1);

    // Установкка пути к тестовым JSON файлам
    static final String SEPARATOR = File.separator;
    static final String FOLDER_PATH = "src" + SEPARATOR + "test" + SEPARATOR +
            "resources" + SEPARATOR + "serialization" + SEPARATOR;

    // User 1 Dto
    static final String USER_1_DTO_FILE_NAME = "user1_dto_test.json";
    static final String USER_1_DTO_FILE_PATH = FOLDER_PATH + USER_1_DTO_FILE_NAME;

    static final UserDto USER_1_DTO = UserDto.builder()
            .id(1L)
            .email("user1@yandex.ru")
            .name("User1")
            .build();


    // Booking 1 BookingDtoForItems
    static final String BOOKING_1_DTO_FOR_ITEMS_FILE_NAME = "booking1_dto_for_items_test.json";
    static final String BOOKING_1_DTO_FOR_ITEMS_FILE_PATH = FOLDER_PATH + BOOKING_1_DTO_FOR_ITEMS_FILE_NAME;

    static final BookingDtoForItems LAST_BOOKING_DTO_FOR_ITEMS = BookingDtoForItems.builder()
            .id(2L)
            .start(SerializationTestConstant.BOOKING_START_1)
            .end(SerializationTestConstant.BOOKING_END_1)
            .bookerId(1L)
            .status(BookingStatus.APPROVED)
            .build();

    static final BookingDtoForItems NEXT_BOOKING_DTO_FOR_ITEMS = BookingDtoForItems.builder()
            .id(3L)
            .start(SerializationTestConstant.BOOKING_START_1.plusDays(2))
            .end(SerializationTestConstant.BOOKING_END_1.plusDays(2))
            .bookerId(2L)
            .status(BookingStatus.WAITING)
            .build();

    // Comment 1 CommentResponseDto
    static final CommentResponseDto COMMENT_RESPONSE_DTO = CommentResponseDto.builder()
            .id(1L)
            .text("Мой комментарий")
            .authorName("Прирожденный комментатор")
            .created(SerializationTestConstant.COMMENT_TIME)
            .build();


    // Item 1 ResponseDto
    static final String ITEM_1_RESPONSE_DTO_FILE_NAME = "item1_dto_test.json";
    static final String ITEM_1_RESPONSE_DTO_FILE_PATH = FOLDER_PATH + ITEM_1_RESPONSE_DTO_FILE_NAME;
    static final ItemResponseDto ITEM_1_RESPONSE_DTO = ItemResponseDto.builder()
            .id(1L)
            .name("Дрель")
            .description("Чтобы доставать соседей")
            .available(true)
            .lastBooking(LAST_BOOKING_DTO_FOR_ITEMS)
            .nextBooking(NEXT_BOOKING_DTO_FOR_ITEMS)
            .comments(List.of(COMMENT_RESPONSE_DTO))
            .requestId(1L)
            .build();

    // Booking 1 Dto
    static final String BOOKING_1_DTO_FILE_NAME = "booking1_dto_test.json";
    static final String BOOKING_1_DTO_PATH = FOLDER_PATH + BOOKING_1_DTO_FILE_NAME;

}
