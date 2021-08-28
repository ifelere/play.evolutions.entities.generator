/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ifraytech.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.ifraytech.tools.ui.SlickMappingOptions;

/**
 * A generate that generates slick mapping for entities using an embedded Velocity template
 * @author ifelere
 */
public class ScalaSlickMappingGenerator extends SlickMappingGenerator {

    @Override
    public void write(EntityCollection collection, SlickMappingOptions options, GeneratorListener listener) throws IOException {
        listener.calculatedWorkSize(10);
        
        File outputFile =  new File(options.getDestination(), String.format("%s.scala", options.getWrappClassName()));
        
        listener.onMessage(String.format("Will be generated to %s", outputFile.getAbsolutePath()));
        if (outputFile.exists()) {
            outputFile.delete();
        }
        try (
                FileWriter writer = new FileWriter(outputFile)
          ) {
            VelocityEngine engine = new VelocityEngine();
            engine.setProperty(RuntimeConstants.RESOURCE_LOADERS, "class");
            engine.setProperty("resource.loader.class.class", ClasspathResourceLoader.class.getName());
            engine.init();
            Template t = engine.getTemplate("/org/ifraytech/tools/resources/play.slick-table.schema.template");
            listener.onWorkDone(3);
            listener.onMessage("Loaded mapping template");
            VelocityContext ctx = new VelocityContext();
            ctx.put("helper", new SlickMappingVelocityHelper());
            ctx.put("collection", collection);
            ctx.put("schemaPackageName", options.getPackageName());
            ctx.put("entitiesPackage", getEntityPackageName());
            ctx.put("wrapperClassName", options.getWrappClassName());
            ctx.put("slickProfile", options.getSlickProfile());
            ctx.put("additionalImports", stringToList(options.getAdditionalImports()));
            ctx.put("extensions", stringToList(options.getExtensions()));
            
            listener.onWorkDone(5);
            
            t.merge(ctx, writer);
            
            listener.onWorkDone(10);
            
            listener.onMessage(String.format("Generated to %s successfully", outputFile.getAbsolutePath()));
            
            listener.onComplete();
        }
    }
    
    
    private static List<String> stringToList(String text) {
        if (StringUtils.isBlank(text)) {
            return Collections.EMPTY_LIST;
        }
        try (
            BufferedReader reader = new BufferedReader(new StringReader(text))
                ) {
            
            return reader.lines()
                    .map(x -> x.trim())
                    .filter(x -> !x.isEmpty())
                    .collect(Collectors.toList());
        } catch (IOException ex) {
            Logger.getLogger(ScalaSlickMappingGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Collections.EMPTY_LIST;
    }
    
    
        
    
    
    
        
        
    
}
