package com.example.csv_file_demo.service;

import com.example.csv_file_demo.model.Book;
import com.example.csv_file_demo.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BookService{
    List<Book> findBooksByBook_Author(String authorName);
}
