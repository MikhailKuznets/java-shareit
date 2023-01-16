package ru.practicum.shareit.item.dto;


import org.mapstruct.Mapper;
import ru.practicum.shareit.item.model.Item;

@Mapper(componentModel = "spring")
public interface ItemMapper {
    Item toItem(ItemRequestDto itemRequestDto);


    // По аналогии со сгенерированным классом ItemMapperImpl
    default ItemResponseDto toItemResponseDto(Item item) {
        if (item == null) {
            return null;
        }
        ItemResponseDto itemResponseDto = ItemResponseDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .build();

        if (item.getRequest() != null) {
            itemResponseDto.setRequestId(item.getRequest().getId());
        }

        return itemResponseDto;
    }

}