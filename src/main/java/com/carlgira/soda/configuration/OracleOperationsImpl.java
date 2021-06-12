package com.carlgira.soda.configuration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import oracle.soda.*;
import oracle.soda.rdbms.impl.OracleDocumentImpl;
import oracle.sql.json.OracleJsonFactory;
import oracle.sql.json.OracleJsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.json.Json;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Component
public class OracleOperationsImpl<T> implements OracleOperations<T> {

    private OracleDatabase database;
    private OracleCollection collection;
    private final ObjectMapper oMapper;
    private final OracleJsonFactory factory;
    private String collectionName;
    private Class<T> entity;

    public OracleOperationsImpl() throws OracleException {
        this.oMapper = new ObjectMapper();
        this.factory = new OracleJsonFactory();
    }

    public void init(OracleDatabase database, Class<T> entity, String collectionName) throws OracleException {
        this.database = database;
        this.entity = entity;
        this.collectionName = collectionName;
        this.collection = this.database.openCollection(this.collectionName);
    }

    public List<T> findAll(){

        List<T> list = new ArrayList<>();
        try{
            OracleCursor cursor = collection.find().getCursor();
            while (cursor.hasNext()) {
                OracleDocument doc = cursor.next();
                ObjectMapper mapper = new ObjectMapper();
                T object = mapper.readValue(doc.getContentAsString(), entity);
                list.add(object);
            }
        } catch (OracleException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
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

    // FIX
    @Override
    public Optional<T> findOne(Object id) {
        Optional<T> result = Optional.empty();
        String idName = this.getIdName(entity);

        Map<Object, Object> map = new HashMap<>();
        map.put(idName, id);
        ObjectMapper oMapper = new ObjectMapper();

        try {
            OracleDocument doc = collection.find().filter(oMapper.writeValueAsString(map)).getOne();
            // ObjectMapper mapper = new ObjectMapper();
            // book = Optional.of(mapper.readValue(doc.getContentAsString(), Book.class));
            result = Optional.of(doc.getContentAs(entity));
            return result;

        } catch (OracleException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public void update(T t){

        try {
            Map<String, String> idMap = this.getIdValue(t, entity);
            OracleDocument doc = collection.find().filter(new ObjectMapper().writeValueAsString(idMap)).getOne();

            if(doc.isJSON()){
                OracleJsonObject obj = doc.getContentAs(OracleJsonObject.class);

                ObjectMapper oMapper = new ObjectMapper();
                Map<String, Object> op = oMapper.convertValue(t, Map.class);
                op.forEach((k, v) -> obj.put(k ,  String.valueOf(v)));

                collection.find().key(doc.getKey()).replaceOne(database.createDocumentFrom(obj));
            }
        } catch (OracleException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    // FIX
    @Override
    public void delete(T t) {
        try{
            Map<String, String> idMap = this.getIdValue(t, entity);
            this.collection.find().filter(new ObjectMapper().writeValueAsString(idMap)).remove();
        } catch (OracleException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
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

    private Map<String, String> getIdValue(T t, Class<T> entity) throws IllegalAccessException {
        Map<String, String> id = new HashMap<>();
        for(Field field : entity.getFields()){
            if (field.getAnnotation(javax.persistence.Id.class) != null){
                id.put(field.getName(), String.valueOf(field.get(t)));
                break;
            }
        }
        return id;
    }

    private OracleDocument createDocument(T t){
        OracleJsonObject obj = factory.createObject();
        Map<String, Object> op = oMapper.convertValue(t, Map.class);
        op.forEach((k, v) -> obj.put(k ,  String.valueOf(v)));

        OracleDocument document = null;
        try {
            document = database.createDocumentFrom(obj);
        } catch (OracleException e) {
            e.printStackTrace();
        }
        return document;
    }


}
