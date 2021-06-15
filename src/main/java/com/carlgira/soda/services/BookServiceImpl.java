package com.carlgira.soda.services;

import com.carlgira.soda.configuration.OracleOperations;
import com.carlgira.soda.model.Book;
import com.carlgira.soda.model.BookId;
import com.fasterxml.jackson.core.JsonProcessingException;
import oracle.soda.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class BookServiceImpl implements BookService {


    private OracleOperations<Book, BookId> operations;

    @Autowired
    public void configure(OracleDatabase database, OracleOperations<Book, BookId> operations) throws OracleException {
        this.operations = operations;
        this.operations.init(database, Book.class);
    }

    public void save(Book book) throws OracleException, IllegalAccessException {
        this.operations.insert(book);
    }

    public List<Book> findAll(){
        return operations.findAll();
    }

    public Optional<Book> findOne(BookId bookId) throws OracleException, JsonProcessingException {
        return this.operations.findById(bookId);
    }

    public void update(Book book) {
        this.operations.findAndReplace(book);
    }

    public void delete(BookId bookId) throws OracleException {
        this.operations.findAndRemove(bookId);
    }
}
