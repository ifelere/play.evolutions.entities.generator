/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ifraytech.tools;

import java.io.File;
import java.io.InputStream;
import static org.testng.Assert.*;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 *
 * @author ifelere
 */
public class SQLSchemaParserNGTest {
    
    public SQLSchemaParserNGTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @BeforeMethod
    public void setUpMethod() throws Exception {
    }

    @AfterMethod
    public void tearDownMethod() throws Exception {
    }

    /**
     * Test of parseFile method, of class SQLSchemaParser.
     */
    @Test
    public void testParseFile() throws Exception {
        System.out.println("parseFile");
        File file = new File("C:\\Users\\user\\Documents\\apps\\congtools\\conf\\evolutions\\default\\1.sql");
        TestParserListener listener = new TestParserListener();
        SQLSchemaParser instance = new SQLSchemaParser(listener);
        instance.parseFile(file);
        // TODO review the generated test code and remove the default call to fail.
        assertEquals(listener.getEntityCount(), 1);
        assertTrue(listener.exists("congregations"));
        assertEquals(listener.getFieldCount("congregations"), 8);
        assertTrue(listener.hasKey("congregations", "id"));
    }

    /**
     * Test of parseResource method, of class SQLSchemaParser.
     */
    @Test
    public void testParseResource() throws Exception {
        System.out.println("parseResource");
        TestParserListener listener = new TestParserListener();
        InputStream res = getClass().getResourceAsStream("test_migration.sql");
        SQLSchemaParser instance = new SQLSchemaParser(listener);
        instance.parseResource(res);
        
        
        assertEquals(listener.getEntityCount(), 3);
        
        for (String name : new String[] {"user_groups", "user_group_members", "user_group_permissions"}) {
            assertTrue(listener.exists(name));
            assertTrue(listener.getFieldCount(name) > 0);
            assertTrue(listener.hasKey(name, "id"));
        }
    }

    /**
     * Test of parseString method, of class SQLSchemaParser.
     */
    @Test
    public void testParseString() throws Exception {
        System.out.println("parseString");
        
        TestParserListener listener = new TestParserListener();
        String sql = "-- Congregation table\n" +
"\n" +
"-- !Ups\n" +
"CREATE TABLE congregations (\n" +
"    id bigint(20) NOT NULL AUTO_INCREMENT,\n" +
"    name varchar(255) NOT NULL,\n" +
"    cong_number varchar(12) NULL,\n" +
"    circuit_number varchar(12) NULL,\n" +
"    address varchar(255) NULL,\n" +
"    created_at TIMESTAMP NULL,\n" +
"    updated_at TIMESTAMP NULL,\n" +
"    deleted_at DATETIME NULL,\n" +
"    PRIMARY KEY (id)\n" +
");\n" +
"\n" +
"-- !Downs\n" +
"DROP TABLE congregations;";
        SQLSchemaParser instance = new SQLSchemaParser(listener);
        instance.parseString(sql);
        
        assertEquals(listener.getEntityCount(), 1);
        assertTrue(listener.exists("congregations"));
        assertEquals(listener.getFieldCount("congregations"), 8);
        assertTrue(listener.hasKey("congregations", "id"));
    }
    
}
