package com.carlgira.soda.configuration;

import oracle.jdbc.OracleConnection;
import oracle.soda.*;
import oracle.soda.rdbms.OracleRDBMSClient;
import oracle.soda.rdbms.OracleRDBMSMetadataBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import oracle.jdbc.pool.OracleDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Configuration
public class OracleConfiguration {

    @Autowired
    DataSource dataSource;


    @Bean
    OracleRDBMSClient oracleRDBMSClient() {
        OracleRDBMSClient oracleRDBMSClient = new OracleRDBMSClient();
        return oracleRDBMSClient;
    }

    @Bean
    OracleRDBMSMetadataBuilder oracleRDBMSMetadataBuilder() throws OracleException {

        OracleRDBMSMetadataBuilder oracleRDBMSMetadataBuilder = oracleRDBMSClient().createMetadataBuilder();
        return oracleRDBMSMetadataBuilder;

    }

    @Bean
    OracleDatabase oracleDatabase() throws OracleException, SQLException {
        OracleDatabase oracleDatabase = oracleRDBMSClient().getDatabase(dataSource.getConnection());
        return oracleDatabase;
    }

    @Bean
    OracleDatabaseAdmin oracleDatabaseAdmin() throws OracleException, SQLException {
        OracleDatabaseAdmin oracleDatabaseAdmin = oracleDatabase().admin();
        return oracleDatabaseAdmin;
    }

    /*
    @Bean
    OracleCollection oracleCollection() throws OracleException, SQLException {

        OracleDocument oracleDocument = oracleRDBMSMetadataBuilder().build();
        System.out.println("---------------------- " + oracleDocument.getContentAsString());
        //OracleCollection oracleCollection = oracleDatabase().admin().createCollection(collectionName, oracleDocument);

        return oracleDatabase().openCollection("books");
    }
    */

}

