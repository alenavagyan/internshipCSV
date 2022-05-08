package com.example.csv_file_demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CsvFileDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(CsvFileDemoApplication.class, args);
    }

}
