package com.coindirect.recruitment.exception.model;

import com.coindirect.recruitment.exception.model.BookingNotFoundException;
import com.coindirect.recruitment.exception.model.PlaceAlreadyBookedException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Map;

import static java.lang.String.format;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@ControllerAdvice
public class ResponseExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({BookingNotFoundException.class, PlaceAlreadyBookedException.class, IllegalStateException.class})
    public ResponseEntity<Object> handleBookingNotFoundException(Exception ex, WebRequest request) {
        return new ResponseEntity<>(
            Map.of("message", ex.getMessage()), new HttpHeaders(), UNPROCESSABLE_ENTITY);
    }

    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {
        final var errors = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(fieldError -> format("field: %s, reason: %s", fieldError.getField(), fieldError.getDefaultMessage()))
            .collect(toList());
        return new ResponseEntity<>(
            Map.of("message", "Input problems: " + errors.stream().collect(joining(", "))), new HttpHeaders(), BAD_REQUEST);
    }
}