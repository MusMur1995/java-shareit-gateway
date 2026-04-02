package ru.practicum.shareit.booking.mapper;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BookingMapperTest {

    @Test
    void toEntity() {
        BookingCreateDto dto = BookingCreateDto.builder()
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusDays(1))
                .build();

        Booking booking = BookingMapper.toEntity(dto);

        assertEquals(dto.getStart(), booking.getStart());
        assertEquals(dto.getEnd(), booking.getEnd());
    }

    @Test
    void toDto() {
        User user = new User();
        user.setId(1L);

        Item item = new Item();
        item.setId(2L);
        item.setName("Item");
        item.setOwner(user);

        Booking booking = new Booking();
        booking.setId(3L);
        booking.setStart(LocalDateTime.now());
        booking.setEnd(LocalDateTime.now().plusDays(1));
        booking.setStatus(BookingStatus.APPROVED);
        booking.setItem(item);
        booking.setBooker(user);

        BookingDto dto = BookingMapper.toDto(booking);

        assertEquals(booking.getId(), dto.getId());
        assertEquals(item.getId(), dto.getItem().getId());
        assertEquals(user.getId(), dto.getBooker().getId());
    }

    @Test
    void toShortDto_notNull() {
        User user = new User();
        user.setId(10L);

        Booking booking = new Booking();
        booking.setId(5L);
        booking.setBooker(user);

        BookingShortDto dto = BookingMapper.toShortDto(booking);

        assertEquals(booking.getId(), dto.getId());
        assertEquals(user.getId(), dto.getBookerId());
    }

    @Test
    void toListDto() {
        User user = new User();
        user.setId(1L);

        Item item = new Item();
        item.setId(2L);
        item.setName("Item");
        item.setOwner(user);

        Booking booking = new Booking();
        booking.setId(3L);
        booking.setStart(LocalDateTime.now());
        booking.setEnd(LocalDateTime.now().plusDays(1));
        booking.setStatus(BookingStatus.APPROVED);
        booking.setItem(item);
        booking.setBooker(user);

        List<BookingDto> list = BookingMapper.toListDto(List.of(booking));

        assertEquals(1, list.size());
    }
}