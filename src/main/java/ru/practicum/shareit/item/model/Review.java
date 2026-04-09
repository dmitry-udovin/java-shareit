package ru.practicum.shareit.item.model;

import lombok.Data;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Data
public class Review {

    private Long id;

    private String text;
    private Item item;
    private User author;
    private LocalDateTime created = LocalDateTime.now();
    private Booking booking;

}
