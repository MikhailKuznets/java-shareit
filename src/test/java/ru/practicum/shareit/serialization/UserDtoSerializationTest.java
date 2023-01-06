package ru.practicum.shareit.serialization;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import ru.practicum.shareit.user.dto.UserDto;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

@JsonTest
public class UserDtoSerializationTest {

    private static final String SEPARATOR = File.separator;
    private static final String FILE_PATH = "src" + SEPARATOR + "test" + SEPARATOR +
            "resources" + SEPARATOR + "serialization" + SEPARATOR + "itemResponseDtoSerialization.json";

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void userDtoSerialisationTest() throws Exception {
        UserDto userDto = UserDto.builder()
                .id(1L)
                .email("user1@yandex.ru")
                .name("User1")
                .build();

        String expectedJson = Files.readString(Path.of(FILE_PATH));
        assertEquals(expectedJson, objectMapper.writeValueAsString(userDto));
    }

}
