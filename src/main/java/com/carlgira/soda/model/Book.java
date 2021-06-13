package com.carlgira.soda.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import oracle.soda.OracleDocument;
import oracle.soda.OracleDocumentFactory;
import oracle.soda.rdbms.impl.OracleDocumentFragmentImpl;
import oracle.soda.rdbms.impl.OracleDocumentImpl;
import oracle.sql.json.OracleJsonFactory;
import oracle.sql.json.OracleJsonObject;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Book  {

    @Id
    private BookId id;

    private String name;

    private Integer pages;

    private Page introduction;

    private List<Page> content;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private Date publication;

    public Book(){
    }

    public BookId getId() {
        return id;
    }

    public void setId(BookId id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }

    public Page getIntroduction() {
        return introduction;
    }

    public void setIntroduction(Page introduction) {
        this.introduction = introduction;
    }

    public List<Page> getContent() {
        return content;
    }

    public void setContent(List<Page> content) {
        this.content = content;
    }

    public Date getPublication() {
        return publication;
    }

    public void setPublication(Date publication) {
        this.publication = publication;
    }
}
