package ru.practicum.shareit.booking.exception;

public class CannotCreateBookingException extends RuntimeException {
    public CannotCreateBookingException(String message) {
        super(message);
    }
}
