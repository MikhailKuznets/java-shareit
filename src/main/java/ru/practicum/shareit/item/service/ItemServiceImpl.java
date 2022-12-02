package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.InvalidIdException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemStorage itemStorage;
    private final UserStorage userStorage;

    @Override
    public Collection<ItemDto> findAllItems() {
        return null;
    }

    @Override
    public Optional<ItemDto> findItemById(Long itemId) {
        return Optional.empty();
    }

    @Override
    public ItemDto createItem(Item item, Long userId) {
        if (!userStorage.isContainsUserId(userId)) {
            throw new InvalidIdException("Невозможно добавить предмет т.к. собственника с id = " + userId +
                    " не существует");
        }
        User owner = userStorage.findUserById(userId).get();
        return ItemMapper.toItemDto(itemStorage.createItem(item, owner));
    }

    @Override
    public ItemDto updateItem(Long userId, Item item, Long itemId) {
        return ItemMapper.toItemDto(itemStorage.updateItem(userId, item, itemId));
    }
}
