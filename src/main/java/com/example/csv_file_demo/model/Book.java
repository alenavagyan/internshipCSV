package com.example.csv_file_demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.GenerationType.AUTO;

@Entity
@Table(name = "Books")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = AUTO)
    @Column(name = "bookId", nullable = false)
    private Long bookId;

    @Column(name = "ISBN")
    private String isbn;

    @Column(name = "Book-Title")
    private String book_Title;

    @Column(name = "Book-Author")
    private String book_Author;

    @Column(name = "Publisher")
    private String publisher;

    @Column(name = "Image-URL-M")
    private String image_URL_M;

    @Column(name = "Image-URL-S")
    private String image_URL_S;

    @Column(name = "Image-URL-L")
    private String image_URL_L;

    @Column(name = "Year-Of-Publication")
    private Integer year_of_publication;


}
