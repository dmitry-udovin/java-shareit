package ru.practicum.shareit.booking.model;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Data
public class Booking {

    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private Item bookedItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "who_booked", nullable = false)
    private User userWhoBooked;

    @Column(name = "start_rent", nullable = false)
    private LocalDateTime startRentTime;

    @Column(name = "end_rent", nullable = false)
    private LocalDateTime endRentTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private BookingApproveStatus status = BookingApproveStatus.WAITING;

}
