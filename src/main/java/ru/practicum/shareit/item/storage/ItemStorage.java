package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

public interface ItemStorage {
    Item createItem(Item item, User Owner);

    Item updateItem(Long userId, Item item, Long itemId);
}
