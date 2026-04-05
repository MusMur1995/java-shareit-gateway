package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    List<ItemDto> getUserItems(Long userId);

    Item getItemById(Long itemId);

    ItemDto createItem(Long userId, ItemDto itemDto);

    ItemDto updateItem(Long userId, Long itemId, ItemDto itemDto);

    List<ItemDto> searchItems(String text);

    List<Item> getItemsByRequestIdIn(List<Long> ids);

    ItemDto getAboutItem(Long id);

    List<Item> getAllItems();

    List<Item> findByOwnerId(Long userId);

    CommentDto addComment(Long userId, Long itemId, CommentCreateDto commentCreateDto);

    void deleteItem(Long itemId);
}