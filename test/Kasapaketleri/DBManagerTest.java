/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Kasapaketleri;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author revolver
 */
public class DBManagerTest {
    
    DBManager instance; 
    public DBManagerTest() {
        instance = new DBManager();
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        instance.connect();
    }
    
    @After
    public void tearDown() {
        instance.close();
    }


    @Test 
    public void getOutputFromToTest2arg(){
        
    }
    @Test
    public void getOutputFromToTest4arg(){
    }
    
    @Test 
    public void getProductByIdTest(){
        System.out.println("getProductByIdTest");
        
    }
}
