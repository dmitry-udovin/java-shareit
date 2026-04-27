package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateBookingDto {

    @NotNull
    @Positive
    private final Long bookedItemId;
    @NotNull
    @Positive
    private final Long idWhoBooked;
    @NotNull
    @FutureOrPresent
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private final LocalDateTime startRentTime;
    @NotNull
    @Future
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private final LocalDateTime endRentTime;

    @AssertTrue(message = "Дата окончания бронирования должна быть позже даты начала")
    public boolean isPeriodValid() {
        return startRentTime != null && endRentTime != null && endRentTime.isAfter(startRentTime);
    }

}
