package ru.practicum.shareit.request.dto;

import lombok.Data;
import ru.practicum.shareit.item.dto.ItemDtoForRequestDto;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ItemReqResponseDto {
    private final Long id;
    private final String description;
    private final LocalDateTime created;
    private List<ItemDtoForRequestDto> items;
}
