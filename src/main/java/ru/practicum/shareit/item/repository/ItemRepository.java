package ru.practicum.shareit.item.repository;

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

    Collection<Item> findAllByOwner_IdOrderByIdAsc(Long userId);

    @Query(" select i from Item i " +
            "where (lower(i.name) like lower(concat('%', ?1, '%')) " +
            "   or lower(i.description) like lower(concat('%', ?1, '%')))" +
            "and i.available = true")
    Collection<Item> searchItemByText(String text);


    Collection<Item> findAllByRequest_Id(Long requestId);
}
