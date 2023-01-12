package ru.practicum.shareit.serialization;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import ru.practicum.shareit.TestUtility;
import ru.practicum.shareit.request.dto.ItemReqResponseDto;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.practicum.shareit.TestUtility.REQUEST_1_DTO_PATH;

@JsonTest
public class ItemRequestSerializationTest {
    private static final File FILE = new File(REQUEST_1_DTO_PATH);

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Проверка сериализации ItemReqResponseDto")
    public void itemReqResponseDtoSerializationTest() throws IOException {
        ItemReqResponseDto requestDto = TestUtility.getItemReqResponseDto();

        String resultString = objectMapper.writeValueAsString(requestDto);
        System.out.println("Response JSON: \n" + resultString + "\n");

        // Делал чтобы удобно сравнивать с Json в PrettyStyle
        ItemReqResponseDto fileData = objectMapper.readValue(FILE, ItemReqResponseDto.class);
        String expectedString = objectMapper.writeValueAsString(fileData);
        System.out.println("JSON from file: \n" + expectedString);

        assertEquals(expectedString, resultString);
    }

}
