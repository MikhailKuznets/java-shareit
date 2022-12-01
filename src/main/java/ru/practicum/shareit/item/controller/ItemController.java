package ru.practicum.shareit.item.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
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

    @PostMapping
    public ItemDto createItem(@RequestHeader("X-Sharer-User-Id") @Positive Long userId,
                              @RequestBody @Valid Item item) {
        log.info("Получен запрос Post /items. От пользователя id = {}, добавить вещь: {}", userId, item);
        return itemService.createItem(item);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") @Positive Long userId,
                              @PathVariable @Positive Long itemId,
                              @RequestBody @Valid Item item) {
        log.info("Получен запрос Patch /items/{}. От пользователя id = {}, обновить данные вещи {}.",
                userId, userId, item);
        return itemService.updateItem(userId, item);
    }

    @GetMapping
    public Collection<ItemDto> getAllItems(@RequestHeader("X-Sharer-User-Id") @Positive Long userId) {
        return itemService.findAllItems();
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@RequestHeader("X-Sharer-User-Id") @Positive Long userId,
                               @PathVariable @Positive Long itemId,
                               @RequestBody @Valid Item item) {
        log.info("Получен запрос Patch /items/{}. От пользователя id = {}, обновить данные вещи {}.",
                userId, userId, item);
        return itemService.updateItem(userId, item);
    }


}
