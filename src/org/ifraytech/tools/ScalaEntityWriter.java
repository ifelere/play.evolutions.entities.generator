/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ifraytech.tools;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * A writer to generate a scala case class given entity definition
 * 
 * @deprecated Use {@link ScalaVelocityEntityWriter} instead
 * @author ifelere
 */
@Deprecated()
public class ScalaEntityWriter extends EntityWriter3 {

    
    public ScalaEntityWriter() {
    }
    
    

    
    @Override
    public void write(Entity entity, Writer writer) throws IOException {
        PrintWriter out = new PrintWriter(writer);
        if (!StringUtils.isBlank(getPackageName())) {
            out.printf("package %s%n", getPackageName());
        }
        printBlankLines(out, 3);
        
        out.printf("case class %s (%n", entity.getProgramName());
        
        int index = 0;
        int attributeCount = entity.getAttributes().size();
        
        for (Attribute attr : entity.getAttributes()) {
            printAttribute(out, attr, index, attributeCount);
            index++;
        }
        out.println();
        
        out.println(")");
        
        printBlankLines(out, 3);
    }
    
    private static void printBlankLines(PrintWriter out, int numLines) throws IOException {
        for (int i = 0; i < numLines; i++) {
            out.println();
        }
    }
    
    private static final Pattern NOT_WRAP_DEFAULT = Pattern.compile("^(None|Some)");
    
    private static void printAttribute(PrintWriter writer, Attribute attribute, int index, int attributeCount) throws IOException {
        writer.append("\t\t");
        writer.append(attribute.getName());
        writer.append(": ");
        if (attribute.isOptional()) {
            writer.printf("Option[%s]", attribute.getProgramType());
        } else {
            writer.print(attribute.getProgramType());
        }
        if (!StringUtils.isBlank(attribute.getDefaultValue())) {
            writer.append(" = ");
            if (NOT_WRAP_DEFAULT.matcher(attribute.getDefaultValue()).find()) {
                writer.append(attribute.getDefaultValue());
            }
            else if (attribute.isOptional()) {
                writer.printf("Some(%s)", attribute.getDefaultValue());
            } else {
                writer.append(attribute.getDefaultValue());
            }
        }else if (attribute.isOptional()) {
            writer.append(" = None");
        }
        if (attributeCount > 1 && index < attributeCount - 1) {
            writer.append(",");
        }
        writer.println();
    }
    

    @Override
    public String createFilename(Entity entity) {
        return String.format("%s.scala", entity.getProgramName());
    }
    
    
}
