package ru.practicum.shareit.item.exception;

public class OwnerNotExistsException extends RuntimeException {
    public OwnerNotExistsException(String message) {
        super(message);
    }
}
