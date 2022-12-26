package ru.practicum.shareit.booking.controller;

import java.util.Arrays;
import java.util.Objects;

public enum BookingState {
    ALL,
    CURRENT,
    PAST,
    FUTURE,
    WAITING,
    REJECTED,
    UNKNOWN;

    public static BookingState of(String value) {
        return Arrays.stream(values())
                .filter(item -> Objects.equals(value, item.name()))
                .findFirst()
                .orElse(UNKNOWN);
    }


}
