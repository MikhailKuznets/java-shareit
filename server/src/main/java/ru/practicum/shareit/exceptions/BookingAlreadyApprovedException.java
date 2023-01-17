package ru.practicum.shareit.exceptions;

public class BookingAlreadyApprovedException extends RuntimeException {

    public BookingAlreadyApprovedException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
