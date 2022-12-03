package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.model.Item;

import java.util.Optional;

public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .build();
    }

    public static Optional<ItemDto> toOptionalItemDto(Optional<Item> item) {
        return item.map(ItemMapper::toItemDto);
    }
}
