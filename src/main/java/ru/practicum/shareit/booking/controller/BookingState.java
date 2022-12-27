package ru.practicum.shareit.booking.controller;

public enum BookingState {
    ALL,
    CURRENT,
    PAST,
    FUTURE,
    WAITING,
    REJECTED;

//    public static BookingState of(String value) {
//        return Arrays.stream(values())
//                .filter(item -> Objects.equals(value, item.name()))
//                .findFirst()
//                .orElse(UNKNOWN);
//    }


}
