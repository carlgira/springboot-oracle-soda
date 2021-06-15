package com.carlgira.soda.configuration;

import oracle.soda.OracleDatabase;
import oracle.soda.OracleException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OracleRepositoryImpl<T, ID> implements OracleRepository<T, ID> {

    private OracleOperations<T, ID> operations;

    public void configure(OracleDatabase database, OracleOperations<T, ID> operations, Class<T> objectClass) throws OracleException {
        this.operations = operations;
        this.operations.init(database, objectClass);
    }

    @Override
    public List<T> findAll() {
        return operations.findAll();
    }
}