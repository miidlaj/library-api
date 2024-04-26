package com.midlaj.olikassigment.model;

import com.midlaj.olikassigment.annotation.MaxYear;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name ="book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name= "book_title", length = 256, nullable = false)
    private String title;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private Author author;

    @Column(unique = true, nullable = false, name = "book_isbn")
    private String isbn;

    @MaxYear
    @Column(name =  "publication_year", nullable = false)
    private Integer publicationYear;

    @Column(name =  "is_available", nullable = false)
    private Boolean available;
}
