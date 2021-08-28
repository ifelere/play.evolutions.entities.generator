/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ifraytech.tools;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author ifelere
 */
public class TestParserListener implements SQLSchemaParser.ParseListener{
    private final Map<String, Integer> entityMap = new HashMap<>();
    
    private final Map<String, String> entityKeyMap = new HashMap<>();
    
    @Override
    public void foundEntity(String name) {
        entityMap.put(name, 0);
    }

    @Override
    public void foundAttribute(String entity, Attribute attribute) {
        entityMap.put(entity, entityMap.getOrDefault(entity, 0) + 1);
        System.out.printf("Found attribute: %s -> %s%n", entity, attribute.getName());
    }

    @Override
    public void removeAttribute(String entity, String attribute) {
        int e = entityMap.getOrDefault(entity, 0);
        if (e > 1) {
            entityMap.put(entity, e - 1);
        }
    }

    @Override
    public void foundPrimaryKey(String entity, String attribute) {
        this.entityKeyMap.put(entity, attribute);
    }
    
    public int getEntityCount() {
        return entityMap.size();
    }
    
    public boolean exists(String entity) {
        return entityMap.containsKey(entity);
    }
    
    public int getFieldCount(String entity) {
        return entityMap.getOrDefault(entity, 0);
    }
    
    public boolean hasKey() {
        return !this.entityKeyMap.isEmpty();
    }
    
    public boolean hasKey(String entity) {
        return this.entityKeyMap.containsKey(entity);
    }
    public boolean hasKey(String entity, String attribute) {
        return this.entityKeyMap.getOrDefault(entity, "-1").equals(attribute);
    }
}
