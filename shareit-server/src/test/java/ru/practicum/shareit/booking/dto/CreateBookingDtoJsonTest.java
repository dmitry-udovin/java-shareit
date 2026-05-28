package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@JsonTest
class CreateBookingDtoJsonTest {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    @Autowired
    private JacksonTester<CreateBookingDto> json;

    @Test
    void deserializesWithCustomDateFormat() throws Exception {
        LocalDateTime start = LocalDateTime.now().plusDays(1).withNano(0);
        LocalDateTime end = start.plusHours(2);
        String body = String.format(
                "{\"itemId\":7,\"start\":\"%s\",\"end\":\"%s\"}",
                start.format(FMT),
                end.format(FMT));
        CreateBookingDto dto = json.parse(body).getObject();
        assertThat(dto.itemId()).isEqualTo(7);
        assertThat(dto.start()).isEqualTo(start);
        assertThat(dto.end()).isEqualTo(end);
        assertThat(dto.isPeriodValid()).isTrue();
    }

    @Test
    void periodInvalidWhenEndNotAfterStart() {
        LocalDateTime start = LocalDateTime.now().plusDays(5).withNano(0);
        LocalDateTime end = start.minusHours(1);
        CreateBookingDto dto = new CreateBookingDto(1, start, end);
        assertThat(dto.isPeriodValid()).isFalse();
    }

    @Test
    void malformedDateInJson_failsParse() {
        String body = "{\"itemId\":1,\"start\":\"not-a-date\",\"end\":\"2026-06-01T12:00:00\"}";
        assertThatThrownBy(() -> json.parse(body).getObject())
                .isInstanceOf(JsonProcessingException.class);
    }
}
