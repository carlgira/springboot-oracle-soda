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

    /*
    boolean exists(Query query, Class<?> entityClass);
    <T> List<T> find(Query query, Class<T> entityClass);
    default <T> List<T> findDistinct(String field, Class<?> entityClass, Class<T> resultClass)


    <T> T findAndRemove(Query query, Class<T> entityClass);
        default <T> T findAndReplace(Query query, T replacement, String collectionName) {
        <T> T findAndModify(Query query, UpdateDefinition update, FindAndModifyOptions options, Class<T> entityClass,
        String collectionName);
        <T> T findAndRemove(Query query, Class<T> entityClass);
        long count(Query query, Class<?> entityClass);

        <T> Collection<T> insert(Collection<? extends T> batchToSave, Class<?> entityClass);

        <T> Collection<T> insertAll(Collection<? extends T> objectsToSave);

        <T> T save(T objectToSave);
        UpdateResult upsert(Query query, UpdateDefinition update, Class<?> entityClass);
        UpdateResult upsert(Query query, UpdateDefinition update, Class<?> entityClass, String collectionName);
        UpdateResult upsert(Query query, UpdateDefinition update, Class<?> entityClass, String collectionName);

        UpdateResult updateMulti(Query query, UpdateDefinition update, Class<?> entityClass);

        DeleteResult remove(Object object);
        DeleteResult remove(Query query, Class<?> entityClass);

        <T> List<T> findAllAndRemove(Query query, String collectionName);

     */

}
