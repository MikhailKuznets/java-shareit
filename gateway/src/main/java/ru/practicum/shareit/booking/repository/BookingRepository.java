package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    Page<Booking> findAllByBooker_Id(Long bookerId, Pageable pageable);

    Page<Booking> findByBooker_IdAndStartIsBeforeAndEndIsAfter(Long bookerId,
                                                               LocalDateTime start,
                                                               LocalDateTime end,
                                                               Pageable pageable);

    Page<Booking> findByBooker_IdAndStartIsAfterAndEndIsAfter(Long bookerId,
                                                              LocalDateTime start,
                                                              LocalDateTime end,
                                                              Pageable pageable);

    Page<Booking> findByBooker_IdAndStartIsBeforeAndEndIsBefore(Long bookerId,
                                                                LocalDateTime start,
                                                                LocalDateTime end,
                                                                Pageable pageable);

    Page<Booking> findByBooker_IdAndStatusIs(Long bookerId, BookingStatus status, Pageable pageable);

    Page<Booking> findByItem_Owner_Id(Long ownerId, Pageable pageable);

    Page<Booking> findByItem_Owner_IdAndStartIsBeforeAndEndIsAfter(Long ownerId,
                                                                   LocalDateTime start,
                                                                   LocalDateTime end,
                                                                   Pageable pageable);

    Page<Booking> findByItem_Owner_IdAndStartIsAfterAndEndIsAfter(Long ownerId,
                                                                  LocalDateTime start,
                                                                  LocalDateTime end,
                                                                  Pageable pageable);

    Page<Booking> findByItem_Owner_IdAndStartIsBeforeAndEndIsBefore(Long ownerId,
                                                                    LocalDateTime start,
                                                                    LocalDateTime end,
                                                                    Pageable pageable);

    Page<Booking> findByItem_Owner_IdAndStatusIs(Long ownerId,
                                                 BookingStatus status,
                                                 Pageable pageable);

    Optional<Booking> findFirstByItem_IdAndEndIsBeforeOrderByEndDesc(Long itemId, LocalDateTime now);

    Optional<Booking> findFirstByItem_IdAndStartIsAfterOrderByStartAsc(Long itemId, LocalDateTime now);

    Boolean existsBookingByItem_IdAndBooker_IdAndStatusAndEndIsBefore(Long itemId,
                                                                      Long userId,
                                                                      BookingStatus status,
                                                                      LocalDateTime now);
}
