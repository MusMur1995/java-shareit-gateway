package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface ItemRequestService {

    ItemRequestDto addRequest(Long userId, ItemRequestDto dto);

    List<ItemRequestDto> getUserRequests(Long userId);

    ItemRequest getItemRequestById(Long requestId);

    List<ItemRequestDto> getAllRequests(Long userId);

    ItemRequestDto getAboutItemRequest(Long requestId);
}

