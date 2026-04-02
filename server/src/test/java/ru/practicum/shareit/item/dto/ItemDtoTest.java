package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingShortDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@JsonTest
class ItemDtoTest {

    @Autowired
    private JacksonTester<ItemDto> json;

    @Test
    void testSerialize() throws Exception {
        BookingShortDto last = new BookingShortDto(1L, 2L);
        BookingShortDto next = new BookingShortDto(3L, 4L);

        CommentDto comment = new CommentDto(
                1L,
                "text",
                "author",
                LocalDateTime.of(2024, 1, 1, 12, 0)
        );

        ItemDto dto = ItemDto.builder()
                .id(1L)
                .name("Item")
                .description("Desc")
                .available(true)
                .lastBooking(last)
                .nextBooking(next)
                .comments(List.of(comment))
                .build();

        JsonContent<ItemDto> result = json.write(dto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("Item");
        assertThat(result).extractingJsonPathBooleanValue("$.available").isTrue();
        assertThat(result).extractingJsonPathNumberValue("$.lastBooking.id").isEqualTo(1);
        assertThat(result).extractingJsonPathNumberValue("$.nextBooking.id").isEqualTo(3);
        assertThat(result).extractingJsonPathArrayValue("$.comments").hasSize(1);
    }
}