package ru.practicum.shareit.user.exception;

public class NotOwnerException extends Exception {
    public NotOwnerException(String message) {
        super(message);
    }
}
