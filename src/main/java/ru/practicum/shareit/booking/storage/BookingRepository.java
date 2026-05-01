package ru.practicum.shareit.booking.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingApproveStatus;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

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
                    "WHERE i.ownerId = :ownerId " +
                    "ORDER BY b.startRentTime DESC"
    )
    List<Booking> findByOwnerIdOrderByStartDesc(@Param("ownerId") Long ownerId);

    @Query("SELECT b FROM Booking b WHERE b.bookedItem.id IN :itemIds AND b.status = :status")
    List<Booking> findApprovedByItemIds(@Param("itemIds") Collection<Long> itemIds,
                                        @Param("status") BookingApproveStatus status);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.bookedItem.id = :itemId " +
            "AND b.userWhoBooked.id = :userId " +
            "AND b.status = 'APPROVED' " +
            "AND b.endRentTime < :now")
    Optional<Booking> findApprovedPastBooking(@Param("itemId") Long itemId,
                                              @Param("userId") Long userId,
                                              @Param("now") LocalDateTime now);

}