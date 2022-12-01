package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService{
    @Override
    public Collection<ItemDto> findAllItems() {
        return null;
    }

    @Override
    public Optional<ItemDto> findItemById(Long ItemId) {
        return Optional.empty();
    }

    @Override
    public ItemDto createItem(Item item) {
        return null;
    }

    @Override
    public ItemDto updateItem(Long ItemId, Item item) {
        return null;
    }

    @Override
    public void deleteItemById(Long ItemId) {

    }
}
