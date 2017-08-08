/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thetestmonkey.jeremyai;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author TTGAHX
 */
public class MongoWargamesConnector implements MongoConnector {

    private MongoDatabase database;
    private MongoCollection<Document> collection;
    private int winCount = 1;
    private HashMap<Integer, HashMap<Integer, Integer>> winResults = new HashMap<>();
    private HashMap<Integer, HashMap<Integer, Integer>> loseResults = new HashMap<>();
    private HashMap<Integer, HashMap<Integer, Integer>> drawResults = new HashMap<>();
    private int drawCount = 1;
    private int loseCount = 1;

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
        FindIterable<Document> allDocs = collection.find();
        HashMap<Integer, Integer> moves;
        JSONArray moveList;
        for (Document doc : allDocs) {

            JSONObject jsonRecord = new JSONObject(doc.toJson());

            if ("Jeremy".equalsIgnoreCase(jsonRecord.getString("Result"))) {

                winResults.put(winCount, GenerateMoveList(jsonRecord));
                winCount++;

            } else if ("Other".equalsIgnoreCase(jsonRecord.getString("Result"))) {
                loseResults.put(winCount, GenerateMoveList(jsonRecord));
                loseCount++;
            } else if ("Draw".equalsIgnoreCase(jsonRecord.getString("Result"))) {
                drawResults.put(winCount, GenerateMoveList(jsonRecord));
                drawCount++;

            } else {
                System.out.println("Found an incorrect record");
            }
        }
    }

    private HashMap<Integer, Integer> GenerateMoveList(JSONObject jsonRecord) throws JSONException, NumberFormatException {
        HashMap<Integer, Integer> moves;
        JSONArray moveList;
        moves = new HashMap<>();
        moveList = jsonRecord.getJSONArray("MoveList");
        for (Object o : moveList) {
            if (o instanceof JSONObject) {
                JSONObject jsonO = (JSONObject) o;
                Map<String, Object> move = jsonO.toMap();
                for (String key : move.keySet()) {
                    int selectedNumber = (int) move.get(key);
                    moves.put(Integer.parseInt(key), selectedNumber);
                }
            }
        }
        return moves;
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

    HashMap<Integer, HashMap<Integer, Integer>> GetWinResults() {
        return winResults;
    }
        HashMap<Integer, HashMap<Integer, Integer>> GetLoseResults() {
        return loseResults;
    }
            HashMap<Integer, HashMap<Integer, Integer>> GetDrawResults() {
        return drawResults;
    }

}
