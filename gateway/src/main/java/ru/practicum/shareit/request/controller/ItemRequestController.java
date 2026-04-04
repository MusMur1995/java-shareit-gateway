package ru.practicum.shareit.request.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.client.ItemRequestClient;
import ru.practicum.shareit.request.dto.ItemRequestDto;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemRequestController {
    private final ItemRequestClient requestClient;
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";


    @PostMapping
    public ResponseEntity<Object> addRequest(@RequestHeader(USER_ID_HEADER) @Positive Long userId,
                                             @Valid @RequestBody ItemRequestDto dto) {

        log.info("Gateway: создание запроса userId={}, dto={}", userId, dto);

        return requestClient.addRequest(userId, dto);
    }


    @GetMapping
    public ResponseEntity<Object> getUserRequests(
            @RequestHeader(USER_ID_HEADER) Long userId) {

        log.info("Gateway: получение запросов пользователя userId={}", userId);

        return requestClient.getUserRequests(userId);
    }


    @GetMapping("/all")
    public ResponseEntity<Object> getAllRequests(@RequestHeader(USER_ID_HEADER) Long userId) {

        log.info("Gateway: получение всех запросов userId={}", userId);

        return requestClient.getAllRequests(userId);
    }


    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getItemRequest(@PathVariable Long requestId) {

        log.info("Gateway: получение запроса requestId={}", requestId);

        return requestClient.getRequestById(requestId);
    }
}