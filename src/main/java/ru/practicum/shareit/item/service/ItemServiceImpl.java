package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.InvalidIdException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemStorage itemStorage;
    private final UserStorage userStorage;
    private final ItemMapper itemMapper;

    @Override
    public Collection<ItemDto> getUserItems(Long userId) {
        return itemStorage.getUserItems(userId).stream().map(itemMapper::toItemDto).collect(Collectors.toList());
    }

    @Override
    public ItemDto getItemById(Long itemId) {
        return itemMapper.toItemDto(itemStorage.getItemById(itemId));
    }

    @Override
    public ItemDto createItem(Item item, Long userId) {
        if (!userStorage.isContainsUserId(userId)) {
            throw new InvalidIdException("Невозможно добавить предмет т.к. собственника с id = " + userId +
                    " не существует");
        }
        User owner = userStorage.getUserById(userId);
        return itemMapper.toItemDto(itemStorage.createItem(item, owner));
    }

    @Override
    public ItemDto updateItem(Long userId, Item item, Long itemId) {
        return itemMapper.toItemDto(itemStorage.updateItem(userId, item, itemId));
    }

    @Override
    public Collection<ItemDto> searchItem(String text) {
        return itemStorage.searchItem(text).stream().map(itemMapper::toItemDto).collect(Collectors.toList());
    }
}
