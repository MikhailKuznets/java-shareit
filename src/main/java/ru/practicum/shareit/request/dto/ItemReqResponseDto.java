package ru.practicum.shareit.request.dto;

import lombok.Data;
import ru.practicum.shareit.item.dto.ItemResponseDto;

import java.time.LocalDateTime;
import java.util.Collection;

@Data
public class ItemReqResponseDto {
    private final Long id;
    private final String description;
    private final LocalDateTime created;
    private Collection<ItemResponseDto> items;
}
