package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.InvalidIdException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ItemMapper itemMapper;

    @Override
    public Collection<ItemDto> getUserItems(Long userId) {
        Collection<Item> userItems = itemRepository.findAllByOwnerIdOrderByIdAsc(userId);
        return userItems.stream().map(itemMapper::toItemDto).collect(Collectors.toList());
    }

    @Override
    public ItemDto getItemById(Long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> {
            throw new InvalidIdException("К сожалению, вещи с id " + itemId + " нет.");
        });
        return itemMapper.toItemDto(item);
    }

    @Override
    public ItemDto createItem(Item item, Long userId) {
        User owner = userRepository.findById(userId).orElseThrow(() -> {
            throw new InvalidIdException("Невозможно добавить предмет т.к. собственника с id = " + userId +
                    " не существует");
        });
        item.setOwner(owner);
        return itemMapper.toItemDto(itemRepository.save(item));
    }

    @Override
    public ItemDto updateItem(Long userId, Item item, Long itemId) {
        User user = userRepository.findById(userId).orElseThrow(() -> {
            throw new InvalidIdException("Невозможно добавить предмет т.к. собственника с id = " + userId +
                    " не существует");
        });

        Item selectedItem = itemRepository.findById(itemId).orElseThrow(() -> {
            throw new InvalidIdException("Невозможно добавить предмет т.к. предмета с id = " + itemId +
                    " не существует");
        });

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
        return itemMapper.toItemDto(itemRepository.save(selectedItem));
    }

    @Override
    public Collection<ItemDto> searchItem(String text) {
        if (text.isBlank()) {
            log.warn("Задан пустой поисковый запрос.");
            return Collections.emptyList();
        }
        String lowerText = text.toLowerCase();
        Collection<Item> items = itemRepository.searchItemByText(lowerText);
        return items.stream().map(itemMapper::toItemDto).collect(Collectors.toList());

    }
}
