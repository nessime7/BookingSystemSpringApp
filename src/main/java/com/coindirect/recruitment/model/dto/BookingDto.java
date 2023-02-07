package com.coindirect.recruitment.model.dto;

import lombok.AllArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
public class BookingDto {

    // struktura danych używana do przekazywania danych między serwerami
    public final UUID bookingId;
    public final int row;
    public final int column;
    public final String name;
}
