package ru.practicum.shareit.item.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.exceptions.InvalidIdException;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemRepository extends JpaRepository<Item, Long> {

    default Item validateItem(Long itemId) {
        return findById(itemId).orElseThrow(() -> {
            throw new InvalidIdException("Предмет с id = " + itemId + " не существует");
        });
    }

    Page<Item> findAllByOwner_IdOrderByIdAsc(Long userId, PageRequest pageRequest);

    @Query(" select i from Item i " +
            "where (lower(i.name) like lower(concat('%', ?1, '%')) " +
            "   or lower(i.description) like lower(concat('%', ?1, '%')))" +
            "and i.available = true")
    Page<Item> searchItemByText(String text, PageRequest pageRequest);


    Collection<Item> findAllByRequest_Id(Long requestId);
}
