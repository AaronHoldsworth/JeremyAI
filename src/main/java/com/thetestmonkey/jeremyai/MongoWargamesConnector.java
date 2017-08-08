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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import org.bson.Document;

/**
 *
 * @author TTGAHX
 */
public class MongoWargamesConnector implements MongoConnector {

    private MongoDatabase database;
    private MongoCollection<Document> collection;

    public MongoWargamesConnector() {
        java.util.logging.Logger.getLogger("org.mongodb.driver").setLevel(Level.SEVERE);
        this.database = null;
        this.collection = null;
    }

    @Override
    public void ConnectToMongoDatabse() {
        try {

            MongoClient mongoClient = new MongoClient("localhost", 27017);

            database = mongoClient.getDatabase("JeremyAI");

            collection = database.getCollection("Wargames");

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void QueryMongoCollection() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void CreateNewRecord(Document newRecord) {
        try {
            collection.insertOne(newRecord);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    void GenerateResults(HashMap<Integer, Integer> moves, String result) {

        List<BasicDBObject> movesDbObj = new ArrayList<>();
        for (Map.Entry<Integer, Integer> move : moves.entrySet()) {
            movesDbObj.add(new BasicDBObject(Integer.toString(move.getKey()), move.getValue()));
        }

        Document newRecord = new Document().append("Result", result).append("MoveList", movesDbObj);

        CreateNewRecord(newRecord);
    }

}
