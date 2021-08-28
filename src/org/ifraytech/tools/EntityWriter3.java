/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ifraytech.tools;

import java.io.IOException;
import java.io.Writer;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * An abstract class to generate a code file for an entity
 * @author ifelere
 */
public abstract class EntityWriter3 {
    /**
     * Generate a code file for an entity
     * @param entity The entity
     * @param out The output writer
     * @throws IOException 
     */
    public abstract void write(Entity entity, Writer out) throws IOException;
    
    /**
     * Suggest a filename for an entity
     * @param entity The entity
     * @return Generated file name
     */
   public abstract String createFilename(Entity entity);
    
    
    
    private final StringProperty packageName = new SimpleStringProperty();

    /**
     * Get the name of the package to use for generated code file
     * @return 
     */
    public String getPackageName() {
        return packageName.get();
    }

    
    /**
     * Set the name of the package to use for generated code file
     * @param value 
     */
    public void setPackageName(String value) {
        packageName.set(value);
    }

    
    /**
     * Get javafx property for the package name
     * @return
     */
    public StringProperty packageNameProperty() {
        return packageName;
    }
}
