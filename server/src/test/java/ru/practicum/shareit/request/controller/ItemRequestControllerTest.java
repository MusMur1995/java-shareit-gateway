package ru.practicum.shareit.request.controller;

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
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ItemRequestControllerTest {

    @Mock
    private ItemRequestService requestService;

    @InjectMocks
    private ItemRequestController controller;

    private MockMvc mvc;
    private ObjectMapper mapper = new ObjectMapper();


    @BeforeEach
    void setup() {
        mvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();
    }

    @Test
    void addRequest() throws Exception {
        mapper.registerModule(new JavaTimeModule());

        ItemRequestDto dto = ItemRequestDto.builder()
                .id(1L)
                .description("Ищу перфоратор")
                .created(LocalDateTime.of(2026, 3, 11, 10, 0))
                .build();

        when(requestService.addRequest(anyLong(), any()))
                .thenReturn(dto);

        mvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 2L)
                        .content(mapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.description").value("Ищу перфоратор"));
    }

    @Test
    void getUserRequests() throws Exception {
        User requestor = new User();
        requestor.setId(5L);
        requestor.setName("Barry Allen");
        requestor.setEmail("speed@central-city.com");

        ItemRequest request1 = new ItemRequest();
        request1.setId(7L);
        request1.setDescription("Энергетические батончики из Старлабс");
        request1.setRequestor(requestor);
        request1.setCreated(LocalDateTime.of(2026, 3, 11, 10, 30));

        ItemRequest request2 = new ItemRequest();
        request2.setId(8L);
        request2.setDescription("Кольцо для сжатия предметов");
        request2.setRequestor(requestor);
        request2.setCreated(LocalDateTime.of(2026, 3, 11, 11, 0));

        List<ItemRequestDto> requests = List.of(ItemRequestMapper.toDto(request1), ItemRequestMapper.toDto(request2));

        when(requestService.getUserRequests(eq(requestor.getId())))
                .thenReturn(requests);

        mvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 5L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(7))
                .andExpect(jsonPath("$[1].id").value(8));
    }

    @Test
    void getAllRequests() throws Exception {
        User requestor1 = new User();
        requestor1.setId(30L);
        requestor1.setName("Matt");
        requestor1.setEmail("Murdock@hells-kitchen.com");

        User requestor2 = new User();
        requestor2.setId(31L);
        requestor2.setName("Foggy");
        requestor2.setEmail("Nelson@nelson-and-murdock.com");

        ItemRequest request1 = new ItemRequest();
        request1.setId(17L);
        request1.setDescription("Тёмные очки");
        request1.setRequestor(requestor2);
        request1.setCreated(LocalDateTime.of(2026, 3, 11, 12, 30));

        ItemRequest request2 = new ItemRequest();
        request2.setId(18L);
        request2.setDescription("Белая трость");
        request2.setRequestor(requestor2);
        request2.setCreated(LocalDateTime.of(2026, 3, 11, 13, 0));

        List<ItemRequestDto> requests = List.of(ItemRequestMapper.toDto(request1), ItemRequestMapper.toDto(request2));

        when(requestService.getAllRequests(eq(30L)))
                .thenReturn(requests);

        mvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 30L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(17))
                .andExpect(jsonPath("$[1].id").value(18));
    }

    @Test
    void getItemRequest() throws Exception {
        ItemRequestDto dto = ItemRequestDto.builder()
                .id(3L)
                .description("Ищу пароочиститель")
                .created(LocalDateTime.of(2026, 3, 11, 15, 0))
                .build();

        when(requestService.getAboutItemRequest(eq(3L)))
                .thenReturn(dto);

        mvc.perform(get("/requests/3")
                        .header("X-Sharer-User-Id", 30L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.description").value("Ищу пароочиститель"));
    }
}
