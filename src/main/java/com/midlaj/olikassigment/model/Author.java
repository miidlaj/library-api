package com.midlaj.olikassigment.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "author")
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "author_name", nullable = false, length = 256)
    private String name;

    @Column(name = "author_biography", nullable = false, length = 1024)
    private String biography;
}
