package com.example.csv_file_demo.model;

import lombok.Data;

import javax.persistence.*;

import static javax.persistence.GenerationType.AUTO;

@Entity
@Table(name = "Authors")
@Data
public class Author {

    @Id
    @GeneratedValue(strategy = AUTO)
    @Column(name = "authorId", nullable = false)
    private Long authorId;

    private String authorName;

}
