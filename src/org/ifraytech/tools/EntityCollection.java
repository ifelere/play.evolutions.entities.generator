/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ifraytech.tools;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javafx.collections.ObservableList;

/**
 * This class is responsible for holding a collection of entities and coordinating with parser to
 * create/update entities 
 * @author ifelere
 */
public class EntityCollection implements SQLSchemaParser.ParseListener {

    private final ObservableList<Entity> entities = javafx.collections.FXCollections.observableArrayList();
    
    private final List<Entity> lastEntities = new ArrayList<>();
    
   
    @Override
    public void foundEntity(String name) {
        Entity et = new Entity();
        et.setName(name);
        entities.add(et);
        lastEntities.add(et);
    }

    @Override
    public void foundAttribute(String entity, Attribute attribute) {
        Optional<Entity> result =  entities.stream()
                .filter((Entity e) -> entity.equals(e.getName())).findAny();
        if (!result.isPresent()) {
            throw new IllegalArgumentException(String.format("Could not find entity named %s", entity));
        }    
        result.get().getAttributes().add(attribute);
    }
    
    private SQLSchemaParser parser;

    private SQLSchemaParser getParser() {
        if (parser == null) {
            parser = new SQLSchemaParser(this);
        }
        return parser;
    }
    
    
    
    public List<Entity> addSQLFile(File file) throws SQLParseException, IOException {
        lastEntities.clear();
        this.getParser().parseFile(file);
        return lastEntities;
    }

    public ObservableList<Entity> getEntities() {
        return entities;
    }

    @Override
    public void foundPrimaryKey(String entity, String attribute) {
        Optional<Entity> result =  entities.stream()
                .filter((Entity e) -> entity.equals(e.getName())).findAny();
        if (!result.isPresent()) {
            throw new IllegalArgumentException(String.format("Could not find entity named %s", entity));
        }
        
        result.get().getAttributes().stream()
                .filter((a) -> attribute.equals(a.getName())).findAny().ifPresent((attr) -> {
                attr.setPrimaryKey(true);});
        
        
    }

    @Override
    public void removeAttribute(String entity, String attribute) {
        entities
                .stream()
                .filter((e) -> e.getName().equals(entity))
                .findFirst()
                .ifPresent((model) -> {
                    model.getAttributes()
                            .removeIf((a) -> a.getName().equals(attribute));
                });
        
    }
    
    
}
