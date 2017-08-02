/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thetestmonkey.jeremyai;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.util.regex.Pattern;
import org.bson.Document;
import org.bson.types.ObjectId;

public class MongoPersonasConnector {

    MongoDatabase database = null;
    private Document _record = null;
    MongoCollection<Document> collection = null;

    
    //Creates Mongo User Collection Connection
    public MongoPersonasConnector() {

        try {

            MongoClient mongoClient = new MongoClient("localhost", 27017);

            database = mongoClient.getDatabase("JeremyAI");

            collection = database.getCollection("Personas");

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    
    //Creates Document and returns _id if it is required for other operations
    String CreateMongoDocument(Document newRecord) {

        collection.insertOne(newRecord);

        return newRecord.get("_id").toString();

    }

    //Search for document from _id
    Document RetrieveRecordBy_Id(String id) {

        BasicDBObject query = new BasicDBObject("_id", new ObjectId(id));
        Document retrievedRecord = collection.find(query).first();

        return retrievedRecord;
    }

    //retrieve Top-Level (Master) User
    private Document RetrieveMasterUser(String user) {

        boolean recordFound = false;
        try {
            BasicDBObject query = new BasicDBObject();
            query.put("userType", Pattern.compile(user, Pattern.CASE_INSENSITIVE));
            Document retrievedRecord = collection.find(query).first();

            return retrievedRecord;

        } catch (Exception e) {
            System.out.println(e.getMessage());

            return null;

        }

    }

    //Retrieve Sub Users (Similar testing requirements to a master user)
    private Document RetrieveSubUser(String user) {
        boolean recordFound = false;
        try {
            BasicDBObject userType = new BasicDBObject();
            userType.put("userType", Pattern.compile(user, Pattern.CASE_INSENSITIVE));
            BasicDBObject arrayItems = new BasicDBObject("$elemMatch", userType);
            BasicDBObject query = new BasicDBObject("subUsers", arrayItems);
            Document retrievedRecord = collection.find(query).first();

            return retrievedRecord;
        } catch (Exception e) {
            System.out.println(e.getMessage());

            return null;
        }

    }

    //Check for User Wrapper
    //TODO: needs to be smarter
    Document CheckForUsers(String user) {

        Document userDoc = RetrieveMasterUser(user);
        if (userDoc == null) {
            userDoc = RetrieveSubUser(user);
        }

        return userDoc;
    }

    //Updates a master with a new Sub USer
    void UpdateUserDocument(String masterUser, String newUser) {

        Document recordToUpdate = RetrieveMasterUser(newUser);
        BasicDBObject listItem = new BasicDBObject("subUser", new BasicDBObject("userType", newUser));
        BasicDBObject updateQuery = new BasicDBObject("$push", listItem);
        collection.updateOne(recordToUpdate, updateQuery);
    }

    //Deletes a Document if erroneously created
    //Jeremy.forget(User)?
    public void DeleteDocument(Document recordToDelete) {
        collection.deleteOne(recordToDelete);
    }
}
