/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ifraytech.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;

/**
 * Parses and SQL string and extract table and column names found in play evolution scripts
 * 
 * It handles statements found in <tt>-- !Ups </tt> section only
 * 
 * This parser is simple and only handles SQL statements that have a specific form
 * The statements must be declared as simple line-by-line format. 
 * This means that it will correctly handle SQL DDL statement of the form:
 * <code>
 * CREATE TABLE table1 (
 *  id INTEGER AUTO_INCREMENT,
 *  col1 VARCHAR(45),
 *  col2 INTEGER NOT NULL,
 *  PRIMARY KEY (id)
 * )
 * </code>
 * 
 * If will not correctly handle DDL statement of this form:
 * 
 * <code>
 *   CREATE TABLE table1 (id BIGINT(20) NOT NULL, col VARCHAR(200), col8 BIT ...)
 * </code>
 * @author ifelere
 */
public class SQLSchemaParser {
    
    /**
     * Parser listener interface to notify of status/data found in SQL statement
     */
    public static interface ParseListener {
        void foundEntity(String name);
        void foundAttribute(String entity, Attribute attribute);
        void removeAttribute(String entity, String attribute);
        void foundPrimaryKey(String entity, String attribute);
    }
    
    
    private final ParseListener listener;
    
    private int currentLine = -1;

    /**
     * Constructs a parser
     * @param listener the listener to accept entity, column found, removed, etc, events
     */
    public SQLSchemaParser(ParseListener listener) {
        this.listener = listener;
    }
    
    /**
     * Parse play evolution file containing SQL DDL statements
     * @param file The play evolution file containing SQL DDL statements
     * @throws SQLParseException
     * @throws IOException 
     */
    public void parseFile(File file) throws SQLParseException, IOException  {
        try (
                FileReader fr = new FileReader(file)
                ) {
            parseImpl(fr);
        }
    }
    
    /**
     * Parse play evolution DDL statements from a stream
     * @param res The input stream of play evolution DDL statements
     * @throws IOException
     * @throws SQLParseException 
     */
    public void parseResource(InputStream res) throws IOException, SQLParseException {
        try (
                InputStreamReader reader = new InputStreamReader(res)
                ) {
            parseImpl(reader);
        }
    }
    /**
     * Parse a string of play evolution DDL statements
     * @param sql play evolution DDL statements
     * @throws SQLParseException
     * @throws IOException 
     */
    public void parseString(String sql) throws SQLParseException, IOException {
        parseImpl(new StringReader(sql));
    }
    
    private static final Pattern CREATE_TABLE_REGEX = Pattern.compile("CREATE\\s+TABLE\\s+(?:IF\\s+NOT\\s+EXISTS\\s+)?(\\w+)", Pattern.CASE_INSENSITIVE);
    
    private static final Pattern ALTER_TABLE_REGEX = Pattern.compile("ALTER\\s+TABLE\\s+(\\w+)", Pattern.CASE_INSENSITIVE);
    
    private static final Pattern NOT_NULL_REGEX = Pattern.compile("NOT\\s+NULL", Pattern.CASE_INSENSITIVE);
    
    private static final Pattern DEFAULT_VALUE_REGEX = Pattern.compile("DEFAULT\\s+(\\S+)", Pattern.CASE_INSENSITIVE);
    
    
    private static final Pattern AUTO_INCREMENT_REGEX = Pattern.compile("AUTO_INCREMENT", Pattern.CASE_INSENSITIVE);
    
    private static final Pattern ADD_COLUMN_REGEX = Pattern.compile("ADD\\s+(?:COLUMN\\s+)?");
    
    private static final Pattern DROP_COLUMN_REGEX = Pattern.compile("DROP\\s+(?:COLUMN)?\\s+(\\w+)");
    
    
    private static final Pattern PRIMARY_KEY_REGEX = Pattern.compile("PRIMARY\\s+KEY", Pattern.CASE_INSENSITIVE);
    
    private static final Pattern NAMED_PRIMARY_KEY_REGEX = Pattern.compile("PRIMARY\\s+KEY\\s+\\((\\w+)\\)", Pattern.CASE_INSENSITIVE);
    
    private static final Pattern IGNORE_REGEX = Pattern.compile("^(INDEX|FOREIGN|TRIGGER|CREATE)\\s+", Pattern.CASE_INSENSITIVE);
    
    private static final Pattern LAST_COMMA_REGEX = Pattern.compile("\\,\\s*$");
    
    private static final Pattern UPS_SECTION_HEADER_REGEX = Pattern.compile("^\\-\\-\\s+!Ups", Pattern.CASE_INSENSITIVE);
    
    private static final Pattern DOWNS_SECTION_HEADER_REGEX = Pattern.compile("^\\-\\-\\s+!Downs", Pattern.CASE_INSENSITIVE);
    
    
    private void parseImpl(Reader reader) throws SQLParseException, IOException {
        currentLine = -1;
        BufferedReader buReader = new BufferedReader(reader);
        String line;
        String currentEntity = null;
        boolean started = false;
        while ((line = buReader.readLine()) != null) {
            line = line.trim();
            if (StringUtils.isEmpty(line)) {
                continue;
            }
            
            currentLine += 1;
            if (DOWNS_SECTION_HEADER_REGEX.matcher(line).find()) {
                break;
            }
            if (UPS_SECTION_HEADER_REGEX.matcher(line).find()) {
                started = true;
                continue;
            }
            
            if (!started) {
                continue;
            }
            
            
            
            if (line.equals(");")) {
                currentEntity = null;
            } else {
                line = LAST_COMMA_REGEX.matcher(line).replaceFirst("");
                
                Matcher m = CREATE_TABLE_REGEX.matcher(line);
                if (m.find()) {
                    currentEntity = m.group(1);
                    listener.foundEntity(currentEntity);
                    continue;
                }
                
                m = NAMED_PRIMARY_KEY_REGEX.matcher(line);
                if (m.find()) {
                    if (currentEntity == null) {
                        throw new SQLParseException(currentLine, "Primary key declaration found but no entity in focus");
                    }
                    
                    try {
                        listener.foundPrimaryKey(currentEntity, m.group(1));                        
                    } catch (Exception ex) {
                        throw new SQLParseException(currentLine, ex.getMessage(), ex);
                    }
                    continue;
                }
                
                
                
                m = ALTER_TABLE_REGEX.matcher(line);
                if (m.find()) {
                    currentEntity = m.group(1);
                    
                    String[] rest = line.substring(m.end()).split("\\,");
                    for (String stm : rest) {
                        m = ADD_COLUMN_REGEX.matcher(stm);
                        if (m.find()) {
                             parseColumnDef(currentEntity, stm.substring(m.end()));
                             continue;
                        }
                        
                        m = DROP_COLUMN_REGEX.matcher(stm);
                        if (m.find()) {
                            listener.removeAttribute(currentEntity, m.group(1));
                        }
                    }
                    
                    continue;
                }
                
                m = ADD_COLUMN_REGEX.matcher(line);
                
                if (m.find()) {
                    String[] additions = line.split("\\,");
                    for (String stm : additions) {
                        m = ADD_COLUMN_REGEX.matcher(stm);
                        if (m.find()) {
                             parseColumnDef(currentEntity, stm.substring(m.end()));
                        }
                    }
                    continue;
                }
                
                if (IGNORE_REGEX.matcher(line).find()) {
                    continue;
                }
                if (currentEntity != null) {
                    parseColumnDef(currentEntity, line);
                }
            }
        }
    }
    
    private static String cleanDefaultValue(String value) {
        if (value.endsWith(",")) {
            value = value.substring(1, value.length() - 1);
        }
        return value.replaceAll("(^'|'$)", "\"");
    }
    
    
    private void parseColumnDef(String currentEntity, String definition) throws SQLParseException {
        String[] parts = definition.split("\\s+");
        Attribute attr = new Attribute();
        TypeMapping.MappingInstance mapping = TypeMapping.getInstance().find(parts[1]);
        attr.setProgramType(mapping.getScalaType());
        attr.setOptional(!NOT_NULL_REGEX.matcher(definition).find());
        attr.setName(parts[0]);
        Matcher m = DEFAULT_VALUE_REGEX.matcher(definition);
        if (m.find()) {
            attr.setDefaultValue(cleanDefaultValue(m.group(1)));
        }
        if (AUTO_INCREMENT_REGEX.matcher(definition).find()) {
            attr.setOptional(true);
            attr.setAutoIncrements(true);
            attr.setDefaultValue("None");
        }
        
        if (PRIMARY_KEY_REGEX.matcher(definition).find()) {
            attr.setPrimaryKey(true);
        }
        
        try {
            listener.foundAttribute(currentEntity, attr);
        } catch (Exception e) {
            throw new SQLParseException(currentLine, e.getMessage(), e);
        }
    }
}
