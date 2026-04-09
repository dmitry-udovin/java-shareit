package ru.practicum.shareit.user.model;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class User {

    private Long id;

    private String name;
    private String email;
    private String password;
    private Set<Long> personalItemIds = new HashSet<>();

//    public void approveBookingForPersonalItem(Booking booking) throws ItemNotAvailableException, NotOwnerException, MissingBookingException {
//        long bookingItemId = booking.getBookedItemId();
//        if (personalItems.contains(bookingItem)) {
//            if (booking.getEndRentTime().isAfter(LocalDateTime.now())) {
//                bookingItem.setStatus(RentStatus.UNAVAILABLE);
//                throw new ItemNotAvailableException("Вещь уже находится в аренде. Дождитесь времени окончания");
//            }
//
//            if (bookingItem.getStatus().equals(RentStatus.UNAVAILABLE)) {
//                booking.setStatus(BookingApproveStatus.WAITING);
//                throw new ItemNotAvailableException("Для сдачи вещи в аренду необходимо " +
//                        "подтвердить, что она доступна.");
//            }
//
//            if (booking.getStartRentTime().isBefore(LocalDateTime.now())) {
//                booking.setStatus(BookingApproveStatus.CANCELLED);
//                throw new MissingBookingException("Отказано автоматически: вещь хотели " +
//                        "взять в аренду раньше: " + booking.getStartRentTime().toString());
//            }
//
//            booking.setStatus(BookingApproveStatus.APPROVED);
//
//        } else {
//            throw new NotOwnerException("Вы не являетесь владельцем вещи.");
//        }
//    }

}
