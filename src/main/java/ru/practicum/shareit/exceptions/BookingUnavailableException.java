package ru.practicum.shareit.exceptions;

public class BookingUnavailableException extends RuntimeException {

    public BookingUnavailableException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}