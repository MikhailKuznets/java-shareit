package ru.practicum.shareit;

import ru.practicum.shareit.booking.dto.BookingDtoForItems;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.comment.dto.CommentRequestDto;
import ru.practicum.shareit.comment.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.request.dto.ItemReqRequestDto;
import ru.practicum.shareit.request.dto.ItemReqResponseDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

public class TestUtility {
    // Установки даты и времени
    public static final LocalDateTime BOOKING_START_1 =
            LocalDateTime.of(2023, 1, 9, 10, 10, 10);
    public static final LocalDateTime BOOKING_END_1 = BOOKING_START_1.plusDays(1);
    public static final LocalDateTime COMMENT_TIME = BOOKING_END_1.plusHours(1);
    public static final LocalDateTime REQUEST_TIME = BOOKING_START_1.minusDays(1);

    // Установка пути к тестовым JSON файлам
    public static final String SEPARATOR = File.separator;
    public static final String FOLDER_PATH = "src" + SEPARATOR + "test" + SEPARATOR +
            "resources" + SEPARATOR + "serialization" + SEPARATOR;

    // User 1 Dto
    public static final String USER_1_DTO_FILE_NAME = "user1_dto_test.json";
    public static final String USER_1_DTO_FILE_PATH = FOLDER_PATH + USER_1_DTO_FILE_NAME;

    // Booking 1 BookingDtoForItems
    public static final String BOOKING_1_DTO_FOR_ITEMS_FILE_NAME = "booking1_dto_for_items_test.json";
    public static final String BOOKING_1_DTO_FOR_ITEMS_FILE_PATH = FOLDER_PATH + BOOKING_1_DTO_FOR_ITEMS_FILE_NAME;


    // Item 1 ResponseDto
    public static final String ITEM_1_RESPONSE_DTO_FILE_NAME = "item1_dto_test.json";
    public static final String ITEM_1_RESPONSE_DTO_FILE_PATH = FOLDER_PATH + ITEM_1_RESPONSE_DTO_FILE_NAME;

    // Booking 1 Dto
    public static final String BOOKING_1_DTO_FILE_NAME = "booking1_dto_test.json";
    public static final String BOOKING_1_DTO_PATH = FOLDER_PATH + BOOKING_1_DTO_FILE_NAME;

    // Booking 1 Dto
    public static final String REQUEST_1_DTO_FILE_NAME = "request1_dto_test.json";
    public static final String REQUEST_1_DTO_PATH = FOLDER_PATH + REQUEST_1_DTO_FILE_NAME;

    // User
    public static User getUser() {
        return User.builder()
                .id(1L)
                .email("user1@yandex.ru")
                .name("User1")
                .build();
    }

    public static UserDto getUserDto() {
        return UserDto.builder()
                .id(1L)
                .email("user1@yandex.ru")
                .name("User1")
                .build();
    }

    // Booking
    public static BookingRequestDto getLastBookingRequestDto() {
        return BookingRequestDto.builder()
                .itemId(1L)
                .start(TestUtility.BOOKING_START_1)
                .end(TestUtility.BOOKING_END_1)
                .build();
    }

    public static BookingResponseDto getLastBookingResponseDto() {
        return BookingResponseDto.builder()
                .id(1L)
                .start(TestUtility.BOOKING_START_1)
                .end(TestUtility.BOOKING_END_1)
                .item(TestUtility.getItemResponseDto())
                .booker(TestUtility.getUserDto())
                .status(BookingStatus.WAITING)
                .build();
    }

    public static BookingDtoForItems getLastBookingDtoForItems() {
        return BookingDtoForItems.builder()
                .id(1L)
                .start(TestUtility.BOOKING_START_1)
                .end(TestUtility.BOOKING_END_1)
                .bookerId(1L)
                .status(BookingStatus.APPROVED)
                .build();
    }

    public static BookingRequestDto getNextBookingRequestDto() {
        return BookingRequestDto.builder()
                .itemId(2L)
                .start(TestUtility.BOOKING_START_1.plusMonths(1))
                .end(TestUtility.BOOKING_END_1.plusMonths(1))
                .build();
    }

    public static BookingResponseDto getNextBookingResponseDto() {
        return BookingResponseDto.builder()
                .id(2L)
                .start(TestUtility.BOOKING_START_1.plusMonths(1))
                .end(TestUtility.BOOKING_END_1.plusMonths(1))
                .item(TestUtility.getItemResponseDto())
                .booker(TestUtility.getUserDto())
                .status(BookingStatus.WAITING)
                .build();
    }

    public static BookingDtoForItems getNextBookingDtoForItems() {
        return BookingDtoForItems.builder()
                .id(2L)
                .start(TestUtility.BOOKING_START_1.plusMonths(1))
                .end(TestUtility.BOOKING_END_1.plusMonths(1))
                .bookerId(2L)
                .status(BookingStatus.WAITING)
                .build();
    }


    // Comment
    public static CommentRequestDto getCommentRequestDto() {
        return CommentRequestDto.builder()
                .text("Мой комментарий")
                .build();
    }

    public static CommentResponseDto getCommentResponseDto() {
        return CommentResponseDto.builder()
                .id(1L)
                .text("Мой комментарий")
                .authorName("Прирожденный комментатор")
                .created(TestUtility.COMMENT_TIME)
                .build();
    }

    // Item
    public static ItemRequestDto getItemRequestDto() {
        return ItemRequestDto.builder()
                .name("Дрель")
                .description("Чтобы доставать соседей")
                .available(true)
                .requestId(1L)
                .build();
    }

    public static ItemResponseDto getItemResponseDto() {
        return ItemResponseDto.builder()
                .id(1L)
                .name("Дрель")
                .description("Чтобы доставать соседей")
                .available(true)
                .lastBooking(getLastBookingDtoForItems())
                .nextBooking(getNextBookingDtoForItems())
                .comments(List.of(getCommentResponseDto()))
                .requestId(1L)
                .build();
    }

    // Request
    public static ItemReqRequestDto getItemReqRequestDto() {
        return ItemReqRequestDto.builder()
                .description("Нужна дрель")
                .build();
    }

    public static ItemReqResponseDto getItemReqResponseDto() {
        return ItemReqResponseDto.builder()
                .id(1L)
                .description("Нужна дрель")
                .created(REQUEST_TIME)
                .items(List.of(getItemResponseDto()))
                .build();
    }

}
