package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemShortDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository requestRepository;
    private final UserService userService;
    private final ItemRepository itemRepository;


    @Override
    public ItemRequestDto addRequest(Long userId, ItemRequestDto dto) {
        User requestor = userService.getUserById(userId);
        ItemRequest request = ItemRequestMapper.toEntity(dto);

        request.setRequestor(requestor);
        request.setCreated(LocalDateTime.now());
        ItemRequest savedRequest = requestRepository.save(request);

        return ItemRequestMapper.toDto(savedRequest);
    }

    @Override
    public List<ItemRequestDto> getUserRequests(Long userId) {
        userService.getUserById(userId);

        List<ItemRequest> requests = requestRepository.findByRequestorIdOrderByCreatedDesc(userId);

        List<Long> ids = requests.stream()
                .map(ItemRequest::getId)
                .toList();

        Map<Long, List<Item>> itemsByRequests = itemRepository.findByRequestIdIn(ids).stream()
                .collect(Collectors.groupingBy(item -> item.getRequest().getId()));

        return requests.stream()
                .map(request -> ItemRequestDto.builder()
                        .id(request.getId())
                        .description(request.getDescription())
                        .created(request.getCreated())
                        .items(ItemMapper.toShortList(
                                itemsByRequests.getOrDefault(request.getId(), List.of())
                        ))
                        .build())
                .toList();
    }

    @Override
    public List<ItemRequestDto> getAllRequests(Long userId) {
        userService.getUserById(userId);

        List<ItemRequest> requests = requestRepository.findAll();

        return requests.stream()
                .filter(request -> !Objects.equals(request.getRequestor().getId(), userId))
                .map(ItemRequestMapper::toDto)
                .toList();
    }

    @Override
    public ItemRequestDto getAboutItemRequest(Long requestId) {
        ItemRequest request = getItemRequestById(requestId);

        List<ItemShortDto> answers = ItemMapper.toShortList(itemRepository.findAll().stream()
                .filter(item -> item.getRequest() != null
                        && item.getRequest().getId().equals(requestId))
                .toList());

        ItemRequestDto dto = ItemRequestMapper.toDto(request);
        dto.setItems(answers);

        return dto;
    }

    @Override
    public ItemRequest getItemRequestById(Long requestId) {
        return requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Запрос не найден"));
    }
}