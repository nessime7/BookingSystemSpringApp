package com.coindirect.recruitment.integration;

import com.coindirect.recruitment.BookingSystemApplication;
import com.coindirect.recruitment.model.Booking;
import com.coindirect.recruitment.repository.BookingRepository;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {BookingSystemApplication.class}, webEnvironment = RANDOM_PORT)
public class BookingIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private BookingRepository bookingRepository;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @AfterEach
    void tearDown() {
        bookingRepository.deleteAll();
    }

    @Test
    public void should_create_booking() {

        var body = "{\n" +
            "  \"name\": \"John's booking\",\n" +
            "  \"row\": 0,\n" +
            "  \"column\": 0\n" +
            "}";
        given()
            .when()
            .body(body)
            .contentType(JSON)
            .post("/create")
            .then()
            .statusCode(SC_OK)
            .and().body("bookingId", notNullValue())
            .and().body("name", equalTo("John's booking"))
            .and().body("row", equalTo(0))
            .and().body("column", equalTo(0));
    }

    @Test
    public void should_return_booking_by_booking_id() {
        var booking = bookingRepository.save(new Booking("Elton's booking", 1, 1));

        given()
            .when()
            .contentType(JSON)
            .get(String.format("/getByBookingId/%s", booking.getId()))
            .then()
            .statusCode(SC_OK)
            .and().body("bookingId", equalTo(booking.getId().toString()))
            .and().body("name", equalTo("Elton's booking"))
            .and().body("row", equalTo(1))
            .and().body("column", equalTo(1));
    }

    @Test
    public void should_return_booking_by_row_and_col() {
        var booking = bookingRepository.save(new Booking("Elton's booking", 1, 1));

        given()
            .when()
            .contentType(JSON)
            .get("/getByPosition/1/1")
            .then()
            .statusCode(SC_OK)
            .and().body("bookingId", equalTo(booking.getId().toString()))
            .and().body("name", equalTo("Elton's booking"))
            .and().body("row", equalTo(1))
            .and().body("column", equalTo(1));
    }

    @Test
    public void should_return_false_when_booking_position_not_available() {
        bookingRepository.save(new Booking("Elton's booking", 1, 1));

        given()
            .when().get("isAvailable/1/1")
            .then()
            .statusCode(SC_OK)
            .and().body("available", equalTo(false));
    }
}