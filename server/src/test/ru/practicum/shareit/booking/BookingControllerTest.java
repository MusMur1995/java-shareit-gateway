package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ItemShortDto;
import ru.practicum.shareit.user.dto.UserShortDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class BookingControllerTest {

    @Mock
    private BookingService bookingService;

    @InjectMocks
    private BookingController controller;

    private MockMvc mvc;
    private ObjectMapper mapper = new ObjectMapper();


    @BeforeEach
    void setup() {
        mvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();
    }

    @Test
    void confirmBooking() throws Exception {
        ItemShortDto itemShortDto = new ItemShortDto(3L, "Молоток", 5L);
        UserShortDto userShortDto = new UserShortDto(4L, "Эдуард");

        BookingDto bookingDto = BookingDto.builder()
                .id(14L)
                .start(LocalDateTime.of(2026, 1, 1, 11, 0))
                .end(LocalDateTime.of(2026, 1, 1, 11, 30))
                .status(BookingStatus.APPROVED)
                .item(itemShortDto)
                .booker(userShortDto)
                .build();

        when(bookingService.approveBooking(eq(14L), eq(4L), eq(true)))
                .thenReturn(bookingDto);

        mvc.perform(patch("/bookings/14")
                        .header("X-Sharer-User-Id", 4L)
                        .param("approved", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(14))
                .andExpect(jsonPath("$.status").value("APPROVED"));
    }

    @Test
    void getBookingById() throws Exception {
        ItemShortDto itemShortDto = new ItemShortDto(5L, "Молоток", 7L);
        UserShortDto userShortDto = new UserShortDto(6L, "Эдуард");

        BookingDto bookingDto = BookingDto.builder()
                .id(29L)
                .start(LocalDateTime.of(2026, 1, 1, 12, 0))
                .end(LocalDateTime.of(2026, 1, 1, 12, 30))
                .status(BookingStatus.WAITING)
                .item(itemShortDto)
                .booker(userShortDto)
                .build();

        when(bookingService.getAboutBooking(eq(7L), eq(29L)))
                .thenReturn(bookingDto);

        mvc.perform(get("/bookings/29")
                        .header("X-Sharer-User-Id", 7L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(29))
                .andExpect(jsonPath("$.status").value("WAITING"));
    }

    @Test
    void getUserBookings() throws Exception {
        ItemShortDto itemShortDto1 = new ItemShortDto(7L, "Молоток", 9L);
        ItemShortDto itemShortDto2 = new ItemShortDto(10L, "Отвёртка", 12L);

        UserShortDto userShortDto = new UserShortDto(8L, "Эдуард");

        BookingDto bookingDto1 = BookingDto.builder()
                .id(23L)
                .start(LocalDateTime.of(2026, 1, 1, 13, 0))
                .end(LocalDateTime.of(2026, 1, 1, 13, 30))
                .status(BookingStatus.WAITING)
                .item(itemShortDto1)
                .booker(userShortDto)
                .build();

        BookingDto bookingDto2 = BookingDto.builder()
                .id(24L)
                .start(LocalDateTime.of(2026, 1, 1, 14, 0))
                .end(LocalDateTime.of(2026, 1, 1, 14, 30))
                .status(BookingStatus.WAITING)
                .item(itemShortDto2)
                .booker(userShortDto)
                .build();

        List<BookingDto> bookings = List.of(bookingDto1, bookingDto2);

        when(bookingService.findBookingsByUserId(eq(userShortDto.getId()), eq(BookingState.ALL)))
                .thenReturn(bookings);

        mvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", userShortDto.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(23))
                .andExpect(jsonPath("$[1].id").value(24));
    }

    @Test
    void getOwnerBookings() throws Exception {
        UserShortDto owner = new UserShortDto(30L, "Том");
        UserShortDto booker = new UserShortDto(25L, "Джерри");

        ItemShortDto itemShortDto1 = new ItemShortDto(22L, "Молоко", owner.getId());
        ItemShortDto itemShortDto2 = new ItemShortDto(33L, "Пирог", owner.getId());

        BookingDto bookingDto1 = BookingDto.builder()
                .id(41L)
                .start(LocalDateTime.of(2026, 1, 1, 15, 0))
                .end(LocalDateTime.of(2026, 1, 1, 15, 30))
                .status(BookingStatus.WAITING)
                .item(itemShortDto1)
                .booker(booker)
                .build();

        BookingDto bookingDto2 = BookingDto.builder()
                .id(42L)
                .start(LocalDateTime.of(2026, 1, 1, 16, 0))
                .end(LocalDateTime.of(2026, 1, 1, 16, 30))
                .status(BookingStatus.WAITING)
                .item(itemShortDto2)
                .booker(booker)
                .build();

        List<BookingDto> bookings = List.of(bookingDto1, bookingDto2);

        when(bookingService.findBookingsByItemOwnerId(eq(owner.getId()), eq(BookingState.ALL)))
                .thenReturn(bookings);

        mvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", owner.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(41))
                .andExpect(jsonPath("$[1].id").value(42));
    }
}
