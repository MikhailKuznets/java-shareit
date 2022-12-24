package ru.practicum.shareit.booking;

import java.util.Arrays;
import java.util.Objects;

public enum BookingStatus {
    ALL,
    CURRENT,
    PAST,
    FUTURE,
    WAITING,
    REJECTED,
    UNKNOWN;

    public static BookingStatus of(String value) {
        return Arrays.stream(values())
                .filter(item -> Objects.equals(value, item.name()))
                .findFirst()
                .orElse(UNKNOWN);
    }


}
