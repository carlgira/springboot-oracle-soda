package com.carlgira.soda.configuration;

import com.fasterxml.jackson.core.JsonProcessingException;
import oracle.soda.*;



import java.util.List;
import java.util.Optional;

public interface OracleOperations<T> {

    void init(OracleDatabase database, Class<T> entity, String collectionName) throws OracleException ;

    List<T> findAll();

    void insert(T t);

    void insert(List<T> t);

    Optional<T> findOne(Object id);

    void update(T t);

    void delete(T t);

    void deleteAll();

}
