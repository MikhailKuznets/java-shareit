package ru.practicum.shareit.exceptions;

public class BookingNotFinishedException extends RuntimeException {

    public BookingNotFinishedException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
