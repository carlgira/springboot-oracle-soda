package com.carlgira.soda.services;

import com.carlgira.soda.configuration.OracleOperations;
import com.carlgira.soda.model.Book;
import com.fasterxml.jackson.core.JsonProcessingException;
import oracle.soda.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class BookServiceImpl implements BookService {

    @Autowired
    OracleOperations operations;

    public void save(Book book) throws OracleException, IllegalAccessException {
        this.operations.save(book);
    }

    public List<Book> findAll(){
        return operations.findAll(Book.class);
    }

    public Optional<Book> findOne(String id) throws OracleException, JsonProcessingException {
        return this.operations.findOne(id, Book.class);
    }

    public void update(Book book) {
        this.operations.update(book, Book.class);
    }

    public void delete(String id) throws OracleException {
        this.operations.delete(new Book(id), Book.class);
    }
}
