package ru.practicum.shareit.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController controller;

    private MockMvc mvc;
    private ObjectMapper mapper = new ObjectMapper();


    @BeforeEach
    void setup() {
        mvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();
    }

    @Test
    void createUser() throws Exception {
        UserDto dto = UserDto.builder()
                .id(8L)
                .name("Arthur")
                .email("Curry@trident.com")
                .build();

        when(userService.saveUser(any()))
                .thenReturn(dto);

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(8))
                .andExpect(jsonPath("$.name").value("Arthur"));
    }

    @Test
    void updateUser() throws Exception {
        UserDto dto = UserDto.builder()
                .id(1L)
                .name("Bruce")
                .email("Banner@incredible.com")
                .build();

        when(userService.updateUser(anyLong(), any()))
                .thenReturn(dto);

        mvc.perform(patch("/users/1")
                        .content(mapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Bruce"));
    }

    @Test
    void getUser() throws Exception {
        UserDto dto = UserDto.builder()
                .id(2L)
                .name("Bruce")
                .email("Wayne@gotham.com")
                .build();

        when(userService.getAboutUser(anyLong()))
                .thenReturn(dto);

        mvc.perform(get("/users/2")
                        .content(mapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.email").value("Wayne@gotham.com"));
    }

    @Test
    void deleteUser() throws Exception {
        UserDto dto = UserDto.builder()
                .id(5L)
                .name("Tony")
                .email("Stark@stark-indastries.com")
                .build();

        mvc.perform(delete("/users/5"))
                .andExpect(status().isOk());
    }
}