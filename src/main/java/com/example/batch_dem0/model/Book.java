package com.example.batch_dem0.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static javax.persistence.GenerationType.AUTO;

@Entity
@Table(name = "books")
@Data
@NoArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = AUTO)
    @Column(name = "book_id", nullable = false)
    private Long bookId;

    @Column(name = "isbn")
    private String isbn;

    @Column(name = "book_name")
    private String bookName;
//
//    @OneToMany(cascade = CascadeType.ALL)
//    @JoinColumn(name = "fk_book_id", referencedColumnName = "book_id")
//    private List<Author> author;

    @ManyToMany
    @JoinTable(
            name = "book_authors",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private Set<Author> book_authors = new HashSet<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_book_image_id")
    private Image image;


}
