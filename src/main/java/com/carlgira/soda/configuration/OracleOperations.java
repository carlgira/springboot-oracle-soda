package com.carlgira.soda.configuration;

import com.fasterxml.jackson.core.JsonProcessingException;
import oracle.soda.*;



import java.util.List;
import java.util.Optional;

public interface OracleOperations<T> {

    List<T> findAll(Class<T> entity);

    void save(T t);

    Optional<T> findOne(Object id, Class<T> entity);

    void update(T t, Class<T> entity);

    void delete(T t, Class<T> entity);

}
