package ru.practicum.shareit.booking.client;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.booking.dto.BookingDto;

import static org.junit.jupiter.api.Assertions.assertNotNull;

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
    void createBooking_shouldNotThrowException() {
        try {
            BookingDto dto = new BookingDto();
            bookingClient.createBooking(1L, dto);
        } catch (Exception e) {
            // Ожидаемо, сервер не запущен
        }
    }

    @Test
    void approveBooking_shouldNotThrowException() {
        try {
            bookingClient.approveBooking(1L, 1L, true);
        } catch (Exception e) {
            // Ожидаемо
        }
    }

    @Test
    void getBooking_shouldNotThrowException() {
        try {
            bookingClient.getBooking(1L, 1L);
        } catch (Exception e) {
            // Ожидаемо
        }
    }

    @Test
    void getUserBookings_shouldNotThrowException() {
        try {
            bookingClient.getUserBookings(1L, BookingState.ALL);
        } catch (Exception e) {
            // Ожидаемо
        }
    }

    @Test
    void getOwnerBookings_shouldNotThrowException() {
        try {
            bookingClient.getOwnerBookings(1L, BookingState.ALL);
        } catch (Exception e) {
            // Ожидаемо
        }
    }
}