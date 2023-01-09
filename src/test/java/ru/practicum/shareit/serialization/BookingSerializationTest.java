package ru.practicum.shareit.serialization;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import ru.practicum.shareit.booking.dto.BookingDtoForItems;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.practicum.shareit.serialization.SerializationTestConstant.BOOKING_1_DTO_FOR_ITEMS_FILE_PATH;
import static ru.practicum.shareit.serialization.SerializationTestConstant.BOOKING_1_DTO_PATH;

@JsonTest
class BookingSerializationTest {
    private static final File BOOKING_1_DTO_FOR_ITEMS_FILE = new File(BOOKING_1_DTO_FOR_ITEMS_FILE_PATH);
    private static final File BOOKING_1_DTO_FILE = new File(BOOKING_1_DTO_PATH);

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Проверка сериализации BookingDtoForItems")
    public void bookingDtoForItemsSerialisationTest() throws IOException {
        BookingDtoForItems bookingDto = BookingDtoForItems.builder()
                .id(1L)
                .start(SerializationTestConstant.BOOKING_START_1)
                .end(SerializationTestConstant.BOOKING_END_1)
                .bookerId(1L)
                .status(BookingStatus.APPROVED)
                .build();
        String resultString = objectMapper.writeValueAsString(bookingDto);
        System.out.println("Response JSON: \n" + resultString + "\n");

        // Делал чтобы удобно сравнивать с Json в PrettyStyle
        BookingDtoForItems fileData = objectMapper.readValue(BOOKING_1_DTO_FOR_ITEMS_FILE, BookingDtoForItems.class);
        String expectedString = objectMapper.writeValueAsString(fileData);
        System.out.println("JSON from file: \n" + expectedString);

        assertEquals(expectedString, resultString);
    }


    @Test
    @DisplayName("Проверка сериализации BookingResponseDto")
    public void bookingResponseDtoSerialisationTest() throws IOException {
        BookingResponseDto bookingDto = BookingResponseDto.builder()
                .id(1L)
                .start(SerializationTestConstant.BOOKING_START_1)
                .end(SerializationTestConstant.BOOKING_END_1)
                .item(SerializationTestConstant.ITEM_1_RESPONSE_DTO)
                .booker(SerializationTestConstant.USER_1_DTO)
                .status(BookingStatus.WAITING)
                .build();
        String resultString = objectMapper.writeValueAsString(bookingDto);
        System.out.println("Response JSON: \n" + resultString + "\n");

        // Делал чтобы удобно сравнивать с Json в PrettyStyle
        BookingResponseDto fileData = objectMapper.readValue(BOOKING_1_DTO_FILE, BookingResponseDto.class);
        String expectedString = objectMapper.writeValueAsString(fileData);
        System.out.println("JSON from file: \n" + expectedString);

        assertEquals(expectedString, resultString);


    }
}
