package com.carlgira.soda.configuration;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import oracle.soda.*;
import oracle.sql.json.OracleJsonFactory;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class OracleOperationsImpl<T, ID> implements OracleOperations<T, ID> {

    private OracleDatabase database;
    private OracleCollection collection;
    private final ObjectMapper oMapper;
    private final OracleJsonFactory factory;
    private String collectionName;
    private Class<T> entity;

    private String idField;

    public OracleOperationsImpl() throws OracleException {
        this.oMapper = new ObjectMapper();
        this.factory = new OracleJsonFactory();
    }

    public void init(OracleDatabase database, Class<T> entity) throws OracleException {
        this.database = database;
        this.entity = entity;
        this.collectionName = entity.getName();
        this.idField = this.getIdName(this.entity);
        this.collection = this.database.openCollection(this.collectionName);
    }

    @Override
    public String getCollectionName() {
        return this.collectionName;
    }

    private String getIdName(Class<T> entity){
        String idName = null;
        for(Field field : entity.getDeclaredFields()){
            if (field.getAnnotation(javax.persistence.Id.class) != null){
                idName = field.getName();
                break;
            }
        }

        return idName;
    }

    private Map<String, String> getIdValue(T t) throws IllegalAccessException, JsonProcessingException {
        Map<String, String> id = new HashMap<>();
        for(Field field : entity.getFields()){
            field.setAccessible(true);
            if (field.getAnnotation(javax.persistence.Id.class) != null){
                id.put(field.getName(), oMapper.writeValueAsString(field.get(t)));
                break;
            }
        }
        return id;
    }

    private OracleDocument type2Doc(T t) {
        OracleDocument document = null;
        try {
            String value = oMapper.writeValueAsString(t);
            document = database.createDocumentFromString(value);
        } catch (OracleException | JsonProcessingException e) {
            e.printStackTrace();
        }
        return document;
    }

    private T doc2Type(OracleDocument doc){
        if(doc == null){
            return null;
        }

        try {
            return oMapper.readValue(doc.getContentAs(InputStream.class), entity);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (OracleException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<T> findAll(){

        List<T> list = new ArrayList<>();
        try{
            OracleCursor cursor = collection.find().getCursor();
            while (cursor.hasNext()) {
                OracleDocument doc = cursor.next();
                T object = oMapper.readValue(doc.getContentAsString(), entity);
                list.add(object);
            }
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (OracleException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public void insert(T t) {
        try {
            collection.insertAndGet(this.type2Doc(t));
        } catch (OracleException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void insert(List<T> t) {
        try {
            this.collection.insert(t.stream().map(this::type2Doc).collect(Collectors.toList()).iterator());
        } catch (OracleBatchException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<T> findById(Object id) {
        Optional<T> result = Optional.empty();

        Map<Object, Object> map = new HashMap<>();
        map.put(this.idField, id);

        try {
            OracleDocument doc = collection.find().filter(oMapper.writeValueAsString(map)).getOne();
            if(doc != null){
                result = Optional.of(oMapper.readValue(doc.getContentAs(InputStream.class), entity));
                return result;
            }
        } catch (OracleException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public void findAndReplace(T t){
        try {
            Map<String, String> idMap = this.getIdValue(t);
            OracleDocument doc = collection.find().filter(oMapper.writeValueAsString(idMap)).getOne();
            if( doc != null){
                collection.find().key(doc.getKey()).replaceOne(this.type2Doc(t));
            }
        } catch (OracleException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void findAndModify(T t) {
        try {
            Map<String, String> idMap = this.getIdValue(t);
            OracleDocument doc = collection.find().filter(oMapper.writeValueAsString(idMap)).getOne();
            if( doc != null){
                collection.find().key(doc.getKey()).mergeOne(this.type2Doc(t));
            }
        } catch (OracleException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void findAndRemove(Object id) {
        try{
            Map<Object, Object> idMap = new HashMap<>();
            idMap.put(this.idField, id);
            this.collection.find().filter(oMapper.writeValueAsString(idMap)).remove();
        } catch (OracleException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void clear()  {
        try{
            this.collection.find().remove();
        } catch (OracleException e) {
            e.printStackTrace();
        }
    }

    @Override
    public T save(T t) {
        try {
            OracleDocument doc = collection.saveAndGet(this.type2Doc(t));
            return oMapper.readValue(doc.getContentAs(InputStream.class), entity);
        } catch (OracleException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean collectionExists() {
        return this.collection != null;
    }

    @Override
    public boolean collectionExists(String collectionName) throws OracleException {
        return this.database.openCollection(collectionName) != null;
    }

    @Override
    public void dropCollection() throws OracleException {
        this.collection.admin().drop();
    }

    @Override
    public void dropCollection(String collectionName) throws OracleException {
        this.database.openCollection(collectionName).admin().drop();
    }
}
