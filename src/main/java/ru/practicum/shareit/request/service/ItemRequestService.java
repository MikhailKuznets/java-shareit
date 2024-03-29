package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemReqRequestDto;
import ru.practicum.shareit.request.dto.ItemReqResponseDto;

import java.util.Collection;

public interface ItemRequestService {
    ItemReqResponseDto createRequest(ItemReqRequestDto itemReqRequestDto, Long requesterId);

    Collection<ItemReqResponseDto> getUserAllRequests(Long requesterId);

    ItemReqResponseDto getRequestById(Long requestId, Long userId);

    Collection<ItemReqResponseDto> getAllRequestByOtherUsers(Long userId, Integer from, Integer size);
}
