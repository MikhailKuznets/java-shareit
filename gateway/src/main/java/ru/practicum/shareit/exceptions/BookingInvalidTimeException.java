package ru.practicum.shareit.exceptions;

public class BookingInvalidTimeException extends RuntimeException {

    public BookingInvalidTimeException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}