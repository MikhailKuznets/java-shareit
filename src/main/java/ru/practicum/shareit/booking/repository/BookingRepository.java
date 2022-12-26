package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exceptions.InvalidIdException;

public interface BookingRepository  extends JpaRepository<Booking, Long> {

    default Booking validateBooking(Long bookingId) {
        return findById(bookingId).orElseThrow(() -> {
            throw new InvalidIdException("Бронирование с id = " + bookingId + " не существует");
        });
    }

//    Collection<Booking> findByBooker_IdAndEndIsBefore(Long bookerId, LocalDateTime end, Sort sort);
}
