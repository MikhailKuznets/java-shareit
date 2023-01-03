package ru.practicum.shareit.item.dto;


import org.mapstruct.Mapper;
import ru.practicum.shareit.item.model.Item;

import java.util.Objects;

@Mapper(componentModel = "spring")
public interface ItemMapper {
    ItemResponseDto toItemResponseDto(Item item);

    default ItemDtoForRequestDto toItemDtoForRequestDto(Item item) {
        if (Objects.isNull(item)) {
            return null;
        }
        ItemDtoForRequestDto itemDtoForRequestDto = new ItemDtoForRequestDto();
        itemDtoForRequestDto.setId(item.getId());
        itemDtoForRequestDto.setName(item.getName());
        itemDtoForRequestDto.setDescription(item.getDescription());
        itemDtoForRequestDto.setAvailable(item.getAvailable());
        return itemDtoForRequestDto;
    }
}