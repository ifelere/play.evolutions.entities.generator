/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ifraytech.tools;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;

/**
 * This is a velocity template helper designed for assisting slick database mapping generation
 * @author ifelere
 */
public class SlickMappingVelocityHelper extends ScalaGenerationHelper {
    public String tableVariableName(Entity entity) {
        return String.format("%sTable", NameGenerator.toLowerCaseFirst(entity.getProgramName()));
    }
    
    static final Pattern CONSTANCE_TYPE = Pattern.compile("^(None|Some)");
    
    /**
     * Generates options that should accompany a table column method
     * @param attribute The entity attribute
     * @return 
     */
    public String attributeOptions(Attribute attribute) {
        List<String> buff = new LinkedList<>();
        if (attribute.isPrimaryKey()) {
            buff.add("O.PrimaryKey");
        }
        if (attribute.isAutoIncrements()) {
            buff.add("O.AutoInc");
        }
        if (!StringUtils.isEmpty(attribute.getDefaultValue()) &&
                !CONSTANCE_TYPE.matcher(attribute.getDefaultValue()).find()) {
            if (attribute.isOptional()) {
                buff.add(String.format("O.Default(Some(%s))", attribute.getDefaultValue()));
            } else {
                buff.add(String.format("O.Default(%s)", attribute.getDefaultValue()));
            }
        }
        if (buff.isEmpty()) {
            return "";
        }
        return ", " + StringUtils.join(buff, ", ");
    }
    
    /**
     * Gets appropriate type name used in table column method
     * @param attribute The entity attribute
     * @return 
     */
    public String columnType(Attribute attribute) {
        if (attribute.isOptional() && !attribute.isAutoIncrements()) {
            return String.format("Option[%s]", attribute.getProgramType());
        }
        return attribute.getProgramType();
    }
    
    /**
     * Render correct presentation of the attribute in <tt>*</tt> projection of attribute
     * @param attribute The entity attribute
     * @return 
     */
    public String renderProjectionAttribute(Attribute attribute) {
        if (attribute.isAutoIncrements()) {
            return String.format("%s.?", nameGenerator.generateVariable(attribute.getName()));
        }
        return nameGenerator.generateVariable(attribute.getName());
    }
    
    /**
     * Render a tuple of attributes/columns used in <tt>*</tt> projection
     * @param entity The entity
     * @return 
     */
    public String renderProjectAttributes(Entity entity) {
        return entity.getAttributes()
                .stream()
                .map((Attribute attr) -> {
                    if (attr.isAutoIncrements()) {
                        return String.format("%s.?", this.nameGenerator.generateVariable(attr.getName()));
                    } 
                    return this.nameGenerator.generateVariable(attr.getName());
                }).collect(Collectors.joining(", "));
    }
}
