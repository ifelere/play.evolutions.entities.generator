/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ifraytech.tools.services;

import java.util.List;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.ifraytech.tools.EntityCollection;
import org.ifraytech.tools.SlickMappingGenerator;
import org.ifraytech.tools.ui.SlickMappingOptions;

/**
 * A service that uses a writer/generator to generate slick database mapping for entities
 * @author ifelere
 */
public class SlickMappingGeneratorService extends Service<Void> {
    private final EntityCollection entityCollection;
    private final SlickMappingOptions options;
    private final SlickMappingGenerator generator;

    /**
     * Constructs new SlickMappingGeneratorService
     * @param entityCollection the entity collection instance
     * @param options The mapping options
     * @param generator the generator that is deligated to
     */
    public SlickMappingGeneratorService(EntityCollection entityCollection, SlickMappingOptions options, SlickMappingGenerator generator) {
        this.entityCollection = entityCollection;
        this.options = options;
        this.generator = generator;
    }
    
    @Override
    protected Task<Void> createTask() {
        return new GeneratorTask();
    }
    
    private class GeneratorTask extends Task<Void> implements SlickMappingGenerator.GeneratorListener{
        private long workSize = -1L;

        @Override
        protected Void call() throws Exception {
            List<String> errors = options.validate();
            long size = entityCollection.getEntities().size();
            if (!errors.isEmpty()) {
                updateMessage("Incorrect options");
                errors.forEach((m) -> updateMessage(m));
                updateProgress(size, size);
                return null;
            }
            
            generator.write(entityCollection, options, this);
            
            return null;
        }

        @Override
        public void calculatedWorkSize(long size) {
            this.workSize = size;
        }

        @Override
        public void onMessage(String message) {
            updateMessage(message);
        }

        @Override
        public void onWorkDone(long value) {
            if (workSize > 0) {
                updateProgress(value, workSize);
            }
        }

        @Override
        public void onError(Throwable ex) {
            updateMessage("Error");
            updateMessage(ex.getMessage());
        }

        @Override
        public void onComplete() {
            updateMessage("Done");
            updateProgress(workSize, workSize);
        }
        
        
    }
    
}
