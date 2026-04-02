package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class BookingRepositoryTest {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;


    @Test
    void findByBookerIdOrderByStartDesc() {
        User user1 = new User();
        user1.setName("Name1");
        user1.setEmail("name1@mail.com");
        User owner = userRepository.save(user1);

        User user2 = new User();
        user2.setName("Name2");
        user2.setEmail("name2@mail.com");
        User booker = userRepository.save(user2);

        Item item = new Item();
        item.setName("Item");
        item.setDescription("Very good item");
        item.setAvailable(true);
        item.setOwner(owner);
        Item savedItem = itemRepository.save(item);

        Booking booking = new Booking();
        booking.setStart(LocalDateTime.of(2026, 4, 29, 1, 0));
        booking.setEnd(LocalDateTime.of(2026, 4, 29, 4, 0));
        booking.setItem(savedItem);
        booking.setBooker(booker);
        booking.setStatus(BookingStatus.WAITING);
        Booking savedBooking1 = bookingRepository.save(booking);

        Booking booking2 = new Booking();
        booking2.setStart(LocalDateTime.of(2026, 4, 30, 1, 0));
        booking2.setEnd(LocalDateTime.of(2026, 4, 30, 4, 0));
        booking2.setItem(savedItem);
        booking2.setBooker(booker);
        booking2.setStatus(BookingStatus.WAITING);
        Booking savedBooking2 = bookingRepository.save(booking2);

        List<Booking> bookings = bookingRepository.findByBookerIdOrderByStartDesc(booker.getId());

        assertTrue(bookings.getFirst().getStart().isAfter(bookings.getLast().getStart()));
        assertEquals(2, bookings.size());
        assertEquals(booker.getId(), bookings.getFirst().getBooker().getId());
        assertEquals(booker.getId(), bookings.getLast().getBooker().getId());
        assertEquals(savedBooking2.getId(), bookings.getFirst().getId());
        assertEquals(savedBooking1.getId(), bookings.getLast().getId());
    }

    @Test
    void findCurrentBookings() {
        User user1 = new User();
        user1.setName("Name1");
        user1.setEmail("name1@mail.com");
        User owner = userRepository.save(user1);

        User user2 = new User();
        user2.setName("Name2");
        user2.setEmail("name2@mail.com");
        User booker = userRepository.save(user2);

        Item item = new Item();
        item.setName("Item");
        item.setDescription("Very good item");
        item.setAvailable(true);
        item.setOwner(owner);
        Item savedItem = itemRepository.save(item);

        Item item2 = new Item();
        item2.setName("Item2");
        item2.setDescription("Very good item");
        item2.setAvailable(true);
        item2.setOwner(owner);
        Item savedItem2 = itemRepository.save(item2);

        Booking booking = new Booking();
        booking.setStart(LocalDateTime.of(2026, 3, 15, 18, 0));
        booking.setEnd(LocalDateTime.of(2026, 4, 15, 18, 0));
        booking.setItem(savedItem);
        booking.setBooker(booker);
        booking.setStatus(BookingStatus.APPROVED);
        Booking savedBooking1 = bookingRepository.save(booking);

        Booking booking2 = new Booking();
        booking2.setStart(LocalDateTime.of(2026, 3, 18, 18, 0));
        booking2.setEnd(LocalDateTime.of(2026, 4, 18, 18, 0));
        booking2.setItem(savedItem2);
        booking2.setBooker(booker);
        booking2.setStatus(BookingStatus.APPROVED);
        Booking savedBooking2 = bookingRepository.save(booking2);

        List<Booking> bookings = bookingRepository.findCurrentBookings(booker.getId(), LocalDateTime.now());

        assertTrue(bookings.getFirst().getStart().isBefore(LocalDateTime.now()));
        assertTrue(bookings.getFirst().getEnd().isAfter(LocalDateTime.now()));
        assertTrue(bookings.getLast().getStart().isBefore(LocalDateTime.now()));
        assertTrue(bookings.getLast().getEnd().isAfter(LocalDateTime.now()));
        assertTrue(bookings.getFirst().getStart().isAfter(bookings.getLast().getStart()));
        assertEquals(bookings.getFirst().getId(), savedBooking2.getId());
        assertEquals(bookings.getLast().getId(), savedBooking1.getId());
    }

    @Test
    void findByBookerIdAndEndBeforeOrderByStartDesc() {
        User user1 = new User();
        user1.setName("Наташа");
        user1.setEmail("Romanova@Widow.com");
        User owner = userRepository.save(user1);

        User user2 = new User();
        user2.setName("Клинт");
        user2.setEmail("Barton@Hawkeye.com");
        User booker = userRepository.save(user2);

        Item item = new Item();
        item.setName("Камень разума");
        item.setDescription("Один из камней бесконечности");
        item.setAvailable(true);
        item.setOwner(owner);
        Item sacrifice = itemRepository.save(item);

        Booking booking = new Booking();
        booking.setStart(LocalDateTime.of(2019, 4, 29, 1, 53, 52));
        booking.setEnd(LocalDateTime.of(2019, 4, 29, 1, 53, 59));
        booking.setItem(sacrifice);
        booking.setBooker(booker);
        booking.setStatus(BookingStatus.APPROVED);
        Booking weight = bookingRepository.save(booking);

        Booking secondBooking = new Booking();
        secondBooking.setStart(LocalDateTime.of(2020, 4, 29, 1, 53, 52));
        secondBooking.setEnd(LocalDateTime.of(2020, 4, 29, 1, 53, 59));
        secondBooking.setItem(sacrifice);
        secondBooking.setBooker(booker);
        secondBooking.setStatus(BookingStatus.APPROVED);
        Booking weight2 = bookingRepository.save(secondBooking);

        List<Booking> bookings =
                bookingRepository.findByBookerIdAndEndBeforeOrderByStartDesc(booker.getId(), LocalDateTime.now());

        assertTrue(bookings.getFirst().getEnd().isBefore(LocalDateTime.now()));
        assertTrue(bookings.getFirst().getStart().isAfter(bookings.getLast().getStart()));
        assertEquals(bookings.getFirst().getId(), weight2.getId());
        assertEquals(bookings.getLast().getId(), weight.getId());
        assertEquals(2, bookings.size());
    }

    @Test
    void findByBookerIdAndStartAfterOrderByStartAsc() {
        User user1 = new User();
        user1.setName("Name1");
        user1.setEmail("name1@mail.com");
        User owner = userRepository.save(user1);

        User user2 = new User();
        user2.setName("Name2");
        user2.setEmail("name2@mail.com");
        User booker = userRepository.save(user2);

        Item item = new Item();
        item.setName("Item");
        item.setDescription("Very good item");
        item.setAvailable(true);
        item.setOwner(owner);
        Item savedItem = itemRepository.save(item);

        Booking booking = new Booking();
        booking.setStart(LocalDateTime.of(2030, 4, 29, 1, 0));
        booking.setEnd(LocalDateTime.of(2030, 4, 29, 4, 0));
        booking.setItem(savedItem);
        booking.setBooker(booker);
        booking.setStatus(BookingStatus.APPROVED);
        Booking savedBooking1 = bookingRepository.save(booking);

        Booking booking2 = new Booking();
        booking2.setStart(LocalDateTime.of(2030, 4, 30, 1, 0));
        booking2.setEnd(LocalDateTime.of(2030, 4, 30, 4, 0));
        booking2.setItem(savedItem);
        booking2.setBooker(booker);
        booking2.setStatus(BookingStatus.APPROVED);
        Booking savedBooking2 = bookingRepository.save(booking2);

        List<Booking> bookings = bookingRepository
                .findByBookerIdAndStartAfterOrderByStartAsc(booker.getId(), LocalDateTime.now());

        assertTrue(bookings.getFirst().getStart().isAfter(LocalDateTime.now()));
        assertTrue(bookings.getFirst().getStart().isBefore(bookings.getLast().getStart()));
        assertEquals(bookings.getFirst().getId(), savedBooking1.getId());
        assertEquals(bookings.getLast().getId(), savedBooking2.getId());
        assertEquals(2, bookings.size());
    }

    @Test
    void findByBookerIdAndStatusOrderByStartDesc() {
        User user1 = new User();
        user1.setName("Name1");
        user1.setEmail("name1@mail.com");
        User owner = userRepository.save(user1);

        User user2 = new User();
        user2.setName("Name2");
        user2.setEmail("name2@mail.com");
        User booker = userRepository.save(user2);

        Item item = new Item();
        item.setName("Item");
        item.setDescription("Very good item");
        item.setAvailable(true);
        item.setOwner(owner);
        Item savedItem = itemRepository.save(item);

        Booking booking = new Booking();
        booking.setStart(LocalDateTime.of(2026, 4, 29, 1, 0));
        booking.setEnd(LocalDateTime.of(2026, 4, 29, 4, 0));
        booking.setItem(savedItem);
        booking.setBooker(booker);
        booking.setStatus(BookingStatus.WAITING);
        Booking savedBooking1 = bookingRepository.save(booking);

        Booking booking2 = new Booking();
        booking2.setStart(LocalDateTime.of(2026, 4, 30, 1, 0));
        booking2.setEnd(LocalDateTime.of(2026, 4, 30, 4, 0));
        booking2.setItem(savedItem);
        booking2.setBooker(booker);
        booking2.setStatus(BookingStatus.WAITING);
        Booking savedBooking2 = bookingRepository.save(booking2);

        List<Booking> bookings = bookingRepository
                .findByBookerIdAndStatusOrderByStartDesc(booker.getId(), BookingStatus.WAITING);

        assertEquals(2, bookings.size());
        assertEquals(BookingStatus.WAITING, bookings.getFirst().getStatus());
        assertEquals(BookingStatus.WAITING, bookings.getLast().getStatus());
        assertEquals(savedBooking2.getId(), bookings.getFirst().getId());
        assertEquals(savedBooking1.getId(), bookings.getLast().getId());
        assertTrue(bookings.getFirst().getStart().isAfter(bookings.getLast().getStart()));
    }

    @Test
    void findByItemOwnerIdOrderByStartDesc() {
        User user1 = new User();
        user1.setName("Name1");
        user1.setEmail("name1@mail.com");
        User owner = userRepository.save(user1);

        User user2 = new User();
        user2.setName("Name2");
        user2.setEmail("name2@mail.com");
        User booker = userRepository.save(user2);

        Item item = new Item();
        item.setName("Item");
        item.setDescription("Very good item");
        item.setAvailable(true);
        item.setOwner(owner);
        Item savedItem = itemRepository.save(item);

        Booking booking = new Booking();
        booking.setStart(LocalDateTime.of(2026, 4, 29, 1, 0));
        booking.setEnd(LocalDateTime.of(2026, 4, 29, 4, 0));
        booking.setItem(savedItem);
        booking.setBooker(booker);
        booking.setStatus(BookingStatus.WAITING);
        bookingRepository.save(booking);

        Booking booking2 = new Booking();
        booking2.setStart(LocalDateTime.of(2026, 4, 30, 1, 0));
        booking2.setEnd(LocalDateTime.of(2026, 4, 30, 4, 0));
        booking2.setItem(savedItem);
        booking2.setBooker(booker);
        booking2.setStatus(BookingStatus.WAITING);
        bookingRepository.save(booking2);

        List<Booking> bookings = bookingRepository.findByItemOwnerIdOrderByStartDesc(owner.getId());

        assertEquals(2, bookings.size());
        assertEquals(bookings.getFirst().getItem().getOwner().getId(), owner.getId());
        assertEquals(bookings.getLast().getItem().getOwner().getId(), owner.getId());
        assertTrue(bookings.getFirst().getStart().isAfter(bookings.getLast().getStart()));
    }

    @Test
    void findCurrentBookingsByItemOwnerId() {
        User user1 = new User();
        user1.setName("Name1");
        user1.setEmail("name1@mail.com");
        User owner = userRepository.save(user1);

        User user2 = new User();
        user2.setName("Name2");
        user2.setEmail("name2@mail.com");
        User booker = userRepository.save(user2);

        Item item = new Item();
        item.setName("Item");
        item.setDescription("Very good item");
        item.setAvailable(true);
        item.setOwner(owner);
        Item savedItem = itemRepository.save(item);

        Item item2 = new Item();
        item2.setName("Item2");
        item2.setDescription("Very good item");
        item2.setAvailable(true);
        item2.setOwner(owner);
        Item savedItem2 = itemRepository.save(item2);

        Booking booking = new Booking();
        booking.setStart(LocalDateTime.of(2026, 3, 15, 18, 0));
        booking.setEnd(LocalDateTime.of(2026, 4, 15, 18, 0));
        booking.setItem(savedItem);
        booking.setBooker(booker);
        booking.setStatus(BookingStatus.APPROVED);
        bookingRepository.save(booking);

        Booking booking2 = new Booking();
        booking2.setStart(LocalDateTime.of(2026, 3, 18, 18, 0));
        booking2.setEnd(LocalDateTime.of(2026, 4, 18, 18, 0));
        booking2.setItem(savedItem2);
        booking2.setBooker(booker);
        booking2.setStatus(BookingStatus.APPROVED);
        bookingRepository.save(booking2);

        List<Booking> bookings = bookingRepository
                .findCurrentBookingsByItemOwnerId(owner.getId(), LocalDateTime.now());

        assertEquals(2, bookings.size());
        assertEquals(bookings.getFirst().getItem().getOwner().getId(), owner.getId());
        assertEquals(bookings.getLast().getItem().getOwner().getId(), owner.getId());
        assertTrue(bookings.getFirst().getStart().isAfter(bookings.getLast().getStart()));
        assertTrue(bookings.getFirst().getStart().isBefore(LocalDateTime.now()));
        assertTrue(bookings.getFirst().getEnd().isAfter(LocalDateTime.now()));
        assertTrue(bookings.getLast().getStart().isBefore(LocalDateTime.now()));
        assertTrue(bookings.getLast().getEnd().isAfter(LocalDateTime.now()));
    }

    @Test
    void findByItemOwnerIdAndEndBeforeOrderByStartDesc() {
        User user1 = new User();
        user1.setName("Наташа");
        user1.setEmail("Romanova@Widow.com");
        User owner = userRepository.save(user1);

        User user2 = new User();
        user2.setName("Клинт");
        user2.setEmail("Barton@Hawkeye.com");
        User booker = userRepository.save(user2);

        Item item = new Item();
        item.setName("Камень разума");
        item.setDescription("Один из камней бесконечности");
        item.setAvailable(true);
        item.setOwner(owner);
        Item sacrifice = itemRepository.save(item);

        Booking booking = new Booking();
        booking.setStart(LocalDateTime.of(2019, 4, 29, 1, 53, 52));
        booking.setEnd(LocalDateTime.of(2019, 4, 29, 1, 53, 59));
        booking.setItem(sacrifice);
        booking.setBooker(booker);
        booking.setStatus(BookingStatus.APPROVED);
        bookingRepository.save(booking);

        Booking secondBooking = new Booking();
        secondBooking.setStart(LocalDateTime.of(2020, 4, 29, 1, 53, 52));
        secondBooking.setEnd(LocalDateTime.of(2020, 4, 29, 1, 53, 59));
        secondBooking.setItem(sacrifice);
        secondBooking.setBooker(booker);
        secondBooking.setStatus(BookingStatus.APPROVED);
        bookingRepository.save(secondBooking);

        List<Booking> bookings = bookingRepository
                .findByItemOwnerIdAndEndBeforeOrderByStartDesc(owner.getId(), LocalDateTime.now());

        assertTrue(bookings.getFirst().getEnd().isBefore(LocalDateTime.now()));
        assertTrue(bookings.getFirst().getStart().isAfter(bookings.getLast().getStart()));
        assertEquals(bookings.getFirst().getItem().getOwner().getId(), owner.getId());
        assertEquals(bookings.getLast().getItem().getOwner().getId(), owner.getId());
        assertEquals(2, bookings.size());
    }

    @Test
    void findByItemOwnerIdAndStartAfterOrderByStartDesc() {
        User user1 = new User();
        user1.setName("Name1");
        user1.setEmail("name1@mail.com");
        User owner = userRepository.save(user1);

        User user2 = new User();
        user2.setName("Name2");
        user2.setEmail("name2@mail.com");
        User booker = userRepository.save(user2);

        Item item = new Item();
        item.setName("Item");
        item.setDescription("Very good item");
        item.setAvailable(true);
        item.setOwner(owner);
        Item savedItem = itemRepository.save(item);

        Booking booking = new Booking();
        booking.setStart(LocalDateTime.of(2030, 4, 29, 1, 0));
        booking.setEnd(LocalDateTime.of(2030, 4, 29, 4, 0));
        booking.setItem(savedItem);
        booking.setBooker(booker);
        booking.setStatus(BookingStatus.APPROVED);
        bookingRepository.save(booking);

        Booking booking2 = new Booking();
        booking2.setStart(LocalDateTime.of(2030, 4, 30, 1, 0));
        booking2.setEnd(LocalDateTime.of(2030, 4, 30, 4, 0));
        booking2.setItem(savedItem);
        booking2.setBooker(booker);
        booking2.setStatus(BookingStatus.APPROVED);
        bookingRepository.save(booking2);

        List<Booking> bookings = bookingRepository
                .findByItemOwnerIdAndStartAfterOrderByStartDesc(owner.getId(), LocalDateTime.now());

        assertTrue(bookings.getFirst().getStart().isAfter(LocalDateTime.now()));
        assertTrue(bookings.getFirst().getStart().isAfter(bookings.getLast().getStart()));
        assertEquals(bookings.getFirst().getItem().getOwner().getId(), owner.getId());
        assertEquals(bookings.getLast().getItem().getOwner().getId(), owner.getId());
        assertEquals(2, bookings.size());
    }

    @Test
    void findByItemOwnerIdAndStatusOrderByStartDesc() {
        User user1 = new User();
        user1.setName("Name1");
        user1.setEmail("name1@mail.com");
        User owner = userRepository.save(user1);

        User user2 = new User();
        user2.setName("Name2");
        user2.setEmail("name2@mail.com");
        User booker = userRepository.save(user2);

        Item item = new Item();
        item.setName("Item");
        item.setDescription("Very good item");
        item.setAvailable(true);
        item.setOwner(owner);
        Item savedItem = itemRepository.save(item);

        Booking booking = new Booking();
        booking.setStart(LocalDateTime.of(2026, 4, 29, 1, 0));
        booking.setEnd(LocalDateTime.of(2026, 4, 29, 4, 0));
        booking.setItem(savedItem);
        booking.setBooker(booker);
        booking.setStatus(BookingStatus.APPROVED);
        bookingRepository.save(booking);

        Booking booking2 = new Booking();
        booking2.setStart(LocalDateTime.of(2026, 4, 30, 1, 0));
        booking2.setEnd(LocalDateTime.of(2026, 4, 30, 4, 0));
        booking2.setItem(savedItem);
        booking2.setBooker(booker);
        booking2.setStatus(BookingStatus.APPROVED);
        bookingRepository.save(booking2);

        List<Booking> bookings = bookingRepository
                .findByItemOwnerIdAndStatusOrderByStartDesc(owner.getId(), BookingStatus.APPROVED);

        assertEquals(2, bookings.size());
        assertEquals(BookingStatus.APPROVED, bookings.getFirst().getStatus());
        assertEquals(BookingStatus.APPROVED, bookings.getLast().getStatus());
        assertTrue(bookings.getFirst().getStart().isAfter(bookings.getLast().getStart()));
        assertEquals(bookings.getFirst().getItem().getOwner().getId(), owner.getId());
        assertEquals(bookings.getLast().getItem().getOwner().getId(), owner.getId());
    }

    @Test
    void findByItemIdAndStartBeforeAndStatusOrderByStartDesc() {
        User user1 = new User();
        user1.setName("Name1");
        user1.setEmail("name1@mail.com");
        User owner = userRepository.save(user1);

        User user2 = new User();
        user2.setName("Name2");
        user2.setEmail("name2@mail.com");
        User booker = userRepository.save(user2);

        Item item = new Item();
        item.setName("Item");
        item.setDescription("Very good item");
        item.setAvailable(true);
        item.setOwner(owner);
        Item savedItem = itemRepository.save(item);

        Booking booking = new Booking();
        booking.setStart(LocalDateTime.of(2026, 1, 29, 1, 0));
        booking.setEnd(LocalDateTime.of(2026, 1, 29, 4, 0));
        booking.setItem(savedItem);
        booking.setBooker(booker);
        booking.setStatus(BookingStatus.APPROVED);
        bookingRepository.save(booking);

        Booking booking2 = new Booking();
        booking2.setStart(LocalDateTime.of(2026, 4, 30, 1, 0));
        booking2.setEnd(LocalDateTime.of(2026, 4, 30, 4, 0));
        booking2.setItem(savedItem);
        booking2.setBooker(booker);
        booking2.setStatus(BookingStatus.APPROVED);
        bookingRepository.save(booking2);

        List<Booking> bookings = bookingRepository
                .findByItemIdAndStartBeforeAndStatusOrderByStartDesc(
                        savedItem.getId(),
                        LocalDateTime.now(),
                        BookingStatus.APPROVED);

        assertEquals(1, bookings.size());
        assertEquals(BookingStatus.APPROVED, bookings.getFirst().getStatus());
        assertEquals(savedItem.getId(), bookings.getFirst().getItem().getId());
        assertTrue(bookings.getFirst().getStart().isBefore(LocalDateTime.now()));
    }

    @Test
    void findByItemIdAndStartAfterAndStatusOrderByStartAsc() {
        User user1 = new User();
        user1.setName("Name1");
        user1.setEmail("name1@mail.com");
        User owner = userRepository.save(user1);

        User user2 = new User();
        user2.setName("Name2");
        user2.setEmail("name2@mail.com");
        User booker = userRepository.save(user2);

        Item item = new Item();
        item.setName("Item");
        item.setDescription("Very good item");
        item.setAvailable(true);
        item.setOwner(owner);
        Item savedItem = itemRepository.save(item);

        Booking booking = new Booking();
        booking.setStart(LocalDateTime.of(2026, 1, 29, 1, 0));
        booking.setEnd(LocalDateTime.of(2026, 1, 29, 4, 0));
        booking.setItem(savedItem);
        booking.setBooker(booker);
        booking.setStatus(BookingStatus.APPROVED);
        bookingRepository.save(booking);

        Booking booking2 = new Booking();
        booking2.setStart(LocalDateTime.of(2026, 4, 30, 1, 0));
        booking2.setEnd(LocalDateTime.of(2026, 4, 30, 4, 0));
        booking2.setItem(savedItem);
        booking2.setBooker(booker);
        booking2.setStatus(BookingStatus.APPROVED);
        bookingRepository.save(booking2);

        List<Booking> bookings = bookingRepository
                .findByItemIdAndStartAfterAndStatusOrderByStartAsc(
                        savedItem.getId(),
                        LocalDateTime.now(),
                        BookingStatus.APPROVED);

        assertEquals(1, bookings.size());
        assertEquals(BookingStatus.APPROVED, bookings.getFirst().getStatus());
        assertEquals(savedItem.getId(), bookings.getFirst().getItem().getId());
        assertTrue(bookings.getFirst().getStart().isAfter(LocalDateTime.now()));
    }

    @Test
    void existsByItemIdAndBookerIdAndEndBefore() {
        User user1 = new User();
        user1.setName("Name1");
        user1.setEmail("name1@mail.com");
        User owner = userRepository.save(user1);

        User user2 = new User();
        user2.setName("Name2");
        user2.setEmail("name2@mail.com");
        User booker = userRepository.save(user2);

        Item item = new Item();
        item.setName("Item");
        item.setDescription("Very good item");
        item.setAvailable(true);
        item.setOwner(owner);
        Item savedItem = itemRepository.save(item);

        Booking booking = new Booking();
        booking.setStart(LocalDateTime.of(2026, 3, 17, 12, 0));
        booking.setEnd(LocalDateTime.of(2026, 3, 18, 12, 0));
        booking.setItem(savedItem);
        booking.setBooker(booker);
        booking.setStatus(BookingStatus.WAITING);
        bookingRepository.save(booking);

        assertTrue(bookingRepository
                .existsByItemIdAndBookerIdAndEndBefore(savedItem.getId(), booker.getId(), LocalDateTime.now()));
    }
}