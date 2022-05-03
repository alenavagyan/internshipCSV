package com.example.csv_file_demo.model;


import lombok.Data;

import javax.persistence.*;

import static javax.persistence.GenerationType.AUTO;

@Entity
@Table(name = "Images")
@Data
public class Image {

    @Id
    @GeneratedValue(strategy = AUTO)
    @Column(name = "imageId", nullable = false)
    private Long imageId;

    private String imageUrlS;
    private String imageUrlM;
    private String imageUrlL;
}
