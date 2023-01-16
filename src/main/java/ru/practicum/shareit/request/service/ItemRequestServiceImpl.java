package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.InvalidIdException;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemReqRequestDto;
import ru.practicum.shareit.request.dto.ItemReqResponseDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemRequestServiceImpl implements ItemRequestService {

    private static final Sort CREATED_DESC_SORT = Sort.by(Sort.Direction.DESC, "created");

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final ItemRequestRepository itemRequestRepository;

    private final ItemRequestMapper itemRequestMapper;
    private final ItemMapper itemMapper;

    @Override
    public ItemReqResponseDto createRequest(ItemReqRequestDto itemReqRequestDto, Long requesterId) {
        User requester = getUser(requesterId);
        ItemRequest itemRequest = itemRequestMapper.toItemRequest(itemReqRequestDto);

        LocalDateTime now = LocalDateTime.now();

        itemRequest.setCreated(now);
        itemRequest.setRequester(requester);
        ItemReqResponseDto response = itemRequestMapper.toItemRequestDto(itemRequestRepository.save(itemRequest));
        response.setItems(Collections.EMPTY_LIST);
        return response;
    }

    @Override
    public ItemReqResponseDto getRequestById(Long requestId, Long userId) {
        getUser(userId);
        ItemRequest request = itemRequestRepository.findById(requestId).orElseThrow(() -> {
            throw new InvalidIdException("Запрос с id = " + requestId + " на добавление предмета не существует");
        });
        ItemReqResponseDto requestDto = itemRequestMapper.toItemRequestDto(request);
        return setItems(requestDto);
    }

    @Override
    public Collection<ItemReqResponseDto> getUserAllRequests(Long requesterId) {
        getUser(requesterId);
        Collection<ItemRequest> requests = itemRequestRepository.findByRequesterId(requesterId, CREATED_DESC_SORT);
        return requests.stream()
                .map(itemRequestMapper::toItemRequestDto)
                .map(this::setItems)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<ItemReqResponseDto> getAllRequestByOtherUsers(Long userId, Integer from, Integer size) {
        getUser(userId);

        PageRequest pageRequest = PageRequest.of(from, size, CREATED_DESC_SORT);
        Page<ItemRequest> page = itemRequestRepository.findByRequesterIdIsNot(userId, pageRequest);
        Collection<ItemRequest> requests = page.getContent();
        return requests.stream()
                .map(itemRequestMapper::toItemRequestDto)
                .map(this::setItems)
                .collect(Collectors.toList());
    }

    private ItemReqResponseDto setItems(ItemReqResponseDto itemReqResponseDto) {
        Collection<ItemResponseDto> items = itemRepository.findAllByRequest_Id(itemReqResponseDto.getId())
                .stream()
                .map(itemMapper::toItemResponseDto)
                .collect(Collectors.toList());
        itemReqResponseDto.setItems(items);
        return itemReqResponseDto;
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> {
            throw new InvalidIdException("Пользователя с id = " + userId + " не существует");
        });
    }
}
