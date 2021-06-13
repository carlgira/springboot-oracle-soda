package com.carlgira.soda;

import com.carlgira.soda.configuration.OracleOperations;
import com.carlgira.soda.model.Book;
import com.carlgira.soda.model.BookId;
import com.carlgira.soda.model.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import oracle.soda.OracleDatabase;
import oracle.soda.OracleException;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
class SodaApplicationTests {

    static {
        System.setProperty("oracle.net.tns_admin", "wallet-atp");
    }

    private OracleOperations<Book> operations;

    @Autowired
    public void configure(OracleDatabase database, OracleOperations<Book> operations) throws OracleException {
        this.operations = operations;
        this.operations.init(database, Book.class, "books");
    }

    @Test
    public void test1() throws OracleException, JsonProcessingException {
        // Delete All
        this.operations.deleteAll();
        List<Book> books = operations.findAll();
        assert books.size() == 0;

        // Insert One
        Book book1 = new Book();
        book1.setId(new BookId(1));
        book1.setName("book1");
        book1.setPages(10);
        book1.setIntroduction(new Page(1, true));
        book1.setPublication(new Date());

        this.operations.insert(book1);

        Optional<Book> book1R = this.operations.findOne(new BookId(1));

        assert book1R.isPresent() && book1R.get().getId().equals(new BookId(1)) && book1R.get().getName().equals("book1");

        // Insert Set
        Book book2 = new Book();
        book2.setId(new BookId(2));
        book2.setName("book2");
        book2.setPages(10);
        book2.setIntroduction(new Page(1, true));
        book2.setContent(List.of(new Page(1, false), new Page(2, false)));
        book2.setPublication(new Date());

        Book book3 = new Book();
        book3.setId(new BookId(3));
        book3.setName("book3");
        book3.setPages(10);
        book3.setIntroduction(new Page(1, true));
        book3.setContent(List.of(new Page(3, false), new Page(4, false)));
        book3.setPublication(new Date());

        this.operations.insert(Arrays.asList(book2, book3));

        List<Book> booksR = this.operations.findAll();

        assert booksR != null && booksR.size() == 3;

        // Update
        Book book4 = new Book();
        book4.setId(new BookId(3));
        book4.setName("book31");
        book4.setPages(10);
        book4.setIntroduction(new Page(10, true));
        book4.setContent(List.of(new Page(5, false), new Page(6, false)));
        book4.setPublication(new Date());

        this.operations.update(book4);

        Optional<Book> book4R = this.operations.findOne(book4.getId());

        assert book4R.isPresent() && book4R.get().getName().equals("book31");

        // Delete
        this.operations.delete(book4.getId());

        Optional<Book> book5R = this.operations.findOne(book4.getId());

        assert book5R.isEmpty();
    }

}
