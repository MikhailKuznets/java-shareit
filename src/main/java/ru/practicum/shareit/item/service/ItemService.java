package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemService {
    Collection<ItemResponseDto> getUserItems(Long userId);

    ItemResponseDto getItemById(Long itemId, Long UserId);

    ItemResponseDto createItem(Item item, Long userId);

    ItemResponseDto updateItem(Long userId, Item item, Long itemId);

    Collection<ItemResponseDto> searchItem(String text);
}
