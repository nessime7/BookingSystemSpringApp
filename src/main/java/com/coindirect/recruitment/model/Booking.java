package com.coindirect.recruitment.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.util.UUID;

// Obiekt typu Entity (encja) jest lekkim obiektem należącym do obiektowego modelu danych.
// Zazwyczaj obiekt typu Entity reprezentuje tabelę w relacyjnej bazie danych, a każde wystąpienie tego obiektu odpowiada wierszowi w tej tabeli
@Entity
// Adnotacja @UniqueConstraint służy do opisywania wielu unikalnych kluczy na poziomie tabeli
@Table(name = "bookings", uniqueConstraints =
@UniqueConstraint(columnNames = {"position_row", "position_column"}))
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Booking {

    @Id
    // identyfikator ten jest generowany automatycznie w momencie zapisu do bazy danych
    @GeneratedValue(generator = "UUID")
    private UUID id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "position_row", nullable = false)
    private int positionRow;
    @Column(name = "position_column", nullable = false)
    private int positionColumn;

    public Booking(String name, int positionRow, int positionColumn) {
        this.name = name;
        this.positionRow = positionRow;
        this.positionColumn = positionColumn;
    }
}