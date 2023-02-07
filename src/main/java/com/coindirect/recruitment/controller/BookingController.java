package com.coindirect.recruitment.controller;

import com.coindirect.recruitment.model.dto.BookingAvailabilityDto;
import com.coindirect.recruitment.model.dto.BookingDto;
import com.coindirect.recruitment.model.dto.CreateBookingRequestDto;
import com.coindirect.recruitment.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.UUID;

/**
 * Controller for handling bookings for a bing hall.
 * The hall can be imagined as a grid of rows and columns
 */
@RestController
public class BookingController {

    // referencja
    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    /**
     * Creates a booking with the requested details.
     * If unavailable returns a 200 with error message.
     *
     * @param createBookingRequestDto the requested booking details.
     * @return on success booking details. on failure error message.
     */
    // Walidacji używa się, aby mieć pewność, że dany obiekt wypełniony jest poprawnymi danymi.
    @PostMapping("create")
    public ResponseEntity<BookingDto> createBooking(@Valid @RequestBody CreateBookingRequestDto createBookingRequestDto) {
        final var savedBooking = bookingService.createBooking(createBookingRequestDto);
        final var response = new BookingDto(savedBooking.getId(), savedBooking.getPositionRow(), savedBooking.getPositionColumn(), savedBooking.getName());
        return ResponseEntity.ok(response);
    }

    /**
     * query a booking by grid position
     *
     * @param row    grid position row
     * @param column grid position column
     * @return the booking details. 400 if not found
     */
    @GetMapping("getByPosition/{row}/{column}")
    public ResponseEntity<BookingDto> getBookingByPosition(@PathVariable int row, @PathVariable int column) {
        final var booking = bookingService.getBookingByPosition(row, column);
        final var response = new BookingDto(booking.getId(), booking.getPositionRow(), booking.getPositionColumn(), booking.getName());
        return ResponseEntity.ok(response);
    }

    /**
     * query by booking id
     *
     * @param bookingId booking id
     * @return the booking details. 400 if not found
     */
    @GetMapping("getByBookingId/{bookingId}")
    public ResponseEntity<BookingDto> getBookingById(@PathVariable UUID bookingId) {
        final var booking = bookingService.getBookingById(bookingId);
        final var response = new BookingDto(booking.getId(), booking.getPositionRow(), booking.getPositionColumn(), booking.getName());
        return ResponseEntity.ok(response);
    }

    /**
     * Query if a cell is available
     *
     * @param row    grid position row
     * @param column grid position column
     * @return true if cell is available. false if not
     */
    @GetMapping("isAvailable/{row}/{column}")
    public ResponseEntity<BookingAvailabilityDto> isAvailable(@PathVariable int row, @PathVariable int column) {
        final var response = new BookingAvailabilityDto(bookingService.isAvailable(row, column));
        return ResponseEntity.ok(response);
    }
}
