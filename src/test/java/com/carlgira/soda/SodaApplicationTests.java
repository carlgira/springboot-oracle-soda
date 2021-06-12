package com.carlgira.soda;

import com.carlgira.soda.configuration.OracleOperations;
import com.carlgira.soda.model.Book;
import com.fasterxml.jackson.core.JsonProcessingException;
import oracle.soda.OracleDatabase;
import oracle.soda.OracleException;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
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

    //@Test
    public void test1() throws OracleException, JsonProcessingException {

        this.operations.deleteAll();
        List<Book> books = operations.findAll();
        assert books.size() == 0;

        Book book1 = new Book();
        book1.setId("1");
        book1.setName("book1");

        this.operations.insert(book1);

        Optional<Book> book1R = this.operations.findOne("1");

        assert book1R.isPresent();


        Book book2 = new Book();
        book2.setId("2");
        book2.setName("book2");

        Book book3 = new Book();
        book2.setId("3");
        book2.setName("book3");

        this.operations.insert(Arrays.asList(book2, book3));

        List<Book> booksR = this.operations.findAll();

        assert booksR != null && booksR.size() == 3;

    }

}
