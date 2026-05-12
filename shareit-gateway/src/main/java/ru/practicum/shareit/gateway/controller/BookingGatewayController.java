package ru.practicum.shareit.gateway.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.booking.dto.CreateBookingDto;
import ru.practicum.shareit.gateway.client.BaseClient;
import ru.practicum.shareit.gateway.client.BookingProxyClient;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
@Validated
public class BookingGatewayController {

    private final BookingProxyClient bookingProxyClient;

    @PostMapping
    public ResponseEntity<String> create(@Valid @RequestBody CreateBookingDto dto,
                                         @RequestHeader("X-Sharer-User-Id") Long userId) {
        return BaseClient.forward(bookingProxyClient.create(dto, userId));
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<String> patchStatus(@PathVariable Long bookingId,
                                              @RequestParam Boolean approved,
                                              @RequestHeader("X-Sharer-User-Id") Long ownerId) {
        return BaseClient.forward(bookingProxyClient.patchStatus(bookingId, approved, ownerId));
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<String> getById(@PathVariable Long bookingId,
                                          @RequestHeader("X-Sharer-User-Id") Long userId) {
        return BaseClient.forward(bookingProxyClient.getById(bookingId, userId));
    }

    @GetMapping
    public ResponseEntity<String> findForUser(@RequestParam(defaultValue = "ALL") String state,
                                                @RequestHeader("X-Sharer-User-Id") Long userId) {
        return BaseClient.forward(bookingProxyClient.findForUser(state, userId));
    }

    @GetMapping("/owner")
    public ResponseEntity<String> findForOwner(@RequestParam(defaultValue = "ALL") String state,
                                                 @RequestHeader("X-Sharer-User-Id") Long ownerId) {
        return BaseClient.forward(bookingProxyClient.findForOwner(state, ownerId));
    }
}
