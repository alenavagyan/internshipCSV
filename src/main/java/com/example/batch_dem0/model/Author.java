package com.example.batch_dem0.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.GenerationType.AUTO;

@Entity
@Table(name = "authors")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Author {

    @Id
    @GeneratedValue(strategy = AUTO)
    @Column(name = "authorId", nullable = false)
    private Long authorId;

    @Column(name = "Book-Author")
    private String book_Author;

}
