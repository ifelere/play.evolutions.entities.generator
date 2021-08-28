/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ifraytech.tools;

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
public class TypeMappingNGTest {
    
    public TypeMappingNGTest() {
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
     * Test of load method, of class TypeMapping.
     */
    
    

    /**
     * Test of find method, of class TypeMapping.
     */
    @Test
    public void testFind() {
        System.out.println("find");
        String sqlType = "bigint";
        TypeMapping instance = TypeMapping.getInstance();
        TypeMapping.MappingInstance result = instance.find(sqlType);
        assertEquals(result.getScalaType(), "Long");
        
        assertEquals(instance.find("varchar").getScalaType(), "String");
        
        assertEquals(instance.find("bool").getScalaType(), "Boolean");
        
        assertEquals(instance.find("date").getScalaType(), "java.time.LocalDate");
    }
    
}
