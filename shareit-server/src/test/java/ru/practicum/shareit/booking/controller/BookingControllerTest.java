package ru.practicum.shareit.booking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.dto.CreateBookingDto;
import ru.practicum.shareit.booking.model.BookingApproveStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.handler.ErrorHandler;
import ru.practicum.shareit.item.dto.ItemShortDto;
import ru.practicum.shareit.user.dto.UserShortDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
@Import(ErrorHandler.class)
class BookingControllerTest {

    private static final String USER_HEADER = "X-Sharer-User-Id";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookingService bookingService;

    @Test
    void createBooking_returnsDto() throws Exception {
        LocalDateTime start = LocalDateTime.of(2026, 6, 1, 10, 0, 0);
        LocalDateTime end = LocalDateTime.of(2026, 6, 2, 10, 0, 0);
        BookingResponseDto dto = new BookingResponseDto(
                1L, start, end, BookingApproveStatus.WAITING,
                new UserShortDto(2L), new ItemShortDto(3L, "Item", 4L));
        when(bookingService.createBooking(any(CreateBookingDto.class), eq(2L))).thenReturn(dto);

        mockMvc.perform(post("/bookings")
                        .header(USER_HEADER, 2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CreateBookingDto(3L, start, end))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("WAITING"));
    }

    @Test
    void updateBookingStatus_returnsDto() throws Exception {
        LocalDateTime start = LocalDateTime.of(2026, 7, 1, 10, 0, 0);
        LocalDateTime end = LocalDateTime.of(2026, 7, 2, 10, 0, 0);
        BookingResponseDto dto = new BookingResponseDto(
                5L, start, end, BookingApproveStatus.APPROVED,
                new UserShortDto(2L), new ItemShortDto(3L, "Item", 1L));
        when(bookingService.updateBookingStatus(5L, true, 1L)).thenReturn(dto);

        mockMvc.perform(patch("/bookings/5")
                        .header(USER_HEADER, 1)
                        .param("approved", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("APPROVED"));
    }

    @Test
    void getBooking_returnsDto() throws Exception {
        LocalDateTime start = LocalDateTime.of(2026, 8, 1, 10, 0, 0);
        LocalDateTime end = LocalDateTime.of(2026, 8, 2, 10, 0, 0);
        BookingResponseDto dto = new BookingResponseDto(
                8L, start, end, BookingApproveStatus.WAITING,
                new UserShortDto(2L), new ItemShortDto(3L, "Item", 4L));
        when(bookingService.findBookingById(8L, 2L)).thenReturn(dto);

        mockMvc.perform(get("/bookings/8").header(USER_HEADER, 2))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(8));
    }

    @Test
    void getBookingsForUser_returnsList() throws Exception {
        when(bookingService.findBookingsForUser(3L, "ALL")).thenReturn(List.of());

        mockMvc.perform(get("/bookings").header(USER_HEADER, 3))
                .andExpect(status().isOk());
    }

    @Test
    void getBookingsForItemOwner_returnsList() throws Exception {
        when(bookingService.findBookingsForOwnerItems(4L, "FUTURE")).thenReturn(List.of());

        mockMvc.perform(get("/bookings/owner").header(USER_HEADER, 4).param("state", "FUTURE"))
                .andExpect(status().isOk());
    }
}
