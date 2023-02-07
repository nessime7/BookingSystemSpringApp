package com.coindirect.recruitment.exception.model;

public class PlaceAlreadyBookedException extends IllegalStateException {

    public PlaceAlreadyBookedException() {
        super("Place already booked");
    }
}
