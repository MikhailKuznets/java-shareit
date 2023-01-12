package ru.practicum.shareit.item.service;

import ru.practicum.shareit.comment.dto.CommentRequestDto;
import ru.practicum.shareit.comment.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;

import java.util.Collection;

public interface ItemService {
    Collection<ItemResponseDto> getUserItems(Long ownerId, Integer from, Integer size);

    ItemResponseDto getItemById(Long itemId, Long userId);

    ItemResponseDto createItem(ItemRequestDto itemRequestDto, Long ownerId);

    ItemResponseDto updateItem(Long userId, ItemRequestDto itemRequestDto, Long itemId);

    Collection<ItemResponseDto> searchItem(String text, Integer from, Integer size);

    CommentResponseDto createComment(CommentRequestDto commentRequestDto, Long itemId, Long userId);
}
