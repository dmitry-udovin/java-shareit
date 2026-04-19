package ru.practicum.shareit.booking.dto;

import lombok.Data;
import ru.practicum.shareit.booking.model.BookingApproveStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Data
public class BookingDto {

    private final Item bookedItem;
    private final User whoBooked;
    private final LocalDateTime startRentTime;
    private final LocalDateTime endRentTime;

    private BookingApproveStatus status = BookingApproveStatus.WAITING;


}
