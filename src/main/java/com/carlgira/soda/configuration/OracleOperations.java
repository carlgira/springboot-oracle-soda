package com.carlgira.soda.configuration;

import oracle.soda.*;
import java.util.List;
import java.util.Optional;

public interface OracleOperations<T, ID> {

    void init(OracleDatabase database, Class<T> entity) throws OracleException ;

    List<T> findAll();

    void insert(T t);

    void insert(List<T> t);

    Optional<T> findById(ID id);

    void findAndReplace(T t);

    void findAndModify(T t);

    void findAndRemove(ID id);

    void clear();

    T save(T t);

    String getCollectionName();

    boolean collectionExists();

    boolean collectionExists(String collectionName) throws OracleException;

    void dropCollection() throws OracleException;

    void dropCollection(String collectionName) throws OracleException;
}
