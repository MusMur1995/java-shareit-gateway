package ru.practicum.shareit.user.mapper;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserShortDto;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserMapperTest {

    @Test
    void toDto() {
        User user = new User();
        user.setId(1L);
        user.setName("Bruce");
        user.setEmail("Wayne@gotham.com");

        UserDto dto = UserMapper.toDto(user);

        assertEquals(user.getId(), dto.getId());
        assertEquals(user.getName(), dto.getName());
        assertEquals(user.getEmail(), dto.getEmail());
    }

    @Test
    void toEntity() {
        UserDto dto = UserDto.builder()
                .id(2L)
                .name("Alice")
                .email("alice@mail.com")
                .build();

        User user = UserMapper.toEntity(dto);

        assertEquals(dto.getId(), user.getId());
        assertEquals(dto.getName(), user.getName());
        assertEquals(dto.getEmail(), user.getEmail());
    }

    @Test
    void toShortDto() {
        User user = new User();
        user.setId(3L);
        user.setName("Bob");

        UserShortDto shortDto = UserMapper.toShortDto(user);

        assertEquals(user.getId(), shortDto.getId());
        assertEquals(user.getName(), shortDto.getName());
    }
}