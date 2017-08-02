/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thetestmonkey.jeremyai;

import com.mongodb.BasicDBObject;
import java.util.ArrayList;
import java.util.List;
import org.bson.Document;
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
public class MonogoPersonasConnector_Test {

    MongoPersonasConnector _mc = null;
    Document retrievedRecord = null;

    public MonogoPersonasConnector_Test() {
        _mc = new MongoPersonasConnector();
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    retrievedRecord = null;
    }

    @After
    public void tearDown() {
        if (retrievedRecord != null)
                _mc.DeleteDocument(retrievedRecord);
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    @Test
    public void TestCreateMongoConnection() {
        MongoPersonasConnector mc = new MongoPersonasConnector();
    }

    @Test
    public void TestCreateMongoUserDocument() {

        List<BasicDBObject> subUsers = new ArrayList<>();
        subUsers.add(new BasicDBObject("userType", "SubUser"));
        subUsers.add(new BasicDBObject("userType", "SubUser 2"));
        Document newRecord = new Document().append("userType", "User").append("subUsers", subUsers);
        String id = _mc.CreateMongoDocument(newRecord);

        retrievedRecord = _mc.RetrieveRecordBy_Id(id);

        assertNotNull(retrievedRecord);
        
    }
    
    @Test
    public void TestFindMasterUser()
    {
        TestCreateMongoUserDocument();
        retrievedRecord = _mc.CheckForUsers("USER");
        
        assertNotNull(retrievedRecord);
    }
    
    @Test
    public void TestFindSubUser()
    {
        TestCreateMongoUserDocument();
        retrievedRecord = _mc.CheckForUsers("subuser");
        
        assertNotNull(retrievedRecord);
    }
    
    @Test
    public void TestFindNoUser()
    {
        TestCreateMongoUserDocument();
        Document _retrievedRecord = _mc.CheckForUsers("UserNotFound");
        
        assertNull(_retrievedRecord);
    }
    
}
