package com.midlaj.olikassigment.controller;

import com.midlaj.olikassigment.dto.RentalRequest;
import com.midlaj.olikassigment.model.Rental;
import com.midlaj.olikassigment.service.RentalService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Rental Controller where all the crud operations of rentals mapped
 */
@RestController
@RequestMapping("/api/rental")
public class RentalController {

    private final RentalService rentalService;

    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    /**
     * for creating new rental
     * @return created rental
     */
    @PostMapping("/new")
    public ResponseEntity<?> createNewRental(@RequestBody @Valid RentalRequest rentalRequest) {
        Rental createdRental = rentalService.createNewRental(rentalRequest);
        return new ResponseEntity<>(createdRental, HttpStatus.CREATED);
    }

    /**
     * for retrieving all rental
     * @return list of rental
     */
    @GetMapping()
    public ResponseEntity<?> getAllRentals() {
        List<Rental> rentalList = rentalService.getAllRentals();
        return new ResponseEntity<>(rentalList, HttpStatus.OK);
    }

    /**
     * for retrieving rental by id
     * @return a rental object
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getRentalById(@PathVariable Long id) {
        Rental rental = rentalService.getRentalById(id);
        return new ResponseEntity<>(rental, HttpStatus.OK);
    }


    /**
     * for returning a rental
     * @return updated rental object
     */
    @PostMapping("/return/{id}")
    public ResponseEntity<?> returnBook(@PathVariable Long id) {
        Rental updatedRental = rentalService.returnBook(id);
        return new ResponseEntity<>(updatedRental, HttpStatus.OK);
    }

    /**
     * for retrieving all due rental within some days
     * @return list of rental
     */
    @GetMapping("/overdue/{days}")
    public ResponseEntity<?> findOverdueRentals(@PathVariable Integer days) {
        List<Rental> rentalList = rentalService.findOverdueRentals(days);
        return new ResponseEntity<>(rentalList, HttpStatus.OK);
    }
}
