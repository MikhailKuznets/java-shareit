package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemReqRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> createRequest(@RequestHeader("X-Sharer-User-Id")
                                                @Positive Long requesterId,
                                                @RequestBody @Valid ItemReqRequestDto itemReqRequestDto) {
        log.info("Получен запрос POST /requests . " +
                "От пользователя с userId = {}. Создать запрос на добавление предмета: {}.", requesterId, itemReqRequestDto);
        return itemRequestClient.createRequest(requesterId, itemReqRequestDto);
    }

    @GetMapping
    public ResponseEntity<Object> getUserAllRequests(@RequestHeader("X-Sharer-User-Id")
                                                     @Positive Long requesterId) {
        log.info("Получен запрос Get /requests . " +
                "От пользователя с userId = {}. " +
                "Найти его запросы на добавление предмета.", requesterId);
        return itemRequestClient.getUserAllRequests(requesterId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequestById(@RequestHeader("X-Sharer-User-Id")
                                                 @Positive Long userId,
                                                 @PathVariable @Positive Long requestId) {
        log.info("Получен запрос Get /requests/{} . " +
                        "От пользователя с userId = {}. Найти запрос на добавление предмета с requestId = {}.",
                requestId, userId, requestId);
        return itemRequestClient.getRequestById(requestId, userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllRequestsByOtherUsers(
            @RequestHeader("X-Sharer-User-Id") @Positive Long userId,
            @RequestParam(defaultValue = "0", required = false) @PositiveOrZero Integer from,
            @RequestParam(defaultValue = "10", required = false) @Positive Integer size) {
        log.info("Получен запрос Get /requests/all?from={}&size={} . От пользователя с userId = {}. " +
                        "Найти запросы других пользователей на добавление предметов. " +
                        "Отображать по {} запросов на странице, начиная с requestId = {}.",
                from, size, userId, size, from);
        return itemRequestClient.getAllRequestsByOtherUsers(userId, from, size);
    }


}
