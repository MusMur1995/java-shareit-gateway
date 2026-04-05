package ru.practicum.shareit.request.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ItemRequestRepositoryTest {

    @Autowired
    private ItemRequestRepository itemRequestRepository;

    @Autowired
    private UserRepository userRepository;


    @Test
    void save() {
        User user = new User();
        user.setName("Диана");
        user.setEmail("Wonder@Woman.com");
        User savedUser = userRepository.save(user);

        ItemRequest request = new ItemRequest();
        request.setDescription("Ищу лассо истины");
        request.setRequestor(savedUser);
        request.setCreated(LocalDateTime.of(2025, 4, 6, 18, 40));
        itemRequestRepository.save(request);

        assertNotNull(request.getId());
        assertEquals(request.getRequestor().getId(), savedUser.getId());
    }

    @Test
    void findByRequestorIdOrderByCreatedDesc() {
        User user = new User();
        user.setName("Т'Чалла");
        user.setEmail("Wakanda@forever.com");
        User requestor = userRepository.save(user);

        ItemRequest request1 = new ItemRequest();
        request1.setDescription("Ищу волшебный цветок");
        request1.setRequestor(requestor);
        request1.setCreated(LocalDateTime.of(2025, 5, 7, 19, 40));
        ItemRequest savedRequest1 = itemRequestRepository.save(request1);

        ItemRequest request2 = new ItemRequest();
        request2.setDescription("Ищу волшебный цветок");
        request2.setRequestor(requestor);
        request2.setCreated(LocalDateTime.of(2025, 5, 7, 19, 55));
        ItemRequest savedRequest2 = itemRequestRepository.save(request2);

        List<ItemRequest> requests = itemRequestRepository.findByRequestorIdOrderByCreatedDesc(requestor.getId());

        assertEquals(requests.getFirst().getId(), savedRequest2.getId());
        assertEquals(requests.getLast().getId(), savedRequest1.getId());
        assertTrue(requests.getLast().getCreated().isBefore(requests.getFirst().getCreated()));
    }

}