package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exceptions.InvalidIdException;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    default Booking validateBooking(Long bookingId) {
        return findById(bookingId).orElseThrow(() -> {
            throw new InvalidIdException("Бронирование с id = " + bookingId + " не существует");
        });
    }

    Collection<Booking> findAllByBooker_Id(Long bookerId, Sort sort);

    Collection<Booking> findByBooker_IdAndStartIsBeforeAndEndIsAfter(Long bookerId,
                                                                     LocalDateTime start,
                                                                     LocalDateTime end,
                                                                     Sort sort);

    Collection<Booking> findByBooker_IdAndStartIsAfterAndEndIsAfter(Long bookerId,
                                                                    LocalDateTime start,
                                                                    LocalDateTime end,
                                                                    Sort sort);

    Collection<Booking> findByBooker_IdAndStartIsBeforeAndEndIsBefore(Long bookerId,
                                                                      LocalDateTime start,
                                                                      LocalDateTime end,
                                                                      Sort sort);

    Collection<Booking> findByBooker_IdAndStatusIs(Long bookerId, BookingStatus status, Sort sort);

    Collection<Booking> findByItem_Owner_Id(Long ownerId, Sort sort);

    Collection<Booking> findByItem_Owner_IdAndStartIsBeforeAndEndIsAfter(Long ownerId,
                                                                         LocalDateTime start,
                                                                         LocalDateTime end,
                                                                         Sort sort);

    Collection<Booking> findByItem_Owner_IdAndStartIsAfterAndEndIsAfter(Long ownerId,
                                                                        LocalDateTime start,
                                                                        LocalDateTime end,
                                                                        Sort sort);

    Collection<Booking> findByItem_Owner_IdAndStartIsBeforeAndEndIsBefore(Long ownerId,
                                                                          LocalDateTime start,
                                                                          LocalDateTime end,
                                                                          Sort sort);

    Collection<Booking> findByItem_Owner_IdAndStatusIs(Long ownerId,
                                                       BookingStatus status,
                                                       Sort sort);

    Optional<Booking> findFirstByItem_IdAndEndIsBeforeOrderByEndDesc(Long itemId, LocalDateTime now);

    Optional<Booking> findFirstByItem_IdAndStartIsAfterOrderByStartAsc(Long itemId, LocalDateTime now);

    Boolean existsBookingByItem_IdAndBooker_IdAndStatusAndEndIsBefore(Long itemId,
                                                                      Long userId,
                                                                      BookingStatus status,
                                                                      LocalDateTime now);
}
