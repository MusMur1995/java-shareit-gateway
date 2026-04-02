package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.ItemShortDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemRequestDtoTest {

    @Autowired
    private JacksonTester<ItemRequestDto> json;

    @Test
    void testSerialize() throws Exception {
        ItemShortDto item = new ItemShortDto(1L, "Молоток", 2L);

        ItemRequestDto dto = ItemRequestDto.builder()
                .id(1L)
                .description("Среднего размера")
                .created(LocalDateTime.of(2024, 1, 1, 10, 0))
                .items(List.of(item))
                .build();

        JsonContent<ItemRequestDto> result = json.write(dto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("Среднего размера");
        assertThat(result).extractingJsonPathStringValue("$.created").isEqualTo("2024-01-01T10:00:00");
        assertThat(result).extractingJsonPathArrayValue("$.items").hasSize(1);
        assertThat(result).extractingJsonPathNumberValue("$.items[0].id").isEqualTo(1);
    }
}