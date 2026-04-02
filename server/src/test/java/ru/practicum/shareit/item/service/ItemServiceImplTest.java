package ru.practicum.shareit.item.service;

import jakarta.persistence.EntityManager;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest(
        properties = "spring.datasource.url=jdbc:h2:mem:shareit",
        webEnvironment = SpringBootTest.WebEnvironment.NONE
)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemServiceImplTest {
    private final ItemService itemService;
    private final UserService userService;
    private final EntityManager entityManager;


    @Test
    void addItem() {
        UserDto user = userService.saveUser(UserDto.builder()
                .name("Eric")
                .email("Brooks@hate-vampire.com")
                .build());

        ItemRequest request = new ItemRequest();
        request.setDescription("Ищу меч");
        request.setCreated(LocalDateTime.now());
        request.setRequestor(entityManager.find(User.class, user.getId()));
        entityManager.persist(request);

        ItemDto dto = ItemDto.builder()
                .name("Меч")
                .description("Брать только Блэйду")
                .available(true)
                .requestId(request.getId().toString())
                .build();

        ItemDto saved = itemService.createItem(user.getId(), dto);

        Item item = entityManager.find(Item.class, saved.getId());

        assertEquals("Меч", item.getName());
        assertEquals(request.getId(), item.getRequest().getId());
    }

    @Test
    void addComment() {
        UserDto owner = userService.saveUser(UserDto.builder()
                .name("Naruto")
                .email("Uzumaki@ninja.com")
                .build());

        UserDto booker = userService.saveUser(UserDto.builder()
                .name("Sasuke")
                .email("Uchiha@ninja.com")
                .build());

        ItemDto item = itemService.createItem(owner.getId(), ItemDto.builder()
                .name("Rasengan")
                .description("Техника")
                .available(true)
                .build());

        Booking booking = new Booking();
        booking.setItem(entityManager.find(Item.class, item.getId()));
        booking.setBooker(entityManager.find(User.class, booker.getId()));
        booking.setStart(LocalDateTime.now().minusDays(2));
        booking.setEnd(LocalDateTime.now().minusDays(1));
        booking.setStatus(BookingStatus.APPROVED);
        entityManager.persist(booking);

        CommentCreateDto dto = new CommentCreateDto();
        dto.setText("Лучшая техника в Конохе!");

        CommentDto result = itemService.addComment(booker.getId(), item.getId(), dto);

        assertEquals("Лучшая техника в Конохе!", result.getText());
    }

    @Test
    void addComment_withoutBooking() {
        UserDto user = userService.saveUser(UserDto.builder()
                .name("User")
                .email("user@mail.com")
                .build());

        ItemDto item = itemService.createItem(user.getId(), ItemDto.builder()
                .name("Item")
                .description("Desc")
                .available(true)
                .build());

        CommentCreateDto dto = new CommentCreateDto();
        dto.setText("Неудачный комментарий");

        assertThrows(ValidationException.class,
                () -> itemService.addComment(user.getId(), item.getId(), dto));
    }

    @Test
    void updateItem() {
        UserDto user = userService.saveUser(UserDto.builder()
                .name("Abraham")
                .email("Van-Helsing@kill-vampire.com")
                .build());

        ItemDto item = itemService.createItem(user.getId(), ItemDto.builder()
                .name("Арбалет")
                .description("В комплекте стрелы")
                .available(true)
                .build());

        ItemDto update = ItemDto.builder()
                .name("Hugh")
                .build();

        ItemDto updated = itemService.updateItem(user.getId(), item.getId(), update);

        assertEquals("Hugh", updated.getName());
    }

    @Test
    void updateItem_notOwner_shouldThrow() {
        UserDto owner = userService.saveUser(UserDto.builder()
                .name("Owner")
                .email("owner@mail.com")
                .build());

        UserDto other = userService.saveUser(UserDto.builder()
                .name("Other")
                .email("other@mail.com")
                .build());

        ItemDto item = itemService.createItem(owner.getId(), ItemDto.builder()
                .name("Item")
                .description("Desc")
                .available(true)
                .build());

        ItemDto update = ItemDto.builder().name("Hack").build();

        assertThrows(AccessDeniedException.class,
                () -> itemService.updateItem(other.getId(), item.getId(), update));
    }

    @Test
    void updateItem_descriptionAndAvailable() {
        UserDto user = userService.saveUser(UserDto.builder()
                .name("User")
                .email("user@test.com")
                .build());

        ItemDto item = itemService.createItem(user.getId(), ItemDto.builder()
                .name("Item")
                .description("Old desc")
                .available(true)
                .build());

        ItemDto update = ItemDto.builder()
                .description("New desc")
                .available(false)
                .build();

        ItemDto updated = itemService.updateItem(user.getId(), item.getId(), update);

        assertEquals("New desc", updated.getDescription());
        assertFalse(updated.getAvailable());
    }

    @Test
    void getAboutItem() {
        UserDto user = userService.saveUser(UserDto.builder()
                .name("Orochimaro")
                .email("Snake@ninja.com")
                .build());

        ItemDto item = itemService.createItem(user.getId(), ItemDto.builder()
                .name("Эдо тенсей")
                .description("Техника воскрешения")
                .available(true)
                .build());

        Comment comment = new Comment();
        comment.setText("Нужна жертва");
        comment.setItem(entityManager.find(Item.class, item.getId()));
        comment.setAuthor(entityManager.find(User.class, user.getId()));
        comment.setCreated(LocalDateTime.now());
        entityManager.persist(comment);

        ItemDto result = itemService.getAboutItem(item.getId());

        assertEquals(1, result.getComments().size());
    }

    @Test
    void getAllItems() {
        UserDto user = userService.saveUser(UserDto.builder()
                .name("Кларк")
                .email("Kent@smallville.com")
                .build());

        itemService.createItem(user.getId(), ItemDto.builder()
                .name("Karcher CVH 3")
                .description("Пылесос для автомобиля")
                .available(true)
                .build());

        itemService.createItem(user.getId(), ItemDto.builder()
                .name("Арбалет")
                .description("В комплекте стрелы")
                .available(true)
                .build());

        List<Item> items = itemService.getAllItems();

        assertEquals(2, items.size());
    }

    @Test
    void getUserItems() {
        UserDto userDto = UserDto.builder()
                .name("Кларк")
                .email("Kent@smallville.com")
                .build();
        UserDto savedUser = userService.saveUser(userDto);

        ItemDto firstItemDto = ItemDto.builder()
                .name("Karcher CVH 3")
                .description("Пылесос для автомобиля")
                .available(true)
                .build();

        ItemDto secondItemDto = ItemDto.builder()
                .name("Палатка")
                .description("Туристическая палатка на 6 человек")
                .available(true)
                .build();

        itemService.createItem(savedUser.getId(), firstItemDto);
        itemService.createItem(savedUser.getId(), secondItemDto);

        List<ItemDto> items = itemService.getUserItems(savedUser.getId());


        Item firstItem = entityManager.createQuery("SELECT i FROM Item i WHERE i.name = :name", Item.class)
                .setParameter("name", "Karcher CVH 3")
                .getSingleResult();

        Item secondItem = entityManager.createQuery("SELECT i FROM Item i WHERE i.name = :name", Item.class)
                .setParameter("name", "Палатка")
                .getSingleResult();


        assertEquals(items.getFirst().getName(), firstItem.getName());
        assertEquals(items.getLast().getDescription(), secondItem.getDescription());
    }

    @Test
    void getItemsByRequestIdIn() {
        UserDto user = userService.saveUser(UserDto.builder()
                .name("ReqUser")
                .email("req@mail.com")
                .build());

        ItemRequest request = new ItemRequest();
        request.setDescription("Запрос");
        request.setCreated(LocalDateTime.now());
        request.setRequestor(entityManager.find(User.class, user.getId()));
        entityManager.persist(request);

        itemService.createItem(user.getId(), ItemDto.builder()
                .name("Item")
                .description("Desc")
                .available(true)
                .requestId(request.getId().toString())
                .build());

        List<Item> result = itemService.getItemsByRequestIdIn(List.of(request.getId()));

        assertEquals(1, result.size());
        assertEquals(request.getId(), result.getFirst().getRequest().getId());
    }

    @Test
    void searchItems() {
        UserDto user = userService.saveUser(UserDto.builder()
                .name("Shikamaru")
                .email("nara@ino-shiko-che.com")
                .build());

        itemService.createItem(user.getId(), ItemDto.builder()
                .name("Серьги")
                .description("Символ от клана Сарутоби")
                .available(true)
                .build());

        List<ItemDto> result = itemService.searchItems("Серьги");

        assertEquals(1, result.size());
    }

    @Test
    void searchItems_notFound_shouldReturnEmpty() {
        UserDto user = userService.saveUser(UserDto.builder()
                .name("SearchUser")
                .email("search@mail.com")
                .build());

        itemService.createItem(user.getId(), ItemDto.builder()
                .name("Телефон")
                .description("iPhone")
                .available(true)
                .build());

        List<ItemDto> result = itemService.searchItems("Ноутбук");

        assertTrue(result.isEmpty());
    }

    @Test
    void searchItems_blank_shouldReturnEmpty() {
        List<ItemDto> result = itemService.searchItems("");

        assertEquals(0, result.size());
    }

    @Test
    void searchItems_null_shouldReturnEmpty() {
        List<ItemDto> result = itemService.searchItems(null);

        assertTrue(result.isEmpty());
    }

    @Test
    void findByOwnerId() {
        UserDto user = userService.saveUser(UserDto.builder()
                .name("Owner")
                .email("owner@test.com")
                .build());

        itemService.createItem(user.getId(), ItemDto.builder()
                .name("Item1")
                .description("Desc1")
                .available(true)
                .build());

        itemService.createItem(user.getId(), ItemDto.builder()
                .name("Item2")
                .description("Desc2")
                .available(true)
                .build());

        List<Item> items = itemService.findByOwnerId(user.getId());

        assertEquals(2, items.size());
    }

    @Test
    void deleteItem() {
        UserDto user = userService.saveUser(UserDto.builder()
                .name("Madara")
                .email("Sharingan@ninja.com")
                .build());

        ItemDto item = itemService.createItem(user.getId(), ItemDto.builder()
                .name("Rinnegan")
                .description("Глаз Сансары")
                .available(true)
                .build());

        itemService.deleteItem(item.getId());

        Item found = entityManager.find(Item.class, item.getId());

        assertNull(found);
    }
}
