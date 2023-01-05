package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemRequestServiceImpl implements ItemRequestService {

    private static final Sort SORT_BY_CREATED_DESC = Sort.by(Sort.Direction.DESC, "created");

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final ItemRequestRepository itemRequestRepository;

    private final ItemRequestMapper itemRequestMapper;
    private final ItemMapper itemMapper;

    @Override
    public ItemReqResponseDto createRequest(ItemReqRequestDto dto, Long userId) {
        User user = userRepository.validateUser(userId);
        ItemRequest itemRequest = itemRequestMapper.toItemRequest(dto);

        LocalDateTime now = LocalDateTime.now();

        itemRequest.setCreated(now);
        itemRequest.setRequester(user);
        return itemRequestMapper.toItemRequestDto(itemRequestRepository.save(itemRequest));
    }

    @Override
    public ItemReqResponseDto getRequestById(Long requestId, Long userId) {
        userRepository.validateUser(userId);
        ItemRequest request = itemRequestRepository.validateItemRequest(requestId);
        ItemReqResponseDto requestDto = itemRequestMapper.toItemRequestDto(request);
        return setItems(requestDto);
    }

    @Override
    public Collection<ItemReqResponseDto> getUserAllRequests(Long userId) {
        userRepository.validateUser(userId);
        Collection<ItemRequest> requests = itemRequestRepository.findByRequesterId(userId, SORT_BY_CREATED_DESC);
        return requests.stream()
                .map(itemRequestMapper::toItemRequestDto)
                .map(this::setItems)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<ItemReqResponseDto> getAllRequestByOtherUsers(Long userId, Integer from, Integer size) {
        userRepository.validateUser(userId);

        PageRequest pageRequest = PageRequest.of((from / size), size, SORT_BY_CREATED_DESC);

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

}
