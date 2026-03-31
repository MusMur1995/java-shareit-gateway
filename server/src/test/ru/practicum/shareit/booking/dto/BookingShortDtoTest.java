package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BookingShortDtoTest {

    @Test
    void allArgsConstructor() {
        BookingShortDto dto = new BookingShortDto(2L, 200L);

        assertEquals(2L, dto.getId());
        assertEquals(200L, dto.getBookerId());
    }

    @Test
    void builder() {
        BookingShortDto dto = BookingShortDto.builder()
                .id(3L)
                .bookerId(300L)
                .build();

        assertEquals(3L, dto.getId());
        assertEquals(300L, dto.getBookerId());
    }
}
