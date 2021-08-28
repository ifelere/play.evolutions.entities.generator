/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ifraytech.tools.services;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.ifraytech.tools.Entity;
import org.ifraytech.tools.EntityWriter3;

/**
 * A service that uses a writer to generate generate code files for entities
 * @author ifelere
 */
public class GenerateFilesService extends Service<Integer>{
    private final List<Entity> entities;
    private final File destination;
    private final boolean overwriteExisting;
    
    private final EntityWriter3 writer;

    /**
     * Constructs a GenerateFilesService
     * @param entities list of entities
     * @param destination the destination directory
     * @param overwriteExisting if true then the code file is overwritten if it already exists
     * @param writer the writer to delegate to
     */
    public GenerateFilesService(List<Entity> entities, File destination, boolean overwriteExisting, EntityWriter3 writer) {
        this.entities = entities;
        this.destination = destination;
        this.overwriteExisting = overwriteExisting;
        this.writer = writer;
    }
    
    
    
    @Override
    protected Task<Integer> createTask() {
        return new GeneratorTask();
    }
    
    
    private class GeneratorTask extends Task<Integer> {

        @Override
        protected Integer call() throws Exception {
            int count = 0;
            long size = entities.size();
            long index = 0L;
            try {
                for (Entity entity : entities) {
                    index += 1;
                    File dest = new File(destination, writer.createFilename(entity));
                    if (dest.exists()) {
                        if (!overwriteExisting) {
                            updateMessage(String.format("Skipped class file for %s because %s already exists", entity.getProgramName(), dest));
                            updateProgress(index, size);
                            continue;
                        }
                        dest.delete();
                    }

                    try (
                            FileWriter pw = new FileWriter(dest)
                            ) {
                        writer.write(entity, pw);
                        count += 1;

                        updateMessage(String.format("Generated class file for %s at %s", entity.getProgramName(), dest));
                        updateProgress(index, size);
                    }

                }

                updateMessage(String.format("Generated %s out of %s entities", count, size));

                updateMessage("Done");
                
                updateProgress(size, size);
                return count;
            }catch (IOException ex) {
                updateMessage("Error occurred");
                updateMessage(ex.getMessage());
                updateProgress(size, size);
            }
            
            return 0;
            
            
            
        }
        
    }
}
