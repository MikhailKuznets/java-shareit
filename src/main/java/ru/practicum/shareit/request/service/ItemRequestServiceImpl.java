package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.request.dto.ItemReqRequestDto;
import ru.practicum.shareit.request.dto.ItemReqResponseDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemRequestServiceImpl implements ItemRequestService {
    private final UserRepository userRepository;

    @Override
    public ItemReqResponseDto createRequest(ItemReqRequestDto dto, Long userId) {
        User user = userRepository.validateUser(userId);
        return null;
    }

    @Override
    public Collection<ItemReqResponseDto> getUserAllRequests(Long userId) {
        User user = userRepository.validateUser(userId);
        return null;
    }

    @Override
    public ItemReqResponseDto getRequestById(Long requestId, Long userId) {
        User user = userRepository.validateUser(userId);
        return null;
    }

    @Override
    public Collection<ItemReqResponseDto> getAllRequestByOtherUsers(Long userId, Long fromId, Long size) {
        User user = userRepository.validateUser(userId);
        return null;
    }
}
