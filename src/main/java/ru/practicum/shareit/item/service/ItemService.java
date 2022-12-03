package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.Optional;

public interface ItemService {
    Collection<ItemDto> getUserItems(Long userId);

    Optional<ItemDto> getItemById(Long itemId);

    ItemDto createItem(Item item, Long userId);

    ItemDto updateItem(Long userId, Item item, Long itemId);

    Collection<ItemDto> searchItem(String text);
}
