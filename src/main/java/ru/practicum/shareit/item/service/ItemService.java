package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.Optional;

public interface ItemService {
    Collection<ItemDto> findAllItems();

    Optional<ItemDto> findItemById(Long ItemId);

    ItemDto createItem(Item item);

    ItemDto updateItem(Long ItemId, Item item);

    void deleteItemById(Long ItemId);
}
