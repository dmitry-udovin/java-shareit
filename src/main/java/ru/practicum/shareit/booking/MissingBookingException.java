package ru.practicum.shareit.booking;

public class MissingBookingException extends Exception {
    public MissingBookingException(String message) {
        super(message);
    }
}
