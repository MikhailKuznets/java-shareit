package ru.practicum.shareit.item.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.dto.CommentRequestDto;
import ru.practicum.shareit.comment.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;

@RestController
@RequestMapping(path = "/items")
@Slf4j
@RequiredArgsConstructor
@Validated
public class ItemController {
    private final ItemService itemService;

    @GetMapping
    public ResponseEntity<Collection<ItemResponseDto>> getUserItems(
            @RequestHeader("X-Sharer-User-Id") @Positive Long ownerId,
            @RequestParam(defaultValue = "0", required = false) @PositiveOrZero Integer from,
            @RequestParam(defaultValue = "10", required = false) @Positive Integer size) {
        log.info("Получен запрос GET /items . От пользователя id = {} на просмотр собственных предметов. " +
                        "Отображать по {} предметов на странице, начиная с itemId = {}.",
                ownerId, size, from);
        return new ResponseEntity<>(itemService.getUserItems(ownerId, from, size), HttpStatus.OK);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<ItemResponseDto> getItemById(@RequestHeader("X-Sharer-User-Id") @Positive Long userId,
                                                       @PathVariable @Positive Long itemId) {
        log.info("Получен запрос GET /items/{} . От пользователя id = {}.",
                itemId, userId);
        return new ResponseEntity<>(itemService.getItemById(itemId, userId), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<Collection<ItemResponseDto>> searchItem(
            @RequestHeader("X-Sharer-User-Id") @Positive Long userId,
            @RequestParam String text,
            @RequestParam(defaultValue = "0", required = false) @PositiveOrZero Integer from,
            @RequestParam(defaultValue = "10", required = false) @Positive Integer size) {
        log.info("Получен запрос GET /items/search?text={} . От пользователя id = {} на поиск предмета: {} ." +
                        "Отображать по {} предметов на странице, начиная с itemId = {}.",
                text, userId, text, size, from);
        return new ResponseEntity<>(itemService.searchItem(text, from, size), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ItemResponseDto> createItem(@RequestHeader("X-Sharer-User-Id") @Positive Long ownerId,
                                                      @RequestBody ItemRequestDto itemDto) {
        log.info("Получен запрос Post /items . От пользователя id = {}, добавить вещь: {}", ownerId, itemDto);
        return new ResponseEntity<>(itemService.createItem(itemDto, ownerId), HttpStatus.OK);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<ItemResponseDto> updateItem(@RequestHeader("X-Sharer-User-Id") @Positive Long userId,
                                                      @PathVariable @Positive Long itemId,
                                                      @RequestBody ItemRequestDto itemDto) {
        log.info("Получен запрос Patch /items/{} . От пользователя id = {}, обновить данные вещи {}.",
                itemId, userId, itemDto);
        return new ResponseEntity<>(itemService.updateItem(userId, itemDto, itemId), HttpStatus.OK);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<CommentResponseDto> createComment(@Valid @RequestBody CommentRequestDto commentRequestDto,
                                                            @PathVariable Long itemId,
                                                            @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Получен запрос Post /items/{}/comment . От пользователя id = {}, добавить к вещи с id = {}. " +
                        "Комментарий: {}",
                itemId, userId, itemId, commentRequestDto);
        return new ResponseEntity<>(itemService.createComment(commentRequestDto, itemId, userId), HttpStatus.OK);
    }
}
