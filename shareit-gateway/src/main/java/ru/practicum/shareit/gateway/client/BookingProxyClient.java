package ru.practicum.shareit.gateway.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.practicum.shareit.booking.dto.CreateBookingDto;

import java.util.Map;

@Component
public class BookingProxyClient extends BaseClient {

    public BookingProxyClient(RestTemplate rest, @Value("${shareit.server.url}") String serverUrl) {
        super(rest, serverUrl);
    }

    public ResponseEntity<String> create(CreateBookingDto dto, Long userId) {
        return httpPost("/bookings", dto, userId);
    }

    public ResponseEntity<String> patchStatus(Long bookingId, boolean approved, Long ownerId) {
        var uri = UriComponentsBuilder.fromUriString(base() + "/bookings/" + bookingId)
                .queryParam("approved", approved)
                .build()
                .toUri();
        return httpPatchUri(uri, ownerId);
    }

    public ResponseEntity<String> getById(Long bookingId, Long userId) {
        return httpGet("/bookings/" + bookingId, userId);
    }

    public ResponseEntity<String> findForUser(String state, Long userId) {
        return httpGet("/bookings", userId, Map.of("state", state));
    }

    public ResponseEntity<String> findForOwner(String state, Long ownerId) {
        return httpGet("/bookings/owner", ownerId, Map.of("state", state));
    }
}
