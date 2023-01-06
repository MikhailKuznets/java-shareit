package ru.practicum.shareit.serialization;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import ru.practicum.shareit.item.dto.ItemResponseDto;

import static org.junit.jupiter.api.Assertions.assertEquals;

@JsonTest
public class ItemResponseDtoSerializationTest {
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void itemResponseDtoSerialisationTest() throws Exception {
//        ItemResponseDto itemResponseDto = ItemResponseDto.builder()
//                .id(1L)
//                .name()
//                .description()
//                .available()
//                .lastBooking()
//                .nextBooking()
//                .comments()
//                .requestId()
//                .build();
//
//        String expectedJson = "{\"id\":1,\"name\":\"Иван\",\"email\":\"ivanov@yandex.ru\"}";
//
//        assertEquals(expectedJson, objectMapper.writeValueAsString(userDto));
    }
}
