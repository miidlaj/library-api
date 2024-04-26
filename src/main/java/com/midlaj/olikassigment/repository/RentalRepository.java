package com.midlaj.olikassigment.repository;

import com.midlaj.olikassigment.model.Rental;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface RentalRepository extends JpaRepository<Rental, Long> {

    Boolean existsByBookIdAndRentalDateIsNull(Long bookId);

    List<Rental> findByRentalDateBeforeAndReturnDateIsNull(LocalDate currentDate);

}
