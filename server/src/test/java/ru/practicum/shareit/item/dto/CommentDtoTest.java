package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class CommentDtoTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void serialize() throws Exception {
        LocalDateTime now = LocalDateTime.of(2026, 3, 19, 12, 0);

        CommentDto dto = CommentDto.builder()
                .id(1L)
                .text("Комментарий")
                .authorName("Tony Stark")
                .created(now)
                .build();

        String json = objectMapper.writeValueAsString(dto);

        assertThat(json).contains("\"id\":1");
        assertThat(json).contains("\"text\":\"Комментарий\"");
        assertThat(json).contains("\"authorName\":\"Tony Stark\"");
        assertThat(json).contains("\"created\":\"2026-03-19T12:00:00\"");
    }
}