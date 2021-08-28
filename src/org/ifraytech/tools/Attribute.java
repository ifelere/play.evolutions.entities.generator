/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ifraytech.tools;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Represents an attribute of an entity (or database column)
 * It does not try to emulate every properties of a column (varchar length, for example)
 * only those properties required to generate a code file
 * @author ifelere
 */
public class Attribute implements NamedObject {

    private final StringProperty name = new SimpleStringProperty();

    /**
     * Gets the name
     * @return 
     */
    @Override
    public String getName() {
        return name.get();
    }

    /**
     * Sets the name
     * @param value the name of the attribute
     */
    public void setName(String value) {
        name.set(value);
    }

    /**
     * Gets the name javafx property
     * @return 
     */
    public StringProperty nameProperty() {
        return name;
    }
    private final StringProperty programType = new SimpleStringProperty();

    /**
     * Gets the code type the SQL type is translated to
     * @return 
     */
    public String getProgramType() {
        return programType.get();
    }

    /**
     * Sets the code type the SQL type is translated to
     * @param value 
     */
    public void setProgramType(String value) {
        programType.set(value);
    }

    /**
     * Gets javafx property of programType
     * @return 
     */
    public StringProperty programTypeProperty() {
        return programType;
    }
    private final BooleanProperty optional = new SimpleBooleanProperty();

    /**
     * Gets whether the attribute is option or nullable
     * @return 
     */
    public boolean isOptional() {
        return optional.get();
    }

    /**
     * Sets whether the attribute is option or nullable
     * @param value if true the attribute is nullable/optional
     */
    public void setOptional(boolean value) {
        optional.set(value);
    }

    /**
     * Gets the optional javafx property
     * @return 
     */
    public BooleanProperty optionalProperty() {
        return optional;
    }
    private final StringProperty defaultValue = new SimpleStringProperty();

    /**
     * Gets the default value of the attribute
     * @return 
     */
    public String getDefaultValue() {
        return defaultValue.get();
    }

    /**
     * Sets the default value of the attribute
     * @param value 
     */
    public void setDefaultValue(String value) {
        defaultValue.set(value);
    
    }

    /**
     * Gets the default value javafx property
     * @return 
     */
    public StringProperty defaultValueProperty() {
        return defaultValue;
    }
    
    private final BooleanProperty primaryKey = new SimpleBooleanProperty(false);

    /**
     * Gets whether the attribute is primary key
     * @return 
     */
    public boolean isPrimaryKey() {
        return primaryKey.get();
    }

    /**
     * Sets whether the attribute is primary key
     * @param value if true the attribute is primary key 
     */
    public void setPrimaryKey(boolean value) {
        primaryKey.set(value);
    }

    /**
     * Gets the primary key javafx property
     * @return 
     */
    public BooleanProperty primaryKeyProperty() {
        return primaryKey;
    }
    
    private final BooleanProperty autoIncrements = new SimpleBooleanProperty(false);

    /**
     * Gets whether the attribute is an automatically incremented value
     * @return 
     */
    public boolean isAutoIncrements() {
        return autoIncrements.get();
    }

    /**
     * Sets whether the attribute is an automatically incremented value
     * @param value if true then the attribute's value is auto-incremented 
     */
    public void setAutoIncrements(boolean value) {
        autoIncrements.set(value);
    }

    /**
     * Gets the autoIncrements javafx property
     * @return 
     */
    public BooleanProperty autoIncrementsProperty() {
        return autoIncrements;
    }    
}
