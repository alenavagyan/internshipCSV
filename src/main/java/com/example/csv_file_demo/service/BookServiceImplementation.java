package com.example.csv_file_demo.service;

import com.example.csv_file_demo.model.Book;
import com.example.csv_file_demo.repository.BookRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

@Service
public class BookServiceImplementation implements BookService{

    @Autowired
    private JobLauncher jobLauncher;
    @Autowired
    private Job job;


    BookRepository bookRepository;

    public BookServiceImplementation(BookRepository bookRepository){
        this.bookRepository=bookRepository;
    }

    public void savingCsvInDB(){
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("startAt", System.currentTimeMillis()).toJobParameters();
        try {
            jobLauncher.run(job, jobParameters);
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException | JobParametersInvalidException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Book> findBooksByBook_Author(String authorName) {
        List<Book> books = bookRepository.findBooksByBook_Author(authorName);
        return books;
    }

    @Scheduled(fixedRate = 5000)
    public void downloadingImages(){
        for(int i=0; i<=280000; i++) {
            if (i % 100 == 0) {

            }
        }

        System.out.println("Job works fine");



    }

}
