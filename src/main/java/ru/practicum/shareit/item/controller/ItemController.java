package ru.practicum.shareit.item.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.dto.CommentRequestDto;
import ru.practicum.shareit.comment.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.Collection;

@RestController
@RequestMapping(path = "/items")
@Slf4j
@RequiredArgsConstructor
@Validated
public class ItemController {
    private final ItemService itemService;

    @GetMapping
    public ResponseEntity<Collection<ItemResponseDto>> getUserItems(@RequestHeader("X-Sharer-User-Id") @Positive Long userId) {
        return new ResponseEntity<>(itemService.getUserItems(userId), HttpStatus.OK);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<ItemResponseDto> getItemById(@RequestHeader("X-Sharer-User-Id") @Positive Long userId,
                                                       @PathVariable @Positive Long itemId) {
        log.info("Получен запрос GET /items/{} . От пользователя id = {}.",
                itemId, userId);
        return new ResponseEntity<>(itemService.getItemById(itemId, userId), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<Collection<ItemResponseDto>> searchItem(@RequestHeader("X-Sharer-User-Id") @Positive Long userId,
                                                                  @RequestParam String text) {
        log.info("Получен запрос GET /items/search?text={} . От пользователя id = {}.",
                text, userId);
        return new ResponseEntity<>(itemService.searchItem(text), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ItemResponseDto> createItem(@RequestHeader("X-Sharer-User-Id") @Positive Long userId,
                                                      @RequestBody @Valid Item item) {
        log.info("Получен запрос Post /items . От пользователя id = {}, добавить вещь: {}", userId, item);
        return new ResponseEntity<>(itemService.createItem(item, userId), HttpStatus.OK);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<ItemResponseDto> updateItem(@RequestHeader("X-Sharer-User-Id") @Positive Long userId,
                                                      @PathVariable @Positive Long itemId,
                                                      @RequestBody Item item) {
        log.info("Получен запрос Patch /items/{} . От пользователя id = {}, обновить данные вещи {}.",
                itemId, userId, item);
        return new ResponseEntity<>(itemService.updateItem(userId, item, itemId), HttpStatus.OK);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<CommentResponseDto> createComment(@Valid @RequestBody CommentRequestDto commentRequestDto,
                                                            @PathVariable Long itemId,
                                                            @RequestHeader("X-Sharer-User-Id") Long userId) {
        return new ResponseEntity<>(itemService.createComment(commentRequestDto, itemId, userId), HttpStatus.OK);
    }
}
