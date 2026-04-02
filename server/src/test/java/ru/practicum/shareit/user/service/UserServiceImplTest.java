package ru.practicum.shareit.user.service;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

@Transactional
@SpringBootTest(
        properties = "spring.datasource.url=jdbc:h2:mem:shareit",
        webEnvironment = SpringBootTest.WebEnvironment.NONE
)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserServiceImplTest {
    private final UserService userService;
    private final EntityManager entityManager;


    @Test
    void saveUser() {
        UserDto dto = UserDto.builder()
                .name("Michael")
                .email("Jackson@best.com")
                .build();

        UserDto afterCreate = userService.saveUser(dto);

        User savedUser = entityManager.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class)
                .setParameter("email", "Jackson@best.com")
                .getSingleResult();

        Assertions.assertEquals(savedUser.getEmail(), afterCreate.getEmail());
    }

    @Test
    void createUser_duplicateEmail_shouldThrow() {
        UserDto dto = UserDto.builder()
                .name("User")
                .email("test@mail.com")
                .build();

        userService.saveUser(dto);

        Assertions.assertThrows(ConflictException.class,
                () -> userService.saveUser(dto));
    }

    @Test
    void updateUser_success() {
        UserDto user = userService.saveUser(UserDto.builder()
                .name("Old")
                .email("old@mail.com")
                .build());

        UserDto update = UserDto.builder()
                .name("New")
                .build();

        UserDto updated = userService.updateUser(user.getId(), update);

        Assertions.assertEquals("New", updated.getName());
        Assertions.assertEquals("old@mail.com", updated.getEmail());
    }

    @Test
    void updateUser_duplicateEmail_shouldThrow() {
        UserDto first = userService.saveUser(UserDto.builder()
                .name("First")
                .email("first@mail.com")
                .build());

        UserDto second = userService.saveUser(UserDto.builder()
                .name("Second")
                .email("second@mail.com")
                .build());

        UserDto update = UserDto.builder()
                .email("first@mail.com")
                .build();

        Assertions.assertThrows(ConflictException.class,
                () -> userService.updateUser(second.getId(), update));
    }

    @Test
    void getUserById_notFound_shouldThrow() {
        Assertions.assertThrows(NotFoundException.class,
                () -> userService.getUserById(999L));
    }

    @Test
    void getAboutUser() {
        UserDto user = userService.saveUser(UserDto.builder()
                .name("User")
                .email("user@mail.com")
                .build());

        UserDto result = userService.getAboutUser(user.getId());

        Assertions.assertEquals(user.getEmail(), result.getEmail());
    }

    @Test
    void getAllUsers() {
        userService.saveUser(UserDto.builder()
                .name("One")
                .email("one@mail.com")
                .build());

        userService.saveUser(UserDto.builder()
                .name("Two")
                .email("two@mail.com")
                .build());

        Assertions.assertEquals(2, userService.getAllUsers().size());
    }

    @Test
    void deleteUser_withItems() {
        UserDto user = userService.saveUser(UserDto.builder()
                .name("User")
                .email("user@mail.com")
                .build());

        Item item = new Item();
        item.setName("Item");
        item.setDescription("Desc");
        item.setAvailable(true);
        item.setOwner(entityManager.find(User.class, user.getId()));
        entityManager.persist(item);

        userService.deleteUser(user.getId());

        User deletedUser = entityManager.find(User.class, user.getId());
        Item deletedItem = entityManager.find(Item.class, item.getId());

        Assertions.assertNull(deletedUser);
    }

    @Test
    void deleteUser_notFound_shouldThrow() {
        Assertions.assertThrows(NotFoundException.class,
                () -> userService.deleteUser(999L));
    }
}