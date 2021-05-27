package com.carlgira.soda.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import oracle.sql.json.OracleJsonFactory;
import oracle.sql.json.OracleJsonObject;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Entity
public class Book implements Serializable {
    @Id
    private String id;

    private String name;

    public Book(){
    }

    public Book(String id){
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
