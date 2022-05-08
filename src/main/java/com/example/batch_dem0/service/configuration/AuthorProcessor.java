package com.example.batch_dem0.service.configuration;

import com.example.batch_dem0.model.Author;
import com.example.batch_dem0.model.Image;
import org.springframework.batch.item.ItemProcessor;

public class AuthorProcessor implements ItemProcessor<Author, Author> {

    @Override
    public Author process(Author author) throws Exception {
        return author;
    }
}
