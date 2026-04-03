package ru.practicum.shareit.item.client;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@TestPropertySource(properties = "shareit-server.url=http://localhost:9090")
class ItemClientTest {

    @Autowired(required = false)
    private ItemClient itemClient;

    @MockBean
    private RestTemplate restTemplate;

    @Test
    void contextLoads() {
        assertNotNull(itemClient);
    }

    @Test
    void testMethodsExist() {
        assertNotNull(itemClient);

        try {
            itemClient.addItem(1L, new ItemDto());
            itemClient.addComment(1L, 1L, new CommentCreateDto());
            itemClient.updateItem(1L, 1L, new ItemDto());
            itemClient.getItem(1L);
            itemClient.getUserItems(1L);
            itemClient.searchItems("test");
        } catch (Exception e) {
        }
    }
}