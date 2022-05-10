package com.example.csv_file_demo.repository;

import com.example.csv_file_demo.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
