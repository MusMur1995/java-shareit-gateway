package ru.practicum.shareit.item.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private ItemRequestRepository requestRepository;

    @Test
    void findByOwner_Id() {
        User user = new User();
        user.setName("Charles");
        user.setEmail("Xavier@school.com");
        User owner = userRepository.save(user);

        Item item1 = new Item();
        item1.setName("Cerebro");
        item1.setDescription("Устройство поиска людей и мутантов");
        item1.setAvailable(true);
        item1.setOwner(owner);

        Item item2 = new Item();
        item2.setName("Инвалидное кресло");
        item2.setDescription("Высокотехнологичное кресло");
        item2.setAvailable(true);
        item2.setOwner(owner);

        Item savedItem1 = itemRepository.save(item1);
        Item savedItem2 = itemRepository.save(item2);

        List<Item> items = itemRepository.findByOwnerId(owner.getId());

        assertEquals(items.getFirst().getId(), savedItem1.getId());
        assertEquals(items.getLast().getId(), savedItem2.getId());
    }

    @Test
    void search() {
        User user = new User();
        user.setName("Stephen");
        user.setEmail("Strange@doctor.com");
        User owner = userRepository.save(user);

        Item item = new Item();
        item.setName("Камень времени");
        item.setDescription("Один из камней бесконечности");
        item.setAvailable(true);
        item.setOwner(owner);
        Item savedItem = itemRepository.save(item);

        String text = "Камень";

        List<Item> items = itemRepository.search(text);

        assertEquals(items.getFirst().getId(), savedItem.getId());
    }

    @Test
    void findByRequestIdIn() {
        User user1 = new User();
        user1.setName("Stephen");
        user1.setEmail("Strange@doctor.com");
        User requestor = userRepository.save(user1);

        User user2 = new User();
        user2.setName("Charles");
        user2.setEmail("Xavier@school.com");
        User owner = userRepository.save(user2);

        ItemRequest request1 = new ItemRequest();
        request1.setDescription("Ищу волшебный цветок");
        request1.setRequestor(requestor);
        request1.setCreated(LocalDateTime.of(2025, 5, 7, 19, 40));
        ItemRequest savedRequest1 = requestRepository.save(request1);

        ItemRequest request2 = new ItemRequest();
        request2.setDescription("Ищу волшебный цветок");
        request2.setRequestor(requestor);
        request2.setCreated(LocalDateTime.of(2025, 5, 7, 19, 55));
        ItemRequest savedRequest2 = requestRepository.save(request2);

        Item item1 = new Item();
        item1.setName("Cerebro");
        item1.setDescription("Устройство поиска людей и мутантов");
        item1.setAvailable(true);
        item1.setOwner(owner);
        item1.setRequest(savedRequest1);
        itemRepository.save(item1);

        Item item2 = new Item();
        item2.setName("Инвалидное кресло");
        item2.setDescription("Высокотехнологичное кресло");
        item2.setAvailable(true);
        item2.setOwner(owner);
        item2.setRequest(savedRequest2);
        itemRepository.save(item2);

        List<Long> ids = List.of(savedRequest1.getId(), savedRequest2.getId());
        List<Item> items = itemRepository.findByRequestIdIn(ids);

        assertEquals(2, items.size());
        assertEquals(items.getFirst().getRequest().getId(), savedRequest1.getId());
        assertEquals(items.getLast().getRequest().getId(), savedRequest2.getId());
    }
}