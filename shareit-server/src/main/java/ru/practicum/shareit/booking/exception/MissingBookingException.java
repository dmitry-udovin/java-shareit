package ru.practicum.shareit.booking.exception;

public class MissingBookingException extends RuntimeException {
    public MissingBookingException(String message) {
        super(message);
    }
}
