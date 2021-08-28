/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ifraytech.tools;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Handles mapping of SQL types to Scala types
 * 
 * For now only scala types is supported. In the future I plan to use language flag/option to customize 
 * loaded type map
 * @author ifelere
 */
public class TypeMapping extends org.xml.sax.helpers.DefaultHandler {
    public static class MappingInstance {
        private final String scalaType;
        private final String[] sqlTypes;
        private final String formatString;
        
        

        public MappingInstance(String scalaType, String[] sqlTypes, String formatString) {
            this.scalaType = scalaType;
            this.sqlTypes = sqlTypes;
            this.formatString = formatString;
        }

        public String getScalaType() {
            return scalaType;
        }

        public String[] getSqlTypes() {
            return sqlTypes;
        }

        public String getFormatString() {
            return formatString;
        }
    }

    private TypeMapping() {
    }
    
    
    
    private final List<MappingInstance> instances = new ArrayList<>();
    
    private static TypeMapping instance;
    
    private boolean loaded = false;
    
    
    public void load() throws ParserConfigurationException, SAXException, IOException {
        if (this.loaded) {
            return;
        }
        javax.xml.parsers.SAXParserFactory.newInstance().newSAXParser()
                .parse(TypeMapping.class.getResourceAsStream("resources/type-mapping.xml"), this);
        
    }
    
    public static final MappingInstance DEFAULT_INSTANCE = new MappingInstance("String", new String[0], "\"%s\"");

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if ("type".equals(qName)) {
            instances.add(new MappingInstance(attributes.getValue("name"),
                    attributes.getValue("dbTypes").split("\\s+"),
                    attributes.getValue("formatString")));
        }
    }

    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
        this.loaded = true;
    }

    public List<MappingInstance> getInstances() {
        this.checkLoaded();
        return instances;
    }

    public boolean isLoaded() {
        return !instances.isEmpty();
    }

    public static TypeMapping getInstance() {
        if (instance == null) {
            instance = new TypeMapping();
        }
        return instance;
    }
    
    
    private void checkLoaded() {
        if (!loaded) {
            try {
                load();
            } catch (ParserConfigurationException | SAXException | IOException ex) {
                this.loaded = true;
                Logger.getLogger(TypeMapping.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * Find appropriate mapping of an sqlType to program code type
     * @param sqlType
     * @return 
     */
    public MappingInstance find(String sqlType) {
        sqlType = sqlType.toLowerCase();
        int index = sqlType.indexOf('(');
        if (index > 0) {
            sqlType = sqlType.substring(0, index);
        }
        final String rType = sqlType;
        this.checkLoaded();
        return this.instances.stream()
                .filter((MappingInstance t) -> org.apache.commons.lang3.ArrayUtils.contains(t.sqlTypes, rType))
                .findAny()
                .orElse(DEFAULT_INSTANCE);
        
    }
    
    
    public boolean hasType(String name) {
        this.checkLoaded();
        return this.instances
                .stream()
                .anyMatch((t) -> t.getScalaType().equalsIgnoreCase(name));
    }
}
