package ru.practicum.shareit.item.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exceptions.InvalidIdException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.Collection;

@RestController
@RequestMapping(path = "/items")
@Slf4j
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @GetMapping
    public Collection<ItemDto> getUserItems(@RequestHeader("X-Sharer-User-Id") @Positive Long userId) {
        return itemService.getUserItems(userId);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@RequestHeader("X-Sharer-User-Id") @Positive Long userId,
                               @PathVariable @Positive Long itemId) {
        log.info("Получен запрос GET /items/{}. От пользователя id = {}.",
                itemId, userId);
        return itemService.getItemById(itemId).orElseThrow(
                () -> new InvalidIdException("К сожалению, вещи с id " + itemId + " нет."));
    }

    @GetMapping("/search")
    public Collection<ItemDto> searchItem(@RequestHeader("X-Sharer-User-Id") @Positive Long userId,
                               @RequestParam("text") String text) {
        log.info("Получен запрос GET /items/search?text={}. От пользователя id = {}.",
                text, userId);
        return itemService.searchItem(text);
    }

    @PostMapping
    public ItemDto createItem(@RequestHeader("X-Sharer-User-Id") @Positive Long userId,
                              @RequestBody @Valid Item item) {
        log.info("Получен запрос Post /items. От пользователя id = {}, добавить вещь: {}", userId, item);
        return itemService.createItem(item, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") @Positive Long userId,
                              @PathVariable @Positive Long itemId,
                              @RequestBody Item item) {
        log.info("Получен запрос Patch /items/{}. От пользователя id = {}, обновить данные вещи {}.",
                itemId, userId, item);
        return itemService.updateItem(userId, item, itemId);
    }

}
