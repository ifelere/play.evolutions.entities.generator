/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ifraytech.tools;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;

/**
 * A helper class used in velocity templates
 * @author ifelere
 */
public class ScalaGenerationHelper {
    protected final NameGenerator nameGenerator = new NameGenerator();
    
    private static final Pattern NOT_WRAP_DEFAULT = Pattern.compile("^(None|Some)");
    
    public String programName(String text) {
        return this.nameGenerator.generateClassName(text);
    }
    public String programName(String text, boolean asVariable) {
        if (asVariable) {
            return this.nameGenerator.generateVariable(text);
        }
        return this.nameGenerator.generateClassName(text);
    }
    /**
     * Formats the present attribute/property type of the scala class
     * @param attribute The entity attribute
     * @return Formatted type for scala class property
     */
    public String renderAttributeType(Attribute attribute) {
        StringWriter sw = new StringWriter();
        PrintWriter writer = new PrintWriter(sw);
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
        writer.flush();
        return sw.toString();
    }
}
