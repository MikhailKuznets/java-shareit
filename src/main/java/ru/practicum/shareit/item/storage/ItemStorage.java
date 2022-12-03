package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.util.Collection;
import java.util.Optional;

public interface ItemStorage {

    Optional<Item> getItemById(Long itemId);

    Collection<Item> getUserItems(Long userId);

    Item createItem(Item item, User owner);

    Item updateItem(Long userId, Item item, Long itemId);

    Collection<Item> searchItem(String text);
}
