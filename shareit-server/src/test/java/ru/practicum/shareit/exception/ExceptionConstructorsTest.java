package ru.practicum.shareit.exception;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.exception.BookingDateValidationException;
import ru.practicum.shareit.comment.exception.CommentValidationException;
import ru.practicum.shareit.item.exception.ItemNotAvailableException;
import ru.practicum.shareit.user.exception.EmailAlreadyUserException;
import ru.practicum.shareit.user.exception.NotWhoBookedException;

import static org.assertj.core.api.Assertions.assertThat;

class ExceptionConstructorsTest {

    @Test
    void itemNotAvailableException_message() {
        assertThat(new ItemNotAvailableException("m").getMessage()).isEqualTo("m");
    }

    @Test
    void emailAlreadyUserException_message() {
        assertThat(new EmailAlreadyUserException("e").getMessage()).isEqualTo("e");
    }

    @Test
    void notWhoBookedException_message() {
        assertThat(new NotWhoBookedException("w").getMessage()).isEqualTo("w");
    }

    @Test
    void commentValidationException_message() {
        assertThat(new CommentValidationException("c").getMessage()).isEqualTo("c");
    }

    @Test
    void bookingDateValidationException_message() {
        assertThat(new BookingDateValidationException("d").getMessage()).isEqualTo("d");
    }
}
