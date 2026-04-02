package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class BookingStateTest {

    @Test
    void from_shouldReturnAll_whenStringIsAll() {
        Optional<BookingState> result = BookingState.from("ALL");

        assertTrue(result.isPresent());
        assertEquals(BookingState.ALL, result.get());
    }

    @Test
    void from_shouldReturnAll_whenStringIsAllLowercase() {
        Optional<BookingState> result = BookingState.from("all");

        assertTrue(result.isPresent());
        assertEquals(BookingState.ALL, result.get());
    }

    @Test
    void from_shouldReturnAll_whenStringIsAllMixedCase() {
        Optional<BookingState> result = BookingState.from("AlL");

        assertTrue(result.isPresent());
        assertEquals(BookingState.ALL, result.get());
    }

    @Test
    void from_shouldReturnCurrent_whenStringIsCurrent() {
        Optional<BookingState> result = BookingState.from("CURRENT");

        assertTrue(result.isPresent());
        assertEquals(BookingState.CURRENT, result.get());
    }

    @Test
    void from_shouldReturnCurrent_whenStringIsCurrentLowercase() {
        Optional<BookingState> result = BookingState.from("current");

        assertTrue(result.isPresent());
        assertEquals(BookingState.CURRENT, result.get());
    }

    @Test
    void from_shouldReturnPast_whenStringIsPast() {
        Optional<BookingState> result = BookingState.from("PAST");

        assertTrue(result.isPresent());
        assertEquals(BookingState.PAST, result.get());
    }

    @Test
    void from_shouldReturnPast_whenStringIsPastLowercase() {
        Optional<BookingState> result = BookingState.from("past");

        assertTrue(result.isPresent());
        assertEquals(BookingState.PAST, result.get());
    }

    @Test
    void from_shouldReturnFuture_whenStringIsFuture() {
        Optional<BookingState> result = BookingState.from("FUTURE");

        assertTrue(result.isPresent());
        assertEquals(BookingState.FUTURE, result.get());
    }

    @Test
    void from_shouldReturnFuture_whenStringIsFutureLowercase() {
        Optional<BookingState> result = BookingState.from("future");

        assertTrue(result.isPresent());
        assertEquals(BookingState.FUTURE, result.get());
    }

    @Test
    void from_shouldReturnWaiting_whenStringIsWaiting() {
        Optional<BookingState> result = BookingState.from("WAITING");

        assertTrue(result.isPresent());
        assertEquals(BookingState.WAITING, result.get());
    }

    @Test
    void from_shouldReturnWaiting_whenStringIsWaitingLowercase() {
        Optional<BookingState> result = BookingState.from("waiting");

        assertTrue(result.isPresent());
        assertEquals(BookingState.WAITING, result.get());
    }

    @Test
    void from_shouldReturnRejected_whenStringIsRejected() {
        Optional<BookingState> result = BookingState.from("REJECTED");

        assertTrue(result.isPresent());
        assertEquals(BookingState.REJECTED, result.get());
    }

    @Test
    void from_shouldReturnRejected_whenStringIsRejectedLowercase() {
        Optional<BookingState> result = BookingState.from("rejected");

        assertTrue(result.isPresent());
        assertEquals(BookingState.REJECTED, result.get());
    }

    @Test
    void from_shouldReturnEmpty_whenStringIsInvalid() {
        Optional<BookingState> result = BookingState.from("INVALID");

        assertFalse(result.isPresent());
    }

    @Test
    void from_shouldReturnEmpty_whenStringIsNull() {
        Optional<BookingState> result = BookingState.from(null);

        assertFalse(result.isPresent());
    }

    @Test
    void from_shouldReturnEmpty_whenStringIsEmpty() {
        Optional<BookingState> result = BookingState.from("");

        assertFalse(result.isPresent());
    }
}