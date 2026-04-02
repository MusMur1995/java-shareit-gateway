package ru.practicum.shareit.user.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void save() {
        User user = new User();
        user.setName("Reed");
        user.setEmail("Richards@foursome.com");

        User savedUser = userRepository.save(user);

        assertNotNull(savedUser.getId());
        assertEquals("Richards@foursome.com", savedUser.getEmail());
    }

    @Test
    void findAll() {
        User user1 = new User();
        user1.setName("Susan");
        user1.setEmail("Storm-Richards@foursome.com");

        User user2 = new User();
        user2.setName("Jonathan");
        user2.setEmail("Storm@foursome.com");

        User savedUser1 = userRepository.save(user1);
        User savedUser2 = userRepository.save(user2);

        List<User> users = userRepository.findAll();

        assertEquals(users.getFirst().getEmail(), savedUser1.getEmail());
        assertEquals(users.getLast().getEmail(), savedUser2.getEmail());
    }

    @Test
    void findByEmail() {
        User user = new User();
        user.setName("Reed");
        user.setEmail("Richards@foursome.com");
        userRepository.save(user);

        assertEquals("Richards@foursome.com", userRepository.findByEmail(user.getEmail()).get().getEmail());
        assertTrue(userRepository.findByEmail("Richards@foursome.com").isPresent());
        assertNotNull(userRepository.findByEmail(user.getEmail()).get().getId());
    }
}