package ru.practicum.shareit.item.dto;


import org.mapstruct.Mapper;
import ru.practicum.shareit.item.model.Item;

import java.util.Objects;

@Mapper(componentModel = "spring")
public interface ItemMapper {
    Item toItem(ItemRequestDto itemRequestDto);

    default ItemResponseDto toItemResponseDto(Item item) {
        if (Objects.isNull(item)) {
            return null;
        }
        ItemResponseDto itemResponseDto = new ItemResponseDto();

        itemResponseDto.setId(item.getId());
        itemResponseDto.setName(item.getName());
        itemResponseDto.setDescription(item.getDescription());
        itemResponseDto.setAvailable(item.getAvailable());

        if (Objects.nonNull(item.getRequest())) {
            itemResponseDto.setRequestId(item.getRequest().getId());
        }
        return itemResponseDto;
    }

}