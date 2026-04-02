package ru.practicum.shareit.request.mapper;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ItemRequestMapperTest {

    @Test
    void toDto() {
        ItemRequest request = new ItemRequest();
        request.setId(1L);
        request.setDescription("Нужен инструмент");
        request.setCreated(LocalDateTime.now());

        ItemRequestDto dto = ItemRequestMapper.toDto(request);

        assertEquals(request.getId(), dto.getId());
        assertEquals(request.getDescription(), dto.getDescription());
        assertEquals(request.getCreated(), dto.getCreated());
    }

    @Test
    void toEntity() {
        LocalDateTime now = LocalDateTime.now();

        ItemRequestDto dto = ItemRequestDto.builder()
                .description("Нужен ноутбук")
                .build();

        ItemRequest request = ItemRequestMapper.toEntity(dto);

        assertEquals(dto.getDescription(), request.getDescription());
    }
}