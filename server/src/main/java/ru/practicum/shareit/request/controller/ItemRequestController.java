package ru.practicum.shareit.request.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    private final ItemRequestService requestService;
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";


    @PostMapping
    public ItemRequestDto addRequest(@RequestHeader(USER_ID_HEADER) Long userId, // POST /requests
                                     @Valid @RequestBody ItemRequestDto dto) {
        return requestService.addRequest(userId, dto);
    }

    @GetMapping
    public List<ItemRequestDto> getUserRequests(@RequestHeader(USER_ID_HEADER) Long userId) { // GET /requests
        return requestService.getUserRequests(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAllRequests(@RequestHeader(USER_ID_HEADER) Long userId) { // GET /requests/all
        return requestService.getAllRequests(userId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getItemRequest(@PathVariable Long requestId) { // GET /requests/{requestId}
        return requestService.getAboutItemRequest(requestId);
    }
}
