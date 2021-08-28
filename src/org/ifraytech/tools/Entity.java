/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ifraytech.tools;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Represents a database table with much smaller scope/attributes
 * @author ifelere
 */
public class Entity implements NamedObject{
    
    public Entity() {
        this.syncProgramName();
    }
    
    private void syncProgramName() {
        this.nameProperty().addListener((b, o, n) -> {
            if (n != null) {
                programName.set(generateProgramName(n));
            }
        });
    }

    
    private final ObservableList<Attribute> attributes = FXCollections.observableArrayList();
    
    private final StringProperty programName = new SimpleStringProperty();

    /**
     * Gets the database table name converted to form used in code file
     * @return 
     */
    public String getProgramName() {
        if (programName.get() == null) {
            programName.set(generateProgramName(name.getValue()));
        }
        return programName.get();
    }

    /**
     * Sets the database table name converted to form used in code file
     * @param value the database table name converted to form used in code file
     */
    public void setProgramName(String value) {
        programName.set(value);
    }

    /**
     * Gets programName javafx property
     * @return 
     */
    public StringProperty programNameProperty() {
        return programName;
    }
    
    
    /**
     * Gets list of attributes/column models for the entity
     * @return 
     */
    public ObservableList<Attribute> getAttributes() {
        return attributes;
    }

    private final StringProperty name = new SimpleStringProperty();

    /**
     * Gets the table name
     * @return 
     */
    @Override
    public String getName() {
        return name.get();
    }

    /**
     * Sets the table name
     * @param value 
     */
    public void setName(String value) {
        name.set(value);
    }

    /**
     * The table name javafx property
     * @return 
     */
    public StringProperty nameProperty() {
        return name;
    }
    
    
    private static String generateProgramName(String fromName) {
        return (new NameGenerator()).generateClassName(fromName);
    }
    
   
    
    
    
    
}
