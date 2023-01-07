package ru.practicum.shareit.serialization;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;

@JsonTest
public class ItemResponseDtoSerializationTest {
    @Autowired
    private ObjectMapper objectMapper;

}
