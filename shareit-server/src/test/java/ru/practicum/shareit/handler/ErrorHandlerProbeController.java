package ru.practicum.shareit.handler;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.booking.exception.AccessDeniedException;
import ru.practicum.shareit.booking.exception.CannotCreateBookingException;
import ru.practicum.shareit.booking.exception.MissingBookingException;
import ru.practicum.shareit.comment.exception.CommentValidationException;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.exception.OwnerNotExistsException;
import ru.practicum.shareit.request.exception.RequestNotFoundException;
import ru.practicum.shareit.user.exception.EmailAlreadyUserException;
import ru.practicum.shareit.user.exception.NotOwnerException;
import ru.practicum.shareit.user.exception.NotWhoBookedException;
import ru.practicum.shareit.user.exception.UserNotFoundException;

@RestController
@RequestMapping("/__probe-errors")
public class ErrorHandlerProbeController {

    @GetMapping("/user-not-found")
    public void userNotFound() {
        throw new UserNotFoundException("u");
    }

    @GetMapping("/item-not-found")
    public void itemNotFound() {
        throw new ItemNotFoundException("i");
    }

    @GetMapping("/request-not-found")
    public void requestNotFound() {
        throw new RequestNotFoundException("r");
    }

    @GetMapping("/not-owner")
    public void notOwner() {
        throw new NotOwnerException("no");
    }

    @GetMapping("/not-who-booked")
    public void notWhoBooked() {
        throw new NotWhoBookedException("nwb");
    }

    @GetMapping("/missing-booking")
    public void missingBooking() {
        throw new MissingBookingException("mb");
    }

    @GetMapping("/owner-not-exists")
    public void ownerNotExists() {
        throw new OwnerNotExistsException("one");
    }

    @GetMapping("/access-denied")
    public void accessDenied() {
        throw new AccessDeniedException("ad");
    }

    @GetMapping("/comment-validation")
    public void commentValidation() {
        throw new CommentValidationException("cv");
    }

    @GetMapping("/illegal-arg")
    public void illegalArg() {
        throw new IllegalArgumentException("ia");
    }

    @GetMapping("/email-conflict")
    public void emailConflict() {
        throw new EmailAlreadyUserException("ec");
    }

    @GetMapping("/cannot-book")
    public void cannotBook() {
        throw new CannotCreateBookingException("cb");
    }

    @GetMapping("/throwable")
    public void throwable() throws Exception {
        throw new Exception("ex");
    }
}
