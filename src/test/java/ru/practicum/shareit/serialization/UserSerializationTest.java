package ru.practicum.shareit.serialization;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import ru.practicum.shareit.TestUtility;
import ru.practicum.shareit.user.dto.UserDto;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.practicum.shareit.TestUtility.USER_1_DTO_FILE_PATH;

@JsonTest
public class UserSerializationTest {
    private static final File FILE = new File(USER_1_DTO_FILE_PATH);

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Проверка сериализации UserDto")
    public void userDtoSerialisationTest() throws IOException {
        UserDto userDto = TestUtility.getUser1Dto();

        String resultString = objectMapper.writeValueAsString(userDto);
        System.out.println("Response JSON: \n" + resultString + "\n");

        // Делал чтобы удобно сравнивать с Json в PrettyStyle
        UserDto fileData = objectMapper.readValue(FILE, UserDto.class);
        String expectedString = objectMapper.writeValueAsString(fileData);
        System.out.println("JSON from file: \n" + expectedString);

        assertEquals(expectedString, resultString);
    }

}
