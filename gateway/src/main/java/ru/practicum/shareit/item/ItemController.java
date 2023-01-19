package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
    private final ItemClient itemClient;


    @PostMapping
    public ResponseEntity<Object> createItem(@RequestHeader("X-Sharer-User-Id") @Positive Long ownerId,
                                             @RequestBody @Valid ItemRequestDto itemDto) {
        log.info("Получен запрос Post /items . От пользователя id = {}, добавить вещь: {}", ownerId, itemDto);
        return itemClient.createItem(ownerId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemById(@RequestHeader("X-Sharer-User-Id") @Positive Long userId,
                                              @PathVariable @Positive Long itemId) {
        log.info("Получен запрос Get /items/{} . От пользователя id = {}, найти предмет по itemId = {}.",
                itemId, userId, itemId);
        return itemClient.getItemById(itemId, userId);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@RequestHeader("X-Sharer-User-Id") @Positive Long userId,
                                             @PathVariable @Positive Long itemId,
                                             @RequestBody ItemRequestDto itemDto) {
        log.info("Получен запрос Patch /items/{} . От пользователя id = {}, " +
                        "обновить у предмета itemId = {}, данные: {}",
                itemId, userId, itemId, itemDto);
        return itemClient.updateItem(userId, itemDto, itemId);
    }

    @GetMapping
    public ResponseEntity<Object> getUserItems(
            @RequestHeader("X-Sharer-User-Id") @Positive Long ownerId,
            @RequestParam(defaultValue = "0", required = false) @PositiveOrZero Integer from,
            @RequestParam(defaultValue = "10", required = false) @Positive Integer size) {
        log.info("Получен запрос GET /items . От пользователя id = {} на просмотр собственных предметов. " +
                        "Отображать по {} предметов на странице, начиная с itemId = {}.",
                ownerId, size, from);
        return itemClient.getUserItems(ownerId, from, size);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItem(
            @RequestHeader("X-Sharer-User-Id") @Positive Long userId,
            @RequestParam String text,
            @RequestParam(defaultValue = "0", required = false) @PositiveOrZero Integer from,
            @RequestParam(defaultValue = "10", required = false) @Positive Integer size) {
        log.info("Получен запрос GET /items/search?text={} . От пользователя id = {} на поиск предмета: {} ." +
                        "Отображать по {} предметов на странице, начиная с itemId = {}.",
                text, userId, text, size, from);
        return itemClient.searchItem(text, userId, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                @Valid @RequestBody CommentRequestDto commentRequestDto,
                                                @PathVariable @Positive Long itemId) {
        log.info("Получен запрос Post /items/{}/comment . От пользователя id = {}, добавить к вещи с id = {}. " +
                        "Комментарий: {}",
                itemId, userId, itemId, commentRequestDto);
        return itemClient.createComment(commentRequestDto, itemId, userId);
    }

}