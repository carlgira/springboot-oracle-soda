package com.carlgira.soda.services;

import com.carlgira.soda.model.Book;
import com.carlgira.soda.model.BookId;
import com.fasterxml.jackson.core.JsonProcessingException;
import oracle.soda.OracleException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface BookService {

    void save(Book book) throws OracleException, IllegalAccessException;

    List<Book> findAll();

    Optional<Book> findOne(BookId id) throws OracleException, JsonProcessingException;

    public void update(Book book) throws OracleException, JsonProcessingException;

    public void delete(BookId bookId) throws OracleException;

}
