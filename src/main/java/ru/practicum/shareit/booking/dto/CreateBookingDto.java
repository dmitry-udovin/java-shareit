package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public record CreateBookingDto(@NotNull @Positive Long itemId,
                               @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") @NotNull @FutureOrPresent LocalDateTime start,
                               @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") @NotNull @Future LocalDateTime end) {

    @AssertTrue(message = "Дата окончания бронирования должна быть позже даты начала")
    public boolean isPeriodValid() {
        return start != null && end != null && end.isAfter(start);
    }

}
