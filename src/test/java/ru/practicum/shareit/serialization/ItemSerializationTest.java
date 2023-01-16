package ru.practicum.shareit.serialization;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import ru.practicum.shareit.TestUtility;
import ru.practicum.shareit.item.dto.ItemResponseDto;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.practicum.shareit.TestUtility.ITEM_1_RESPONSE_DTO_FILE_PATH;

@JsonTest
public class ItemSerializationTest {
    private static final File FILE = new File(ITEM_1_RESPONSE_DTO_FILE_PATH);

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Проверка сериализации ItemResponseDto")
    public void itemResponseDtoSerialisationTest() throws IOException {
        ItemResponseDto itemDto = TestUtility.getItemResponseDto();
        String resultString = objectMapper.writeValueAsString(itemDto);
        System.out.println("Response JSON: \n" + resultString + "\n");

        // Делал чтобы удобно сравнивать с Json в PrettyStyle
        ItemResponseDto fileData = objectMapper.readValue(FILE, ItemResponseDto.class);
        String expectedString = objectMapper.writeValueAsString(fileData);
        System.out.println("JSON from file: \n" + expectedString);

        assertEquals(expectedString, resultString);

    }

}
