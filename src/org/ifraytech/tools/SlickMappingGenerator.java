/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ifraytech.tools;

import java.io.IOException;
import org.ifraytech.tools.ui.SlickMappingOptions;

/**
 * This is a generator class that generates mapping trait/object to be used in slick queries
 * @author ifelere
 */
public abstract class SlickMappingGenerator {
    public static interface GeneratorListener {
        void calculatedWorkSize(long size);
        
        void onMessage(String message);
        
        void onWorkDone(long value);
        
        void onError(Throwable ex);
        
        void onComplete();
    }
    
    private String entityPackageName;

    /**
     * Get the value of entityPackageName
     *
     * @return the value of entityPackageName
     */
    public String getEntityPackageName() {
        return entityPackageName;
    }

    /**
     * Set the value of entityPackageName
     *
     * @param entityPackageName new value of entityPackageName
     */
    public void setEntityPackageName(String entityPackageName) {
        this.entityPackageName = entityPackageName;
    }

    
    /**
     * Generates a slick database mapping trait/object/class
     * @param collection The entity collection
     * @param options The options to guide the generation
     * @param listener Task listener interface
     * @throws IOException 
     */
    public abstract void write(EntityCollection collection,
            SlickMappingOptions options,
            GeneratorListener listener) throws IOException; 
}
