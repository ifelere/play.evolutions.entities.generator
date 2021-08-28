/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ifraytech.tools;

import java.io.IOException;
import java.io.PrintWriter;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * An abstract class that represents a class that can generate code file from an entity
 * @author ifelere
 */
public abstract class EntityWriter2 {

   /**
    * Write generated code file from an entity
    * @param entity The entity
    * @param out The writer
    * @throws IOException 
    */ 
  public abstract void write(Entity entity, PrintWriter out) throws IOException;
    
  /**
   * Creates a file name for an entity
   * @param entity The entity
   * @return The generated file name
   */
   public abstract String createFilename(Entity entity);
    
    
    
    private final StringProperty packageName = new SimpleStringProperty();

    /**
     * Gets the package name to use for generated code file
     * @return 
     */
    public String getPackageName() {
        return packageName.get();
    }

    
    /**
     * Sets the package name to use for generated code file
     * @param value the package name to use for generated code file
     */
    public void setPackageName(String value) {
        packageName.set(value);
    }

    /**
     * Gets the packageName javafx property
     * @return 
     */
    public StringProperty packageNameProperty() {
        return packageName;
    }
    
}
