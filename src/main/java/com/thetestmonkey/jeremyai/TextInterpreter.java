/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thetestmonkey.jeremyai;

import java.util.HashMap;
import java.util.Locale;
import org.bson.Document;

/**
 *
 * @author TTGAHX
 */
class TextInterpreter {

    private HashMap<String, String> requirementParts = new HashMap<>();
    private MongoPersonasConnector mc = null;

    public TextInterpreter() {
        mc = new MongoPersonasConnector();
    }

    //Breaks down Requirements
    HashMap<String, String> InterpretRequirement(String input) {

        DetermineUser(input);

        return requirementParts;
    }

    //Find User in String
    //Searches for found user in Mongo
    private void DetermineUser(String input) {

        String beginning = "As A ";
        String end = " I Want";

        int asA = input.toLowerCase(Locale.US).indexOf(beginning.toLowerCase(Locale.US)) + beginning.length();
        int iWant = input.toLowerCase(Locale.US).indexOf(end.toLowerCase(Locale.US));
        requirementParts.put("User", input.substring(asA, iWant));

        //TODO: Fix User Search as it will not distinguish between need to create and update
        Document userDoc = UserSearch();

        if (userDoc == null) {
            PromptToCreateUser();
        }
    }

    //Searches for User and Returns Document if available.
    //TODO: Fix User Search as it will not distinguish between need to create and update
    private Document UserSearch() {

        Document userDoc = mc.CheckForUsers(requirementParts.get("User"));

        return userDoc;
    }

    //Will prompt user for create needs to handle Yes and No Responses
    private void PromptToCreateUser() {
        System.out.println("I cannot find a the user specified in your requirement. Would you like me to create a new one?");

        CreateUpdateUserDocument(true, requirementParts.get("User"), null);

    }

    //Creates or updates document based on whether a Master User has been found
    //Will need to also suggest best guess at some point
    private void CreateUpdateUserDocument(boolean createCustomer, String newUser, String masterUser) {
        if (createCustomer) {
            Document newRecord = new Document().append("userType", newUser).append("subUsers", null);
            String id = mc.CreateMongoDocument(newRecord);
        } else {
            mc.UpdateUserDocument(masterUser, newUser);
        }
    }
}
