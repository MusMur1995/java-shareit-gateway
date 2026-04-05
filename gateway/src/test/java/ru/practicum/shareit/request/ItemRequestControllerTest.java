package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.client.ItemRequestClient;
import ru.practicum.shareit.request.controller.ItemRequestController;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class)
class ItemRequestControllerTest {

    @MockBean
    private ItemRequestClient requestClient;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper = new ObjectMapper();

    private ItemRequestDto dto = ItemRequestDto.builder()
            .id(1L)
            .description("Поиск инструмента")
            .created(LocalDateTime.of(2026, 1, 1, 11, 0))
            .build();


    @Test
    void shouldReturnOk_whenAddRequest() throws Exception {
        when(requestClient.addRequest(anyLong(), any()))
                .thenReturn(ResponseEntity.ok(dto));

        mvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(dto.getId()))
                .andExpect(jsonPath("$.description").value(dto.getDescription()));
    }

    @Test
    void shouldReturnOk_whenGetUserRequests() throws Exception {
        when(requestClient.getUserRequests(anyLong()))
                .thenReturn(ResponseEntity.ok(List.of(dto)));

        mvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnOk_whenGetAllRequests() throws Exception {
        when(requestClient.getAllRequests(anyLong()))
                .thenReturn(ResponseEntity.ok(List.of(dto)));

        mvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnOk_whenGetItemRequest() throws Exception {
        when(requestClient.getRequestById(anyLong()))
                .thenReturn(ResponseEntity.ok(dto));

        mvc.perform(get("/requests/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(dto.getId()));
    }
}
