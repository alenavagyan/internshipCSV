package com.example.batch_dem0.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.util.HashSet;
import java.util.Set;

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

    @JsonIgnore
    @ManyToMany(mappedBy = "book_authors")
    Set<Book> books = new HashSet<>();

}
