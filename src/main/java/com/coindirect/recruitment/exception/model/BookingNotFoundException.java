package com.coindirect.recruitment.exception.model;

import java.util.UUID;

import static java.lang.String.format;

public class BookingNotFoundException extends RuntimeException {

    public BookingNotFoundException(int row, int column) {
        super(format("Booking not found for position %s, %s", row, column));
    }

    public BookingNotFoundException(UUID id) {
        super(format("Booking not found for id %s", id));
    }
}
