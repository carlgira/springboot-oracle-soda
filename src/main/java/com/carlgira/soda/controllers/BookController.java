package com.carlgira.soda.controllers;

import com.carlgira.soda.model.Book;
import com.carlgira.soda.services.BookService;
import com.fasterxml.jackson.core.JsonProcessingException;
import oracle.soda.OracleException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    BookService bookService;

    @PutMapping
    public void save(@RequestBody Book book) throws OracleException, IllegalAccessException {
        this.bookService.save(book);
    }

    @GetMapping
    public List<Book> findAll() throws OracleException, JsonProcessingException {
        return this.bookService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Book> findOne(@PathVariable(value="id") String id) throws OracleException, JsonProcessingException {
        return this.bookService.findOne(id);
    }

    @PostMapping
    public void update(@RequestBody Book book) throws OracleException, JsonProcessingException {
        this.bookService.update(book);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable(value="id") String id) throws OracleException {
        this.bookService.delete(id);
    }
}
