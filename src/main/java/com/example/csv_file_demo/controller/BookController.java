package com.example.csv_file_demo.controller;

import com.example.csv_file_demo.model.Book;
import com.example.csv_file_demo.service.BookServiceImplementation;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/book")
public class BookController {

    private BookServiceImplementation bookService = new BookServiceImplementation();
    @Autowired
    private JobLauncher jobLauncher;
    @Autowired
    private Job job;

    @PostMapping("/parseBooks")
    public void savingCsvInDB(){
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("startAt", System.currentTimeMillis()).toJobParameters();
        try {
            jobLauncher.run(job, jobParameters);
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException | JobParametersInvalidException e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/downloadingImages")
    public void downloadingImages(){
        bookService.downloadingImages();
    }

    @GetMapping("/booksByName")
    public ResponseEntity<List<Book>> searchBooks(@RequestParam("authorName") String authorName){
        return ResponseEntity.ok(bookService.findBooksByBook_Author(authorName));
    }
}
