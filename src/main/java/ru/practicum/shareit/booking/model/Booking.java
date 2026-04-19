package ru.practicum.shareit.booking.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Booking {

    private long id;

    private long bookedItemId;
    private long bookedUserId;
    private LocalDateTime startRentTime;
    private LocalDateTime endRentTime;

    private BookingApproveStatus status = BookingApproveStatus.WAITING;

}
