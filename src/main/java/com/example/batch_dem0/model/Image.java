package com.example.batch_dem0.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.GenerationType.AUTO;

@Entity
@Table(name = "images")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Image {

    @Id
    @GeneratedValue(strategy = AUTO)
    @Column(name = "imageId", nullable = false)
    private Long imageId;

    @Column(name = "imageURLS")
    private String imageURLS;

    @Column(name = "imageURLM")
    private String imageURLM;

    @Column(name = "imageURLL")
    private String imageURLL;

    @Column(name = "imageStatus")
    private String imageStatus;

}
