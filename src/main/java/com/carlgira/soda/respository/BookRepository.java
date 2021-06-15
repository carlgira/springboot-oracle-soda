package com.carlgira.soda.respository;

import com.carlgira.soda.configuration.OracleOperations;
import com.carlgira.soda.configuration.OracleRepositoryImpl;
import com.carlgira.soda.model.Book;
import com.carlgira.soda.model.BookId;
import oracle.soda.OracleDatabase;
import oracle.soda.OracleException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BookRepository extends OracleRepositoryImpl<Book, BookId> {

    @Autowired
    public void configure(OracleDatabase database, OracleOperations<Book, BookId> operations) throws OracleException {
        super.configure(database, operations, Book.class);
    }
}
