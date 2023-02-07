package com.coindirect.recruitment.repository;

import com.coindirect.recruitment.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BookingRepository extends JpaRepository<Booking, UUID> {

    // Optional ma nas chronić przed otrzymaniem wartości NULL.
    // Może zaistnieć to w przypadku kiedy próbujemy się odwołać do elementu (np. w kolekcji), który nie istnieje
    Optional<Booking> findByPositionRowAndPositionColumn(int row, int column);
}
