package ru.practicum.shareit.request.service;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest(
        properties = "spring.datasource.url=jdbc:h2:mem:shareit",
        webEnvironment = SpringBootTest.WebEnvironment.NONE
)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemRequestServiceImplTest {
    private final ItemRequestService requestService;
    private final UserService userService;
    private final ItemService itemService;
    private final EntityManager entityManager;


    @Test
    void addRequest() {
        UserDto user = userService.saveUser(UserDto.builder()
                .name("Barry Allen")
                .email("speed@central-city.com")
                .build());

        ItemRequestDto dto = ItemRequestDto.builder()
                .description("Нужен ноутбук")
                .created(LocalDateTime.now())
                .build();

        ItemRequestDto result = requestService.addRequest(user.getId(), dto);

        ItemRequest request = entityManager.find(ItemRequest.class, result.getId());

        assertEquals("Нужен ноутбук", request.getDescription());
    }

    @Test
    void getAllRequests_shouldReturnOnlyOthers() {
        UserDto first = userService.saveUser(UserDto.builder()
                .name("First")
                .email("first@mail.com")
                .build());

        UserDto second = userService.saveUser(UserDto.builder()
                .name("Second")
                .email("second@mail.com")
                .build());

        requestService.addRequest(first.getId(), ItemRequestDto.builder()
                .description("Req1")
                .created(LocalDateTime.now())
                .build());

        requestService.addRequest(second.getId(), ItemRequestDto.builder()
                .description("Req2")
                .created(LocalDateTime.now())
                .build());

        List<ItemRequestDto> result = requestService.getAllRequests(first.getId());

        assertEquals(1, result.size());
        assertEquals("Req2", result.getFirst().getDescription());
    }

    @Test
    void getAboutItemRequest_withItems() {
        UserDto owner = userService.saveUser(UserDto.builder()
                .name("Owner")
                .email("owner@mail.com")
                .build());

        UserDto requestor = userService.saveUser(UserDto.builder()
                .name("Requestor")
                .email("req@mail.com")
                .build());

        ItemRequestDto request = requestService.addRequest(requestor.getId(),
                ItemRequestDto.builder()
                        .description("Нужен инструмент")
                        .created(LocalDateTime.now())
                        .build());

        itemService.createItem(owner.getId(), ItemDto.builder()
                .name("Drill")
                .description("Tool")
                .available(true)
                .requestId(request.getId().toString())
                .build());

        ItemRequestDto result = requestService.getAboutItemRequest(request.getId());

        assertEquals(1, result.getItems().size());
    }

    @Test
    void getAboutItemRequest_withoutItems() {
        UserDto user = userService.saveUser(UserDto.builder()
                .name("User")
                .email("user@mail.com")
                .build());

        ItemRequestDto request = requestService.addRequest(user.getId(),
                ItemRequestDto.builder()
                        .description("Пустой запрос")
                        .created(LocalDateTime.now())
                        .build());

        ItemRequestDto result = requestService.getAboutItemRequest(request.getId());

        assertEquals(0, result.getItems().size());
    }

    @Test
    void getUserRequests() {
        UserDto firstUserDto = UserDto.builder()
                .name("Kurtis")
                .email("Stryker@mk.com")
                .build();

        UserDto secondDto = UserDto.builder()
                .name("Wade")
                .email("Wilson@hate-francis.com")
                .build();

        UserDto ownerDto = userService.saveUser(firstUserDto);
        UserDto requestorDto = userService.saveUser(secondDto);

        ItemRequestDto requestDto1 = ItemRequestDto.builder()
                .description("Нужен ствол")
                .created(LocalDateTime.of(2026, 3, 16, 10, 0))
                .build();

        ItemRequestDto requestDto2 = ItemRequestDto.builder()
                .description("Одного мало, нужен ещё ствол")
                .created(LocalDateTime.of(2026, 3, 16, 10, 5))
                .build();

        ItemRequestDto requestWithId1 = requestService.addRequest(requestorDto.getId(), requestDto1);
        ItemRequestDto requestWithId2 = requestService.addRequest(requestorDto.getId(), requestDto2);

        ItemDto firstItemDto = ItemDto.builder()
                .name("Desert Eagle Mark XIX")
                .description("Необходимо предъявить разрешение")
                .available(true)
                .requestId(requestWithId1.getId().toString())
                .build();

        ItemDto secondItemDto = ItemDto.builder()
                .name("Beretta 92FS")
                .description("Необходимо предъявить разрешение")
                .available(true)
                .requestId(requestWithId2.getId().toString())
                .build();

        itemService.createItem(ownerDto.getId(), firstItemDto);
        itemService.createItem(ownerDto.getId(), secondItemDto);

        List<ItemRequestDto> requests = requestService.getUserRequests(requestorDto.getId());

        ItemRequest request1 = entityManager.createQuery(
                "SELECT r FROM ItemRequest r WHERE r.id = :requestId", ItemRequest.class)
                .setParameter("requestId", requestWithId1.getId())
                .getSingleResult();

        ItemRequest request2 = entityManager.createQuery(
                        "SELECT r FROM ItemRequest r WHERE r.id = :requestId", ItemRequest.class)
                .setParameter("requestId", requestWithId2.getId())
                .getSingleResult();

        assertEquals(request2.getId(), requests.getFirst().getId());
        assertEquals(request1.getId(), requests.getLast().getId());
    }

    @Test
    void getItemRequestById_notFound_shouldThrow() {
        assertThrows(NotFoundException.class,
                () -> requestService.getItemRequestById(999L));
    }
}