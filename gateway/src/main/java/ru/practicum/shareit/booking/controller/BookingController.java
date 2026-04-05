package ru.practicum.shareit.booking.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.booking.client.BookingClient;
import ru.practicum.shareit.booking.dto.BookingDto;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private final BookingClient bookingClient;
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";


    @PostMapping
    public ResponseEntity<Object> addBooking(@RequestHeader(USER_ID_HEADER) Long userId,
                                             @RequestBody @Valid BookingDto dto) {

        log.info("Gateway: создание бронирования, userId={}, dto={}", userId, dto);

        return bookingClient.createBooking(userId, dto);
    }


    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> confirmBooking(@PathVariable Long bookingId,
                                                 @RequestHeader(USER_ID_HEADER) Long userId,
                                                 @RequestParam Boolean approved) {

        log.info("Gateway: подтверждение бронирования, bookingId={}, userId={}, approved={}",
                bookingId, userId, approved);

        return bookingClient.approveBooking(bookingId, userId, approved);
    }


    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBookingById(@RequestHeader(USER_ID_HEADER) Long userId,
                                                 @PathVariable Long bookingId) {

        log.info("Gateway: получение бронирования по ID, bookingId={}, userId={}", bookingId, userId);

        return bookingClient.getBooking(userId, bookingId);
    }


    @GetMapping
    public ResponseEntity<Object> getUserBookings(@RequestHeader(USER_ID_HEADER) Long userId,
                                                  @RequestParam(defaultValue = "ALL") BookingState state) {

        log.info("Gateway: получение списка бронирований пользователя, userId={}, state={}", userId, state);

        return bookingClient.getUserBookings(userId, state);
    }


    @GetMapping("/owner")
    public ResponseEntity<Object> getOwnerBookings(@RequestHeader(USER_ID_HEADER) Long ownerId,
                                                   @RequestParam(defaultValue = "ALL") BookingState state) {

        log.info("Gateway: получение списка бронирований владельца, ownerId={}, state={}", ownerId, state);

        return bookingClient.getOwnerBookings(ownerId, state);
    }
}