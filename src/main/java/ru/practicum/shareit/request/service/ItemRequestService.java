package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemReqRequestDto;
import ru.practicum.shareit.request.dto.ItemReqResponseDto;

import java.util.Collection;

public interface ItemRequestService {
    ItemReqResponseDto createRequest(ItemReqRequestDto dto, Long userId);

    Collection<ItemReqResponseDto> getUserAllRequests(Long userId);

    ItemReqResponseDto getRequestById(Long requestId, Long userId);

    Collection<ItemReqResponseDto> getAllRequestByOtherUsers(Long userId, Integer fromId, Integer size);
}
