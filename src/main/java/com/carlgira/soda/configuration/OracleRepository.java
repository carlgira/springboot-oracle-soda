package com.carlgira.soda.configuration;

import java.util.List;

public interface OracleRepository<T, ID> {

    List<T> findAll();
}
