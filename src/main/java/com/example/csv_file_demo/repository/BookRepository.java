package com.example.csv_file_demo.repository;

import com.example.csv_file_demo.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    @Query(value = "SELECT * FROM Books b WHERE" + "b.book_Author LIKE CONCAT('%', :authorName, '%')", nativeQuery = true)
    List<Book> findBooksByBook_Author(String authorName);

}
