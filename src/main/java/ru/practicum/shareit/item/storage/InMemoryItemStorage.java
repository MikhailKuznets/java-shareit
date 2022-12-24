package ru.practicum.shareit.item.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exceptions.InvalidIdException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Deprecated
@Repository
@Slf4j
public class InMemoryItemStorage implements ItemStorage {
    private long currentItemId = 1L;
    private final Map<Long, Item> items = new HashMap<>();

    @Override
    public Item getItemById(Long itemId) {
        if (items.containsKey(itemId)) {
            return items.get(itemId);
        } else {
            throw new InvalidIdException("К сожалению, вещи с id " + itemId + " нет.");
        }
    }

    @Override
    public Collection<Item> getUserItems(Long userId) {
        return items.values().stream()
                .filter(x -> x.getOwner().getId().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public Item createItem(Item item, User owner) {
        item.setOwner(owner);
        item.setId(currentItemId);
        items.put(currentItemId++, item);
        return item;
    }

    @Override
    public Item updateItem(Long userId, Item item, Long itemId) {
        Item selectedItem = items.get(itemId);
        Long currentItemUserId = selectedItem.getOwner().getId();

        if (!currentItemUserId.equals(userId)) {
            throw new InvalidIdException("Невозможно обновить данные предмета т.к. указан неверный собственник" +
                    " с id = " + userId);
        }

        String updatedName = item.getName();
        if (updatedName != null) {
            selectedItem.setName(updatedName);
        }

        String updatedDescription = item.getDescription();
        if (updatedDescription != null) {
            selectedItem.setDescription(updatedDescription);
        }

        Boolean updatedAvailable = item.getAvailable();
        if (updatedAvailable != null) {
            selectedItem.setAvailable(updatedAvailable);
        }

        items.put(itemId, selectedItem);
        return selectedItem;
    }

    @Override
    public Collection<Item> searchItem(String text) {
        if (text.isBlank()) {
            log.warn("Задан пустой поисковый запрос.");
            return Collections.emptyList();
        }
        String lowerText = text.toLowerCase();

        return items.values().stream()
                .filter(Item::getAvailable)
                .filter(item -> item.getName().toLowerCase().contains(lowerText)
                        || item.getDescription().toLowerCase().contains(lowerText))
                .collect(Collectors.toList());
    }

}
