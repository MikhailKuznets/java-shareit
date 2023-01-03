package ru.practicum.shareit.request.dto;

import org.mapstruct.Mapper;
import ru.practicum.shareit.request.model.ItemRequest;

@Mapper(componentModel = "spring")
public interface RequestMapper {
    ItemRequest toItemRequest(ItemReqRequestDto itemReqRequestDto);

    ItemReqResponseDto toItemRequestDto(ItemRequest itemRequest);
}
