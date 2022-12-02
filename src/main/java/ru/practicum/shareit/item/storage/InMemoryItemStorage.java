package ru.practicum.shareit.item.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exceptions.InvalidIdException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.util.HashMap;
import java.util.Map;

@Repository
@Slf4j
public class InMemoryItemStorage implements ItemStorage {
    private long currentItemId = 1L;
    private final Map<Long, Item> items = new HashMap<>();

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
}
