package com.coindirect.recruitment.model.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;

@AllArgsConstructor
@EqualsAndHashCode
public class CreateBookingRequestDto {

    @NotBlank
    public final String name;
    @PositiveOrZero
    public final int row;
    @PositiveOrZero
    public final int column;
}
