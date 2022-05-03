package com.example.csv_file_demo.service.batch_processing;

import com.example.csv_file_demo.model.Book;
import com.example.csv_file_demo.repository.BookRepository;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class BookProcessor implements ItemProcessor<Book, Book> {

    @Autowired
    BookRepository bookRepository;


    @Override
    public Book process(Book book) throws Exception {

        return book;
    }
}
