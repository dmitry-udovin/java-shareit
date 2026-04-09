//package ru.practicum.shareit.booking.mapper;
//
//import org.springframework.jdbc.core.RowMapper;
//import org.springframework.stereotype.Component;
//import ru.practicum.shareit.booking.model.Booking;
//import ru.practicum.shareit.booking.model.BookingApproveStatus;
//
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Timestamp;
//import java.time.LocalDateTime;
//
//@Component
//public class BookingRowMapper implements RowMapper<Booking> {
//
//    @Override
//    public Booking mapRow(ResultSet rs, int rowNum) throws SQLException {
//        Booking booking = new Booking();
//
//        booking.setBookedUserId(rs.getLong("user_id"));
//        booking.setBookedItemId(rs.getLong("item_id"));
//
//        LocalDateTime startRentTime = Timestamp.valueOf(rs.getString("start_time")).toLocalDateTime();
//        LocalDateTime endRentTime = Timestamp.valueOf(rs.getString("end_time")).toLocalDateTime();
//
//        BookingApproveStatus status = BookingApproveStatus.valueOf(rs.getString("booked_status"));
//
//        booking.setStartRentTime(startRentTime);
//        booking.setEndRentTime(endRentTime);
//        booking.setStatus(status);
//
//        return booking;
//    }
//}
