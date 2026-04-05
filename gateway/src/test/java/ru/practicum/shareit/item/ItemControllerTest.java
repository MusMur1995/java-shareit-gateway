package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.client.ItemClient;
import ru.practicum.shareit.item.controller.ItemController;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
public class ItemControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private ItemClient itemClient;

    private ItemDto itemDto = ItemDto.builder()
            .id(1L)
            .name("Предмет")
            .available(true)
            .description("Описание")
            .build();

    private CommentCreateDto commentCreateDto = new CommentCreateDto("Комментарий");

    @Test
    void createItem_shouldReturnItem_whenValidRequest() throws Exception {
        when(itemClient.addItem(anyLong(), any()))
                .thenReturn(ResponseEntity.ok(itemDto));

        mvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(itemDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemDto.getId()));
    }

    @Test
    void addCommentToItem_shouldReturnOk_whenValidRequest() throws Exception {
        when(itemClient.addItem(anyLong(), any()))
                .thenReturn(ResponseEntity.ok(itemDto));

        mvc.perform(post("/items/1/comment")
                        .header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(commentCreateDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void updateItem_shouldReturnUpdatedItem_whenValidRequest() throws Exception {
        when(itemClient.updateItem(anyLong(), anyLong(), any()))
                .thenReturn(ResponseEntity.ok(itemDto));

        mvc.perform(patch("/items/1")
                        .header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(itemDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getItemById_shouldReturnItem_whenItemExists() throws Exception {
        when(itemClient.getItem(anyLong()))
                .thenReturn(ResponseEntity.ok(itemDto));

        mvc.perform(get("/items/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemDto.getId()));
    }

    @Test
    void getUserItems_shouldReturnItemsList_whenUserHasItems() throws Exception {
        when(itemClient.getUserItems(anyLong()))
                .thenReturn(ResponseEntity.ok(List.of(itemDto)));

        mvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());
    }

    @Test
    void searchItemsByText_shouldReturnItemsList_whenTextMatches() throws Exception {
        when(itemClient.searchItems(anyString()))
                .thenReturn(ResponseEntity.ok(List.of(itemDto)));

        mvc.perform(get("/items/search")
                        .param("text", "item"))
                .andExpect(status().isOk());
    }
}