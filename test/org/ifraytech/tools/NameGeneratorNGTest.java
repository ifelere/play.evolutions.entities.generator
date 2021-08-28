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
public class NameGeneratorNGTest {
    
    public NameGeneratorNGTest() {
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
     * Test of generate method, of class NameGenerator.
     */
    @Test
    public void testGenerate() {
        System.out.println("generate");
        String text = "users";
        NameGenerator instance = new NameGenerator();
        String expResult = "User";
        String result = instance.generateClassName(text);
        
        assertEquals(result, expResult);
        
        assertEquals(instance.generateClassName("schools"), "School");
        
        assertEquals(instance.generateClassName("monies"), "Money");
        
        assertEquals(instance.generateClassName("schools"), "School");
        
        assertEquals(instance.generateClassName("SCHOOLS"), "School");
        
        assertEquals(instance.generateClassName("stories"), "Storey");
        
        
        assertEquals(instance.generateClassName("HOUSES"), "House");
        
        assertEquals(instance.generateClassName("parties"), "Party");
        
        assertEquals(instance.generateClassName("HouseOfBoys"), "HouseOfBoy");
        
        
        assertEquals(instance.generateClassName("round_of_goals"), "RoundOfGoal");
        
        assertEquals(instance.generateClassName("round of goals"), "RoundOfGoal");
        
        assertNull(instance.generateClassName(null));
    }
    
}
