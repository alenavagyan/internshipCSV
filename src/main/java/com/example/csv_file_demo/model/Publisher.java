package com.example.csv_file_demo.model;

import lombok.Data;

import javax.persistence.*;

import static javax.persistence.GenerationType.AUTO;

@Entity
@Table(name = "Publishers")
@Data
public class Publisher {

    @Id
    @GeneratedValue(strategy = AUTO)
    @Column(name = "publisherId", nullable = false)
    private Long publisherId;

    private String publisherName;
}
