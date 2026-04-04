package ru.practicum.shareit.booking.client;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.ResourceAccessException;
import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.booking.dto.BookingDto;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestPropertySource(properties = "shareit-server.url=http://localhost:9090")
class BookingClientTest {

    @Autowired(required = false)
    private BookingClient bookingClient;

    @Test
    void contextLoads() {
        assertNotNull(bookingClient);
    }

    @Test
    void createBooking_shouldCallMethod() {
        try {
            BookingDto dto = new BookingDto();
            bookingClient.createBooking(1L, dto);
        } catch (ResourceAccessException e) {
            assertTrue(e.getMessage().contains("Connection refused"));
        } catch (Exception ignored) {
        }
    }

    @Test
    void approveBooking_shouldCallMethod() {
        try {
            bookingClient.approveBooking(1L, 1L, true);
        } catch (ResourceAccessException e) {
            assertTrue(e.getMessage().contains("Connection refused"));
        }
    }

    @Test
    void getBooking_shouldCallMethod() {
        try {
            bookingClient.getBooking(1L, 1L);
        } catch (ResourceAccessException e) {
            assertTrue(e.getMessage().contains("Connection refused"));
        }
    }

    @Test
    void getUserBookings_shouldCallMethod() {
        try {
            bookingClient.getUserBookings(1L, BookingState.ALL);
        } catch (ResourceAccessException e) {
            assertTrue(e.getMessage().contains("Connection refused"));
        }
    }

    @Test
    void getOwnerBookings_shouldCallMethod() {
        try {
            bookingClient.getOwnerBookings(1L, BookingState.ALL);
        } catch (ResourceAccessException e) {
            assertTrue(e.getMessage().contains("Connection refused"));
        }
    }
}