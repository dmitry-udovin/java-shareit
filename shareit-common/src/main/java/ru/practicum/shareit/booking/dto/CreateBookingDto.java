package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public record CreateBookingDto(@Positive long itemId,
                               @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") @NotNull LocalDateTime start,
                               @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") @NotNull LocalDateTime end) {

    @AssertTrue(message = "Дата окончания бронирования должна быть позже даты начала")
    public boolean isPeriodValid() {
        return start != null && end != null && end.isAfter(start);
    }

}
