package com.carlgira.soda.configuration;

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
public class OracleOperationsImpl<T> implements OracleOperations<T> {

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

    public void init(OracleDatabase database, Class<T> entity, String collectionName) throws OracleException {
        this.database = database;
        this.entity = entity;
        this.collectionName = collectionName;
        this.idField = this.getIdName(this.entity);
        this.collection = this.database.openCollection(this.collectionName);
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
            collection.insertAndGet(this.createDocument(t)).getKey();
        } catch (OracleException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void insert(List<T> t) {
        try {
            this.collection.insert(t.stream().map(this::createDocument).collect(Collectors.toList()).iterator());
        } catch (OracleBatchException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<T> findOne(Object id) {
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
    public void update(T t){
        try {
            Map<String, String> idMap = this.getIdValue(t);
            OracleDocument doc = collection.find().filter(oMapper.writeValueAsString(idMap)).getOne();
            if( doc != null){
                collection.find().key(doc.getKey()).mergeOne(this.createDocument(t));
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
    public void delete(Object id) {
        try{
            Map<Object, Object> idMap = new HashMap<>();
            idMap.put(this.idField, id);
            System.out.println(oMapper.writeValueAsString(idMap));
            this.collection.find().filter(oMapper.writeValueAsString(idMap)).remove();
        } catch (OracleException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteAll()  {
        try{
            this.collection.find().remove();
        } catch (OracleException e) {
            e.printStackTrace();
        }
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

    private OracleDocument createDocument(T t) {
        OracleDocument document = null;
        try {
            String value = oMapper.writeValueAsString(t);
            document = database.createDocumentFromString(value);
        } catch (OracleException | JsonProcessingException e) {
            e.printStackTrace();
        }
        return document;
    }
}
