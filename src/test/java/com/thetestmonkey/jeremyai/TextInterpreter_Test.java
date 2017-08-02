/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thetestmonkey.jeremyai;

import java.util.HashMap;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author TTGAHX
 */
public class TextInterpreter_Test {
    
    public TextInterpreter_Test() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void InstantiateTextInterpreter() 
    {
        TextInterpreter ti = new TextInterpreter();
        assertNotNull(ti);
    }
    
    @Test
    public void TestUserCreationFromRequirement()
    {
        TextInterpreter ti = new TextInterpreter();
        HashMap<String, String> output = ti.InterpretRequirement("As a User I want a Big Red Button called Verify so that I can press the Big red Button");
        
        assertNotNull(output);
    }
}
