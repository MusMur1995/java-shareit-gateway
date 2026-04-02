package ru.practicum.shareit.booking.service;

import jakarta.persistence.EntityManager;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest(
        properties = "spring.datasource.url=jdbc:h2:mem:shareit",
        webEnvironment = SpringBootTest.WebEnvironment.NONE
)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BookingServiceImplTest {
    private final BookingService bookingService;
    private final UserService userService;
    private final ItemService itemService;
    private final EntityManager entityManager;


    @Test
    void create() {
        UserDto owner = userService.saveUser(UserDto.builder()
                .name("Scott")
                .email("Summers@x-men.com")
                .build());

        UserDto booker = userService.saveUser(UserDto.builder()
                .name("Henry")
                .email("McCoy@x-men.com")
                .build());

        ItemDto item = itemService.createItem(owner.getId(), ItemDto.builder()
                .name("Item")
                .description("Desc")
                .available(true)
                .build());

        BookingCreateDto dto = BookingCreateDto.builder()
                .start(LocalDateTime.of(2026, 3, 4, 15, 0))
                .end(LocalDateTime.of(2026, 3, 4, 15, 30))
                .itemId(item.getId())
                .build();

        BookingDto result = bookingService.create(booker.getId(), dto);

        assertEquals(item.getId(), result.getItem().getId());
    }

    @Test
    void create_ownerBooking_shouldThrow() {
        UserDto owner = userService.saveUser(UserDto.builder()
                .name("Owner")
                .email("owner@mail.com")
                .build());

        ItemDto item = itemService.createItem(owner.getId(), ItemDto.builder()
                .name("Item")
                .description("Desc")
                .available(true)
                .build());

        BookingCreateDto dto = BookingCreateDto.builder()
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .itemId(item.getId())
                .build();

        assertThrows(ValidationException.class,
                () -> bookingService.create(owner.getId(), dto));
    }

    @Test
    void create_invalidDates_shouldThrow() {
        UserDto owner = userService.saveUser(UserDto.builder()
                .name("Owner")
                .email("owner@mail.com")
                .build());

        UserDto booker = userService.saveUser(UserDto.builder()
                .name("Booker")
                .email("booker@mail.com")
                .build());

        ItemDto item = itemService.createItem(owner.getId(), ItemDto.builder()
                .name("Item")
                .description("Desc")
                .available(true)
                .build());

        BookingCreateDto dto = BookingCreateDto.builder()
                .start(LocalDateTime.now().plusDays(2))
                .end(LocalDateTime.now().plusDays(1))
                .itemId(item.getId())
                .build();

        assertThrows(ValidationException.class,
                () -> bookingService.create(booker.getId(), dto));
    }

    @Test
    void confirmBooking_approved() {
        UserDto owner = userService.saveUser(UserDto.builder()
                .name("Bruce")
                .email("Wayne@gotham.com")
                .build());

        UserDto booker = userService.saveUser(UserDto.builder()
                .name("Selina")
                .email("Kyle@gotham.com")
                .build());

        ItemDto item = itemService.createItem(owner.getId(), ItemDto.builder()
                .name("Бэтмобиль")
                .description("Много слов")
                .available(true)
                .build());

        BookingDto booking = bookingService.create(booker.getId(), BookingCreateDto.builder()
                .start(LocalDateTime.of(2026, 3, 6, 18, 0))
                .end(LocalDateTime.of(2026, 3, 7, 10, 30))
                .itemId(item.getId())
                .build());

        BookingDto result = bookingService.approveBooking(booking.getId(), owner.getId(), true);

        assertEquals(BookingStatus.APPROVED, result.getStatus());
    }

    @Test
    void confirmBooking_notOwner_shouldThrow() {
        UserDto owner = userService.saveUser(UserDto.builder()
                .name("Bruce")
                .email("Wayne@gotham.com")
                .build());

        UserDto booker = userService.saveUser(UserDto.builder()
                .name("Selina")
                .email("Kyle@gotham.com")
                .build());

        ItemDto item = itemService.createItem(owner.getId(), ItemDto.builder()
                .name("Бэтмобиль")
                .description("Много слов")
                .available(true)
                .build());

        BookingDto booking = bookingService.create(booker.getId(), BookingCreateDto.builder()
                .start(LocalDateTime.of(2026, 3, 6, 18, 0))
                .end(LocalDateTime.of(2026, 3, 7, 10, 30))
                .itemId(item.getId())
                .build());

        assertThrows(AccessDeniedException.class,
                () -> bookingService.approveBooking(booking.getId(), booker.getId(), true));
    }

    @Test
    void getAboutBooking() {
        UserDto owner = userService.saveUser(UserDto.builder()
                .name("Clark")
                .email("Kent@Smallville.com")
                .build());

        UserDto booker = userService.saveUser(UserDto.builder()
                .name("Lois")
                .email("Lane-Kent@Smallville.com")
                .build());

        ItemDto item = itemService.createItem(owner.getId(), ItemDto.builder()
                .name("Вещь")
                .description("Описание")
                .available(true)
                .build());

        BookingDto booking = bookingService.create(booker.getId(), BookingCreateDto.builder()
                .start(LocalDateTime.of(2026, 3, 6, 18, 0))
                .end(LocalDateTime.of(2026, 3, 7, 10, 30))
                .itemId(item.getId())
                .build());

        BookingDto result = bookingService.getAboutBooking(booker.getId(), booking.getId());

        assertEquals(booking.getId(), result.getId());
    }

    @Test
    void getAboutBooking_noAccess_shouldThrow() {
        UserDto owner = userService.saveUser(UserDto.builder()
                .name("Owner")
                .email("owner@mail.com")
                .build());

        UserDto booker = userService.saveUser(UserDto.builder()
                .name("Booker")
                .email("booker@mail.com")
                .build());

        UserDto stranger = userService.saveUser(UserDto.builder()
                .name("Stranger")
                .email("stranger@mail.com")
                .build());

        ItemDto item = itemService.createItem(owner.getId(), ItemDto.builder()
                .name("Item")
                .description("Desc")
                .available(true)
                .build());

        BookingDto booking = bookingService.create(booker.getId(), BookingCreateDto.builder()
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .itemId(item.getId())
                .build());

        assertThrows(AccessDeniedException.class,
                () -> bookingService.getAboutBooking(stranger.getId(), booking.getId()));
    }

    @Test
    void findBookingsByUserId() {
        UserDto firstUserDto = UserDto.builder()
                .name("J. Jonah")
                .email("Jameson@daily-bugle.com")
                .build();

        UserDto secondUserDto = UserDto.builder()
                .name("Collector")
                .email("Tivan@cygnus-x-1.com")
                .build();

        UserDto ownerUserDto = userService.saveUser(firstUserDto);
        UserDto bookerUserDto = userService.saveUser(secondUserDto);

        ItemDto firstItemDto = ItemDto.builder()
                .name("Костюм человека-паука")
                .description("Старый костюм размера M")
                .available(true)
                .build();

        ItemDto secondItemDto = ItemDto.builder()
                .name("Фото человека-паука")
                .description("Лучшие фотографии, сделанные Питером Паркером")
                .available(true)
                .build();

        ItemDto firstSavedItem = itemService.createItem(ownerUserDto.getId(), firstItemDto);
        ItemDto secondSavedItem = itemService.createItem(ownerUserDto.getId(), secondItemDto);

        BookingCreateDto bookingCreateDto1 = BookingCreateDto.builder()
                .start(LocalDateTime.of(2026, 2, 28, 15, 0))
                .end(LocalDateTime.of(2026, 3, 2, 15, 0))
                .itemId(firstSavedItem.getId())
                .build();

        BookingCreateDto bookingCreateDto2 = BookingCreateDto.builder()
                .start(LocalDateTime.of(2026, 3, 4, 15, 0))
                .end(LocalDateTime.of(2026, 3, 6, 15, 0))
                .itemId(secondSavedItem.getId())
                .build();

        BookingDto bookingDto1 = bookingService.create(bookerUserDto.getId(), bookingCreateDto1);
        BookingDto bookingDto2 = bookingService.create(bookerUserDto.getId(), bookingCreateDto2);

        List<BookingDto> pastBookings = bookingService.findBookingsByUserId(bookerUserDto.getId(), BookingState.PAST);

        Booking booking1 = entityManager.createQuery(
                        "SELECT b FROM Booking b WHERE b.id = :bookingId", Booking.class)
                .setParameter("bookingId", bookingDto1.getId())
                .getSingleResult();

        Booking booking2 = entityManager.createQuery(
                        "SELECT b FROM Booking b WHERE b.id = :bookingId", Booking.class)
                .setParameter("bookingId", bookingDto2.getId())
                .getSingleResult();


        assertEquals(booking2.getId(), pastBookings.getFirst().getId());
        assertEquals(booking1.getId(), pastBookings.getLast().getId());
    }

    @Test
    void findBookingsByItemOwnerId_all() {
        UserDto owner = userService.saveUser(UserDto.builder()
                .name("Clark")
                .email("Kent@Smallville.com")
                .build());

        UserDto booker = userService.saveUser(UserDto.builder()
                .name("Lois")
                .email("Lane-Kent@Smallville.com")
                .build());

        ItemDto item = itemService.createItem(owner.getId(), ItemDto.builder()
                .name("Вещь")
                .description("Описание")
                .available(true)
                .build());

        bookingService.create(booker.getId(), BookingCreateDto.builder()
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .itemId(item.getId())
                .build());

        List<BookingDto> result =
                bookingService.findBookingsByItemOwnerId(owner.getId(), BookingState.ALL);

        assertEquals(1, result.size());
    }
}
