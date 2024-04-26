package com.midlaj.olikassigment.service;

import com.midlaj.olikassigment.dto.RentalRequest;
import com.midlaj.olikassigment.model.Rental;

import java.util.List;

public interface RentalService {
    Rental createNewRental(RentalRequest rentRequest);

    Rental returnBook(Long rentalId);

    List<Rental> findOverdueRentals(Integer days);

    List<Rental> getAllRentals();

    Rental getRentalById(Long id);
}
