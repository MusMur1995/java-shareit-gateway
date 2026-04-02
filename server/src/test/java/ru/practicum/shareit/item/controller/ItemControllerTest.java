package ru.practicum.shareit.item.controller;

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
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ItemControllerTest {

    @Mock
    private ItemService itemService;

    @InjectMocks
    private ItemController controller;

    private MockMvc mvc;
    private ObjectMapper mapper = new ObjectMapper();


    @BeforeEach
    void setup() {
        mvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();
    }

    @Test
    void addItem() throws Exception {
        ItemDto dto = ItemDto.builder()
                .id(1L)
                .name("Canon F‑1n")
                .description("Фотоаппарат японского производства")
                .available(true)
                .build();

        when(itemService.createItem(eq(8L), any()))
                .thenReturn(dto);

        mvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 8L)
                        .content(mapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Canon F‑1n"));
    }

    @Test
    void addComment() throws Exception {
        mapper.registerModule(new JavaTimeModule());

        CommentDto dto = new CommentDto(6L, "Отличный фотоаппарат!",
                "Miles Morales", LocalDateTime.of(2026, 1, 14, 20, 45));

        when(itemService.addComment(eq(10L), eq(12L), any()))
                .thenReturn(dto);

        mvc.perform(post("/items/12/comment")
                        .header("X-Sharer-User-Id", 10L)
                        .content(mapper.writeValueAsBytes(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(6))
                .andExpect(jsonPath("$.authorName").value("Miles Morales"));
    }

    @Test
    void updateItem() throws Exception {
        ItemDto dto = ItemDto.builder()
                .id(7L)
                .name("Karcher CVH 3")
                .description("Пылесос для автомобиля")
                .available(true)
                .build();

        when(itemService.updateItem(eq(4L), eq(7L), any()))
                .thenReturn(dto);

        mvc.perform(patch("/items/7")
                        .header("X-Sharer-User-Id", 4L)
                        .content(mapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(7))
                .andExpect(jsonPath("$.name").value("Karcher CVH 3"));
    }

    @Test
    void getItem() throws Exception {
        ItemDto dto = ItemDto.builder()
                .id(11L)
                .name("Палатка")
                .description("Туристическая палатка на 6 человек")
                .available(true)
                .build();

        when(itemService.getAboutItem(eq(11L)))
                .thenReturn(dto);

        mvc.perform(get("/items/11")
                        .header("X-Sharer-User-Id", 11L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(11))
                .andExpect(jsonPath("$.name").value("Палатка"));
    }

    @Test
    void getUserItems() throws Exception {
        ItemDto firstItemDto = ItemDto.builder()
                .id(1L)
                .name("Мьёльнир")
                .description("Берите, если сможете поднять")
                .available(true)
                .build();

        ItemDto secondItemDto = ItemDto.builder()
                .id(2L)
                .name("Разрушитель")
                .description("Тор всё сломал, теперь только на металл")
                .available(true)
                .build();

        List<ItemDto> items = List.of(firstItemDto, secondItemDto);

        when(itemService.getUserItems(eq(12L)))
                .thenReturn(items);

        mvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 12L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));
    }

    @Test
    void searchITem() throws Exception {
        ItemDto firstItemDto = ItemDto.builder()
                .id(34L)
                .name("Паутиномёты")
                .description("Выпускают верёвку с липким наконечником")
                .available(true)
                .build();

        ItemDto secondItemDto = ItemDto.builder()
                .id(35L)
                .name("Паутина")
                .description("Снаряды для паутиномётов")
                .available(true)
                .build();

        String text = "Паути";

        List<ItemDto> items = List.of(firstItemDto, secondItemDto);

        when(itemService.searchItems(eq(text)))
                .thenReturn(items);

        mvc.perform(get("/items/search")
                        .param("text", text))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(34))
                .andExpect(jsonPath("$[1].id").value(35));
    }
}
