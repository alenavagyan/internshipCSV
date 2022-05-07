package com.example.csv_file_demo.service.batch_processing;

import com.example.csv_file_demo.model.Book;
import com.example.csv_file_demo.repository.BookRepository;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BookWriter implements ItemWriter<Book> {

    @Autowired
    BookRepository bookRepository;

    @Override
    public void write(List<? extends Book> books) throws Exception {
        System.out.println("Thread name: " + Thread.currentThread().getName());
        bookRepository.saveAll(books);
    }
}
