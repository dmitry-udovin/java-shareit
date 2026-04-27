package ru.practicum.shareit.booking.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query(
            "SELECT b FROM Booking b " +
                    "JOIN FETCH b.userWhoBooked " +
                    "JOIN FETCH b.bookedItem i " +
                    "WHERE b.userWhoBooked.id = :userId OR i.ownerId = :userId " +
                    "ORDER BY b.startRentTime DESC"
    )
    List<Booking> findByUserIdOrderByStartDesc(@Param("userId") Long userId);

    @Query(
            "SELECT b FROM Booking b " +
                    "JOIN FETCH b.userWhoBooked " +
                    "JOIN FETCH b.bookedItem i " +
                    "WHERE i.ownerId = :userId " +
                    "ORDER BY b.startRentTime DESC"
    )
    List<Booking> findByOwnerIdOrderByStartDesc(@Param("ownerId") Long ownerId);

}