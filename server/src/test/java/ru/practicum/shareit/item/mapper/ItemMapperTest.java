package ru.practicum.shareit.item.mapper;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemShortDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ItemMapperTest {

    @Test
    void toDto_withoutRequest() {
        Item item = new Item();
        item.setId(1L);
        item.setName("Item");
        item.setDescription("Desc");
        item.setAvailable(true);

        ItemDto dto = ItemMapper.toDto(item);

        assertEquals(item.getId(), dto.getId());
        assertNull(dto.getRequestId()); // важная проверка
    }

    @Test
    void toDto_withRequest() {
        ItemRequest request = new ItemRequest();
        request.setId(10L);

        Item item = new Item();
        item.setId(2L);
        item.setName("Item");
        item.setRequest(request);

        ItemDto dto = ItemMapper.toDto(item);

        assertEquals(request.getId(), Long.valueOf(dto.getRequestId()));
    }

    @Test
    void toItem() {
        ItemDto dto = ItemDto.builder()
                .id(3L)
                .name("Item")
                .description("Desc")
                .available(true)
                .build();

        Item item = ItemMapper.toItem(dto);

        assertEquals(dto.getId(), item.getId());
        assertEquals(dto.getName(), item.getName());
    }

    @Test
    void toShortDto() {
        User owner = new User();
        owner.setId(100L);

        Item item = new Item();
        item.setId(4L);
        item.setName("Item");
        item.setOwner(owner);

        ItemShortDto dto = ItemMapper.toShortDto(item);

        assertEquals(item.getId(), dto.getId());
        assertEquals(owner.getId(), dto.getOwnerId());
    }

    @Test
    void toShortList() {
        User owner = new User();
        owner.setId(1L);

        Item item1 = new Item();
        item1.setId(1L);
        item1.setName("A");
        item1.setOwner(owner);

        Item item2 = new Item();
        item2.setId(2L);
        item2.setName("B");
        item2.setOwner(owner);

        List<ItemShortDto> list = ItemMapper.toShortList(List.of(item1, item2));

        assertEquals(2, list.size());
    }
}
