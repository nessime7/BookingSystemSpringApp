package com.coindirect.recruitment.controller;

import com.coindirect.recruitment.exception.model.BookingNotFoundException;
import com.coindirect.recruitment.model.Booking;
import com.coindirect.recruitment.model.dto.CreateBookingRequestDto;
import com.coindirect.recruitment.service.BookingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static java.lang.String.format;
import static java.util.UUID.randomUUID;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingService bookingService;

    @Test
    void should_create_booking_when_valid_request() throws Exception {
        // given
        var request = new CreateBookingRequestDto("Dayton's booking", 0, 0);
        var booking = new Booking(randomUUID(), "Dayton's booking", 0, 0);
        given(bookingService.createBooking(request)).willReturn(booking);

        // then
        this.mockMvc.perform(post("/create")
            .content(new ObjectMapper().writeValueAsString(request))
            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().string(format("{\"bookingId\":\"%s\",\"row\":0,\"column\":0,\"name\":\"Dayton's booking\"}", booking.getId())));
    }

    @Test
    void should_not_create_booking_when_booking_not_found_exception() throws Exception {
        // given
        var request = new CreateBookingRequestDto("Dayton's booking", 0, 0);
        var booking = new Booking(randomUUID(), "Dayton's booking", 0, 0);
        given(bookingService.createBooking(request)).willThrow(new BookingNotFoundException(request.row, request.column));

        // then
        this.mockMvc.perform(post("/create")
            .content(new ObjectMapper().writeValueAsString(request))
            .contentType(APPLICATION_JSON))
            .andExpect(status().isUnprocessableEntity())
            .andExpect(content().string("{\"message\":\"Booking not found for position 0, 0\"}"));
    }

    @Test
    void should_not_create_booking_when_incorrect_request() throws Exception {
        // given
        var request = new CreateBookingRequestDto("Dayton's booking", -100, 0);
        var booking = new Booking(randomUUID(), "Dayton's booking", 0, 0);
        given(bookingService.createBooking(request)).willReturn(booking);

        // then
        this.mockMvc.perform(post("/create")
            .content(new ObjectMapper().writeValueAsString(request))
            .contentType(APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(content().string(format("{\"message\":\"Input problems: field: row, reason: must be greater than or equal to 0\"}", booking.getId())));
    }

    @Test
    void should_return_booking_by_booking_id() throws Exception {
        // given
        var booking = new Booking(randomUUID(), "Dayton's booking", 0, 0);
        given(bookingService.getBookingById(booking.getId())).willReturn(booking);

        // then
        this.mockMvc.perform(get(format("/getByBookingId/%s", booking.getId())))
            .andExpect(status().isOk())
            .andExpect(content().string(format("{\"bookingId\":\"%s\",\"row\":0,\"column\":0,\"name\":\"Dayton's booking\"}", booking.getId().toString())));
    }

    @Test
    void should_return_booking_by_row_and_column() throws Exception {
        // given
        var booking = new Booking(randomUUID(), "Dayton's booking", 0, 0);
        given(bookingService.getBookingByPosition(booking.getPositionRow(), booking.getPositionColumn())).willReturn(booking);

        // then
        this.mockMvc.perform(get(format("/getByPosition/%s/%s", booking.getPositionRow(), booking.getPositionColumn())))
            .andExpect(status().isOk())
            .andExpect(content().string(format("{\"bookingId\":\"%s\",\"row\":0,\"column\":0,\"name\":\"Dayton's booking\"}", booking.getId().toString())));
    }

    @Test
    void should_return_availability_by_row_and_column() throws Exception {
        // given
        var booking = new Booking(randomUUID(), "Dayton's booking", 0, 0);
        given(bookingService.getBookingById(booking.getId())).willReturn(booking);

        // then
        this.mockMvc.perform(get(format("/isAvailable/%s/%s", booking.getPositionRow(), booking.getPositionColumn())))
            .andExpect(status().isOk())
            .andExpect(content().string("{\"available\":false}"));
    }
}