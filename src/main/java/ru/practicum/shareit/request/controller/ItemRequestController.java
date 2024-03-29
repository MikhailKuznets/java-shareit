package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemReqRequestDto;
import ru.practicum.shareit.request.dto.ItemReqResponseDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;

@RestController
@RequestMapping(path = "/requests")
@Slf4j
@RequiredArgsConstructor
@Validated
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @PostMapping
    public ResponseEntity<ItemReqResponseDto> createRequest(@RequestHeader("X-Sharer-User-Id")
                                                            @Positive Long requesterId,
                                                            @RequestBody @Valid ItemReqRequestDto itemReqRequestDto) {
        log.info("Получен запрос POST /requests . " +
                "От пользователя с userId = {}. Создать запрос на добавление предмета: {}.", requesterId, itemReqRequestDto);
        return new ResponseEntity<>(itemRequestService.createRequest(itemReqRequestDto, requesterId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Collection<ItemReqResponseDto>> getUserAllRequests(@RequestHeader("X-Sharer-User-Id")
                                                                             @Positive Long requesterId) {
        log.info("Получен запрос Get /requests . " +
                "От пользователя с userId = {}. " +
                "Найти его запросы на добавление предмета.", requesterId);
        return new ResponseEntity<>(itemRequestService.getUserAllRequests(requesterId), HttpStatus.OK);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<ItemReqResponseDto> getRequestById(@RequestHeader("X-Sharer-User-Id")
                                                             @Positive Long userId,
                                                             @PathVariable @Positive Long requestId) {
        log.info("Получен запрос Get /requests/{} . " +
                        "От пользователя с userId = {}. Найти запрос на добавление предмета с requestId = {}.",
                requestId, userId, requestId);
        return new ResponseEntity<>(itemRequestService.getRequestById(requestId, userId), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<Collection<ItemReqResponseDto>> getAllRequestsByOtherUsers(
            @RequestHeader("X-Sharer-User-Id") @Positive Long userId,
            @RequestParam(defaultValue = "0", required = false) @PositiveOrZero Integer from,
            @RequestParam(defaultValue = "10", required = false) @Positive Integer size) {
        log.info("Получен запрос Get /requests/all?from={}&size={} . От пользователя с userId = {}. " +
                        "Найти запросы других пользователей на добавление предметов. " +
                        "Отображать по {} запросов на странице, начиная с requestId = {}.",
                from, size, userId, size, from);
        return new ResponseEntity<>(itemRequestService.getAllRequestByOtherUsers(userId, from, size), HttpStatus.OK);
    }

}
