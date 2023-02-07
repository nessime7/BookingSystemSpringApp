package com.coindirect.recruitment.service;

import com.coindirect.recruitment.exception.model.BookingNotFoundException;
import com.coindirect.recruitment.exception.model.PlaceAlreadyBookedException;
import com.coindirect.recruitment.model.Booking;
import com.coindirect.recruitment.model.dto.CreateBookingRequestDto;
import com.coindirect.recruitment.repository.BookingRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

class BookingServiceTest {

    // deklaracja prywatnego finalnego pola obiektu bookingRepository typu BookingRepository
    // i przypisanie do niego mocka klasy BookingRepository
    private final BookingRepository bookingRepository = mock(BookingRepository.class);
    // stworzenie prywatnego, finalnego pola obiektu bookingService typu BookingService
    // z parametrami pola obiektu bookingRepository
    private final BookingService bookingService = new BookingService(bookingRepository);

    @Test
    void should_create_booking() {
        // given
        var bookingRequest = new CreateBookingRequestDto("John's booking", 1, 1);
        var booking = new Booking("John's booking", 1, 1);
        given(bookingRepository.save(any())).willReturn(booking);
        given(bookingRepository.findByPositionRowAndPositionColumn(1, 1)).willReturn(empty());

        // when
        var bookingDto = bookingService.createBooking(bookingRequest);

        // then
        assertThat(bookingDto.getId()).isEqualTo(booking.getId());
        assertThat(bookingDto.getName()).isEqualTo(booking.getName());
        assertThat(bookingDto.getPositionRow()).isEqualTo(booking.getPositionRow());
        assertThat(bookingDto.getPositionColumn()).isEqualTo(booking.getPositionColumn());
    }

    @Test
    void should_not_create_booking_and_throw_when_booking_request_has_incorrect_row() {
        // given
        var bookingRequest = new CreateBookingRequestDto("John's booking", 1000, 1);

        // then
        assertThrows(IllegalArgumentException.class, () -> bookingService.createBooking(bookingRequest));
        then(bookingRepository).shouldHaveNoInteractions();
    }

    @Test
    void should_not_create_booking_and_throw_when_booking_request_has_incorrect_col() {
        // given
        var bookingRequest = new CreateBookingRequestDto("John's booking", 1, 1000);

        // then
        assertThrows(IllegalArgumentException.class, () -> bookingService.createBooking(bookingRequest));
        then(bookingRepository).shouldHaveNoInteractions();

    }

    @Test
    void should_not_create_booking_and_throw_when_booking_request_when_place_already_booked() {
        // given
        var bookingRequest = new CreateBookingRequestDto("John's booking", 1, 1);
        var booking = new Booking("John's booking", 1, 1);
        given(bookingRepository.findByPositionRowAndPositionColumn(1, 1)).willReturn(Optional.of(booking));

        // then
        assertThrows(PlaceAlreadyBookedException.class, () -> bookingService.createBooking(bookingRequest));
        then(bookingRepository).should().findByPositionRowAndPositionColumn(1, 1);
        then(bookingRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    void should_return_booking_by_position() {
        // given
        var row = 0;
        var column = 0;
        given(bookingRepository.findByPositionRowAndPositionColumn(row, column)).willReturn(Optional.of(new Booking("John's booking", row, column)));

        // when
        var booking = bookingService.getBookingByPosition(row, column);

        // then
        assertEquals(0, booking.getPositionRow());
        assertEquals(0, booking.getPositionColumn());
    }

    @Test
    void should_not_return_booking_by_position_and_throw_when_booking_does_not_exist() {
        // given
        var row = 0;
        var column = 0;
        given(bookingRepository.findByPositionRowAndPositionColumn(row, column)).willReturn(empty());

        // then
        assertThrows(BookingNotFoundException.class, () -> bookingService.getBookingByPosition(row, column));
    }

    @Test
    void should_return_booking_by_id() {
        // given
        var id = randomUUID();
        given(bookingRepository.findById(id)).willReturn(of(new Booking(id, "John's booking", 0, 0)));

        // when
        var booking = bookingService.getBookingById(id);

        // then
        assertThat(booking.getPositionRow()).isEqualTo(0);
        assertThat(booking.getPositionColumn()).isEqualTo(0);
    }

    @Test
    void should_not_return_booking_by_id_and_throw_when_booking_does_not_exist() {
        // given
        var id = randomUUID();
        given(bookingRepository.findById(id)).willReturn(empty());

        // then
        assertThrows(BookingNotFoundException.class, () -> bookingService.getBookingById(id));
    }

    @Test
    void should_return_true_availability_when_position_available() {
        // given
        var row = 0;
        var column = 0;
        given(bookingRepository.findByPositionRowAndPositionColumn(row, column)).willReturn(empty());

        // when
        var isAvailable = bookingService.isAvailable(row, column);

        // then
        assertTrue(isAvailable);
    }

    @Test
    void should_return_false_availability_when_position_not_available() {
        // given
        var row = 0;
        var column = 0;
        given(bookingRepository.findByPositionRowAndPositionColumn(row, column)).willReturn(Optional.of(new Booking("John's booking", row, column)));

        // when
        var isAvailable = bookingService.isAvailable(row, column);

        // then
        assertFalse(isAvailable);
    }
}