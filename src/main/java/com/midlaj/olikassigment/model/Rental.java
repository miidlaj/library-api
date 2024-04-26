package com.midlaj.olikassigment.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "rental")
public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "book_id", nullable = false)
    private Long bookId;

    @Column(name = "renter_name", nullable = false)
    private String renterName;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "rental_date", nullable = false)
    private LocalDate rentalDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "return_date")
    private LocalDate returnDate;
}
