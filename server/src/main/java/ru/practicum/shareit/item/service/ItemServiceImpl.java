package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.comment.dto.CommentMapper;
import ru.practicum.shareit.comment.dto.CommentRequestDto;
import ru.practicum.shareit.comment.dto.CommentResponseDto;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.comment.repository.CommentRepository;
import ru.practicum.shareit.exceptions.BookingNotFinishedException;
import ru.practicum.shareit.exceptions.InvalidIdException;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
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
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final ItemRequestRepository itemRequestRepository;
    private final ItemMapper itemMapper;
    private final BookingMapper bookingMapper;
    private final CommentMapper commentMapper;

    @Override
    public Collection<ItemResponseDto> getUserItems(Long ownerId, Integer from, Integer size) {
        PageRequest pageRequest = PageRequest.of(from, size);
        Page<Item> userItems = itemRepository.findAllByOwner_IdOrderByIdAsc(ownerId, pageRequest);
        return userItems.stream()
                .map(itemMapper::toItemResponseDto)
                .map(this::setBookings)
                .map(this::setComments)
                .collect(Collectors.toList());
    }

    @Override
    public ItemResponseDto getItemById(Long itemId, Long userId) {
        getUser(userId);
        Item item = getItem(itemId);

        ItemResponseDto itemDto = itemMapper.toItemResponseDto(item);
        if (item.getOwner().getId().equals(userId)) {
            itemDto = setBookings(itemDto);
        }
        itemDto = setComments(itemDto);
        return itemDto;
    }

    @Override
    public ItemResponseDto createItem(ItemRequestDto itemDto, Long ownerId) {
        User owner = getUser(ownerId);

        Item item = itemMapper.toItem(itemDto);
        item.setOwner(owner);

        Long requestId = itemDto.getRequestId();
        if (requestId != null) {
            ItemRequest request = itemRequestRepository.findById(requestId).orElseThrow(() -> {
                throw new InvalidIdException("Запрос с id = " + requestId + " на добавление предмета не существует");
            });
            item.setRequest(request);
        }

        ItemResponseDto response = itemMapper.toItemResponseDto(itemRepository.save(item));
        response.setComments(Collections.EMPTY_LIST);
        return response;
    }

    @Override
    public ItemResponseDto updateItem(Long userId, ItemRequestDto itemDto, Long itemId) {
        getUser(userId);

        Item selectedItem = getItem(itemId);

        Long currentItemUserId = selectedItem.getOwner().getId();

        if (!currentItemUserId.equals(userId)) {
            throw new InvalidIdException("Невозможно обновить данные предмета т.к. указан неверный собственник" +
                    " с id = " + userId);
        }

        String updatedName = itemDto.getName();
        if (updatedName != null) {
            selectedItem.setName(updatedName);
        }

        String updatedDescription = itemDto.getDescription();
        if (updatedDescription != null) {
            selectedItem.setDescription(updatedDescription);
        }

        Boolean updatedAvailable = itemDto.getAvailable();
        if (updatedAvailable != null) {
            selectedItem.setAvailable(updatedAvailable);
        }
        return itemMapper.toItemResponseDto(itemRepository.save(selectedItem));
    }

    @Override
    public Collection<ItemResponseDto> searchItem(String text, Integer from, Integer size) {
        if (text.isBlank()) {
            log.warn("Задан пустой поисковый запрос.");
            return Collections.emptyList();
        }
        String lowerText = text.toLowerCase();
        PageRequest pageRequest = PageRequest.of(from, size);
        Page<Item> items = itemRepository.searchItemByText(lowerText, pageRequest);
        return items.stream()
                .map(itemMapper::toItemResponseDto)
                .map(this::setComments)
                .collect(Collectors.toList());
    }

    @Override
    public CommentResponseDto createComment(CommentRequestDto commentRequestDto, Long itemId, Long userId) {
        Item item = getItem(itemId);
        User user = getUser(userId);

        if (!bookingRepository.existsBookingByItem_IdAndBooker_IdAndStatusAndEndIsBefore(
                itemId, userId, BookingStatus.APPROVED, LocalDateTime.now())) {
            throw new BookingNotFinishedException("Оставить отзыв можно только на завершенное бронирование.");
        }

        Comment comment = commentMapper.toCommentFromRequestDto(commentRequestDto);
        comment.setItem(item);
        comment.setAuthor(user);
        comment.setCreated(LocalDateTime.now());
        return commentMapper.toCommentResponseDto(commentRepository.save(comment));
    }

    public ItemResponseDto setBookings(ItemResponseDto itemDto) {
        Long itemId = itemDto.getId();
        LocalDateTime now = LocalDateTime.now();
        //   Last booking
        Booking lastBooking = bookingRepository.findFirstByItem_IdAndEndIsBeforeOrderByEndDesc(
                itemId, now).orElse(null);
        itemDto.setLastBooking(bookingMapper.toBookingDtoForItem(lastBooking));
        //   Next booking
        Booking nextBooking = bookingRepository.findFirstByItem_IdAndStartIsAfterOrderByStartAsc(
                itemId, now).orElse(null);
        itemDto.setNextBooking(bookingMapper.toBookingDtoForItem(nextBooking));
        return itemDto;
    }

    public ItemResponseDto setComments(ItemResponseDto itemDto) {
        Long itemId = itemDto.getId();
        Collection<Comment> comments = commentRepository.findAllByItem_Id(itemId);
        itemDto.setComments(comments.stream()
                .map(commentMapper::toCommentResponseDto)
                .collect(Collectors.toList()));
        return itemDto;
    }

    private Item getItem(Long itemId) {
        return itemRepository.findById(itemId).orElseThrow(() -> {
            throw new InvalidIdException("Предмет с id = " + itemId + " не существует");
        });
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> {
            throw new InvalidIdException("Пользователя с id = " + userId + " не существует");
        });
    }

}
