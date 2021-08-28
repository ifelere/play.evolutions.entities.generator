/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ifraytech.tools;

import java.io.IOException;
import java.io.Writer;
import java.util.Collections;
import java.util.logging.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

/**
 * An entity code generate that uses an embedded template written in Velocity
 * @see <a href="http://velocity.apache.org/">Velocity</a>
 * @author ifelere
 */
public class ScalaVelocityEntityWriter extends EntityWriter3 {
    private final ScalaGenerationHelper helper = new ScalaGenerationHelper();
    private static final Logger LOGGER = Logger.getLogger(ScalaVelocityEntityWriter.class.getName());
            
    
    @Override
    public void write(Entity entity, Writer writer) throws IOException {
        VelocityContext ctx = new VelocityContext();
        ctx.put("packageName", getPackageName());
        ctx.put("helper", helper);
        ctx.put("entity", entity);
        Template t = this.getTemplate();
        t.merge(ctx, writer, Collections.EMPTY_LIST);
    }
    
    
    private Template template;
    
    private Template getTemplate() {
        if (template == null) {
            template = this.getVelocityEngine().getTemplate("/org/ifraytech/tools/resources/scala.class.template");
        }
        return template;
    }
    
    

    @Override
    public String createFilename(Entity entity) {
        return String.format("%s.scala", entity.getProgramName());
    }
    
    private VelocityEngine engine;
    
    private VelocityEngine getVelocityEngine() {
        if (engine == null) {
            engine = new VelocityEngine();
            engine.setProperty(RuntimeConstants.RESOURCE_LOADERS, "class");
            engine.setProperty("resource.loader.class.class", ClasspathResourceLoader.class.getName());
            engine.init();
        }
        return engine;
    }
    
}
