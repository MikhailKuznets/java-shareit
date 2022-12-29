package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.InvalidIdException;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
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
    private static final Sort SORT_BY_START_ASC = Sort.by(Sort.Direction.ASC, "start");
    private static final Sort SORT_BY_END_DESC = Sort.by(Sort.Direction.DESC, "end");
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final ItemMapper itemMapper;
    private final BookingMapper bookingMapper;

    @Override
    public Collection<ItemResponseDto> getUserItems(Long userId) {
        Collection<Item> userItems = itemRepository.findAllByOwner_IdOrderByIdAsc(userId);

        return userItems.stream().map(itemMapper::toItemResponseDto).collect(Collectors.toList());
    }

    @Override
    public ItemResponseDto getItemById(Long itemId, Long userId) {
        Item item = itemRepository.validateItem(itemId);
        User user = userRepository.validateUser(userId);

        ItemResponseDto itemDto = itemMapper.toItemResponseDto(item);

        if (item.getOwner().getId().equals(userId)) {
            itemDto = setBookings(itemDto);
        }

        return itemDto;
    }

    @Override
    public ItemResponseDto createItem(Item item, Long userId) {
        User owner = userRepository.validateUser(userId);
        item.setOwner(owner);
        return itemMapper.toItemResponseDto(itemRepository.save(item));
    }

    @Override
    public ItemResponseDto updateItem(Long userId, Item item, Long itemId) {
        User owner = userRepository.validateUser(userId);

        Item selectedItem = itemRepository.validateItem(itemId);

        Long currentItemUserId = selectedItem.getOwner().getId();

        if (!currentItemUserId.equals(userId)) {
            throw new InvalidIdException("Невозможно обновить данные предмета т.к. указан неверный собственник" +
                    " с id = " + userId);
        }

        String updatedName = item.getName();
        if (updatedName != null) {
            selectedItem.setName(updatedName);
        }

        String updatedDescription = item.getDescription();
        if (updatedDescription != null) {
            selectedItem.setDescription(updatedDescription);
        }

        Boolean updatedAvailable = item.getAvailable();
        if (updatedAvailable != null) {
            selectedItem.setAvailable(updatedAvailable);
        }
        return itemMapper.toItemResponseDto(itemRepository.save(selectedItem));
    }

    @Override
    public Collection<ItemResponseDto> searchItem(String text) {
        if (text.isBlank()) {
            log.warn("Задан пустой поисковый запрос.");
            return Collections.emptyList();
        }
        String lowerText = text.toLowerCase();
        Collection<Item> items = itemRepository.searchItemByText(lowerText);
        return items.stream().map(itemMapper::toItemResponseDto).collect(Collectors.toList());
    }

    public ItemResponseDto setBookings(ItemResponseDto itemDto) {
        Long itemId = itemDto.getId();

        //   Last booking
        Booking lastBooking = bookingRepository.findFirstByItem_IdAndEndIsBeforeOrderByEndDesc(
                itemId, LocalDateTime.now());
        itemDto.setLastBooking(bookingMapper.toBookingDtoForItem(lastBooking));

        //   Next booking
        Booking nextBooking = bookingRepository.findFirstByItem_IdAndStartIsAfterOrderByStartAsc(
                itemId, LocalDateTime.now());
        itemDto.setNextBooking(bookingMapper.toBookingDtoForItem(nextBooking));

        log.error(lastBooking.toString());
        log.error(nextBooking.toString());

        return itemDto;
    }

}
