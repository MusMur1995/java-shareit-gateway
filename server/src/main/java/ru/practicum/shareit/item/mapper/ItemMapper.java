package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemShortDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public class ItemMapper {


    public static ItemDto toDto(Item item) {
        ItemDto dto = ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .build();

        if (item.getRequest() != null) {
            dto.setRequestId(item.getRequest().getId().toString());
        }

        return dto;
    }

    public static Item toItem(ItemDto dto) {
        Item item = new Item();

        item.setId(dto.getId());
        item.setName(dto.getName());
        item.setDescription(dto.getDescription());
        item.setAvailable(dto.getAvailable());

        return item;
    }

    public static ItemShortDto toShortDto(Item item) {
        return new ItemShortDto(item.getId(), item.getName(), item.getOwner().getId());
    }

    public static List<ItemShortDto> toShortList(List<Item> items) {
        return items.stream()
                .map(ItemMapper::toShortDto)
                .toList();
    }
}
