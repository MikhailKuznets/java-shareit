package ru.practicum.shareit.request.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.exceptions.InvalidIdException;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.Collection;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {
    default ItemRequest validateItemRequest(Long requestId) {
        return findById(requestId).orElseThrow(() -> {
            throw new InvalidIdException("Запрос с id = " + requestId + " на добавление предмета не существует");
        });
    }

    Collection<ItemRequest> findByRequesterId(Long userId, Sort sort);

    Page<ItemRequest> findByRequesterIdIsNot(Long userId, PageRequest pageRequest);
}
