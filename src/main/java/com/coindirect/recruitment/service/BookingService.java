package com.coindirect.recruitment.service;

import com.coindirect.recruitment.exception.model.BookingNotFoundException;
import com.coindirect.recruitment.exception.model.PlaceAlreadyBookedException;
import com.coindirect.recruitment.model.Booking;
import com.coindirect.recruitment.model.dto.CreateBookingRequestDto;
import com.coindirect.recruitment.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.springframework.transaction.annotation.Isolation.SERIALIZABLE;

@Service
public class BookingService {

    private static final int MAX_ROW = 1000;
    private static final int MAX_COL = 1000;

    private final BookingRepository bookingRepository;

    @Autowired
    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public Booking createBooking(CreateBookingRequestDto request) {
        validateRequest(request);
        return saveBooking(request);
    }

    // Operacje modyfikacji wykonały się w ściśle określonej kolejności, gdyż bieżące operacje mogą bazować na wynikach poprzedników.
    // Zmiany zostały zaaplikowane albo wszystkie, albo wcale.
    @Transactional(isolation = SERIALIZABLE)
    private Booking saveBooking(CreateBookingRequestDto request) {
        if (bookingRepository.findByPositionRowAndPositionColumn(request.row, request.column).isPresent()) {
            throw new PlaceAlreadyBookedException();
        }
        final var booking = new Booking(request.name, request.row, request.column);
        return bookingRepository.save(booking);
    }

    public Booking getBookingByPosition(int row, int column) {
        return bookingRepository.findByPositionRowAndPositionColumn(row, column)
            .orElseThrow(() -> new BookingNotFoundException(row, column));
    }

    public Booking getBookingById(UUID bookingId) {
        return bookingRepository.findById(bookingId)
            .orElseThrow(() -> new BookingNotFoundException(bookingId));
    }

    public boolean isAvailable(int row, int column) {
        return bookingRepository.findByPositionRowAndPositionColumn(row, column).isEmpty();
    }

    private void validateRequest(CreateBookingRequestDto request) {
        if (request.column >= MAX_COL) {
            throw new IllegalArgumentException();
        }
        if (request.row >= MAX_ROW) {
            throw new IllegalArgumentException();
        }
    }
}
