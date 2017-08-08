/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thetestmonkey.jeremyai;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author TTGAHX
 */
public class Wargames {

    HashMap<Integer, String> wargamesBoard;
    BufferedReader br;
    private HashMap<Integer, Integer> moves;
    private String result;
    private final MongoWargamesConnector mwc;
    private int turn = 1;
    private int move = 1;
    private HashMap<Integer, HashMap<Integer, Integer>> _winResults = new HashMap<>();
    private HashMap<Integer, HashMap<Integer, Integer>> _loseResults = new HashMap<>();
    private HashMap<Integer, HashMap<Integer, Integer>> _drawResults = new HashMap<>();

    public Wargames() {
        this.mwc = new MongoWargamesConnector();
        this.moves = new HashMap<>();
        wargamesBoard = new HashMap<Integer, String>() {
            {
                put(1, "1");
                put(2, "2");
                put(3, "3");
                put(4, "4");
                put(5, "5");
                put(6, "6");
                put(7, "7");
                put(8, "8");
                put(9, "9");
            }
        };

        mwc.ConnectToMongoDatabse();
        br = new BufferedReader(new InputStreamReader(System.in));
        BeginGame();
    }

    public void BeginGame() {
        LoadPreviousResults();
        System.out.println("You are X. I am O.");
        DrawWargamesBoard();
        DecideWhoseTurn();
    }

    private void DrawWargamesBoard() {
        System.out.println();
        System.out.println(wargamesBoard.get(1) + "|" + wargamesBoard.get(2) + "|" + wargamesBoard.get(3));
        System.out.println("-|-|-");
        System.out.println(wargamesBoard.get(4) + "|" + wargamesBoard.get(5) + "|" + wargamesBoard.get(6));
        System.out.println("-|-|-");
        System.out.println(wargamesBoard.get(7) + "|" + wargamesBoard.get(8) + "|" + wargamesBoard.get(9));
    }

    private void DecideWhoseTurn() {

        DetermineIfWinner();
        switch (turn) {
            case 1:
                System.out.println("Please select a number from those available");
                OthersTurn();
                break;
            case 0:
                JeremysTurn();
                break;
            default:
                EndGame();
                break;
        }

    }

    private void OthersTurn() {

        try {
            int selectedNumber = Integer.parseInt(br.readLine());
            if (selectedNumber < 1 || selectedNumber > 9) {
                System.out.println("Please enter a valid number");
                System.out.println();
                OthersTurn();
            }
            if ("X".equalsIgnoreCase(wargamesBoard.get(selectedNumber)) || "O".equalsIgnoreCase(wargamesBoard.get(selectedNumber))) {
                System.out.println("Please select a number that hasn't already been picked.");
                System.out.println();
                OthersTurn();
            }
            wargamesBoard.put(selectedNumber, "X");

            moves.put(move, selectedNumber);
            move++;
            DrawWargamesBoard();
            turn = 0;
            DecideWhoseTurn();
        } catch (IOException | NumberFormatException e) {
            System.out.println("Please enter a valid number");
            System.out.println();
            OthersTurn();
        }
    }

    private void JeremysTurn() {

        ArrayList<Integer> availableNumbers = new ArrayList<>();

        for (int i = 1; i < 9; i++) {
            String currentValue = wargamesBoard.get(i);
            if (!"x".equalsIgnoreCase(currentValue) && !"O".equalsIgnoreCase(currentValue)) {
                availableNumbers.add(i);

            }
        }
        int selectedNumber = DetermineBestNextMove(availableNumbers);

        wargamesBoard.put(selectedNumber, "O");
        moves.put(move, selectedNumber);
        move++;
        DrawWargamesBoard();

        turn = 1;

        DecideWhoseTurn();
    }

    private void DetermineIfWinner() {
        boolean isDraw = CheckForDraw();
        if (!isDraw) {
            CheckRowsForWin();
            CheckColumnsForWin();
            CheckDiagonalsForWin();

        }
    }

    private void CheckDiagonalsForWin() {
        if (wargamesBoard.get(1).equals(wargamesBoard.get(5)) && wargamesBoard.get(5).equals(wargamesBoard.get(9))) {
            if ("X".equalsIgnoreCase(wargamesBoard.get(1))) {
                result = "Other";

            } else {
                result = "Jeremy";

            }
            turn = 2;

        }
        if (wargamesBoard.get(3).equals(wargamesBoard.get(5)) && wargamesBoard.get(5).equals(wargamesBoard.get(7))) {
            if ("X".equalsIgnoreCase(wargamesBoard.get(3))) {
                result = "Other";

            } else {
                result = "Jeremy";

            }
            turn = 2;

        }
    }

    private void CheckColumnsForWin() {
        for (int i = 1; i <= 3; i++) {
            int firstSpace = i;
            int secondSpace = i + 3;
            int thirdSpace = i + 6;
            if (wargamesBoard.get(firstSpace).equals(wargamesBoard.get(secondSpace)) && wargamesBoard.get(secondSpace).equals(wargamesBoard.get(thirdSpace))) {
                if ("X".equalsIgnoreCase(wargamesBoard.get(firstSpace))) {
                    result = "Other";

                } else {
                    result = "Jeremy";

                }
                turn = 2;
            }
        }
    }

    private void CheckRowsForWin() {

        for (int i = 0; i < 9; i += 3) {
            int firstSpace = i + 1;
            int secondSpace = i + 2;
            int thirdSpace = i + 3;
            if (wargamesBoard.get(firstSpace).equals(wargamesBoard.get(secondSpace)) && wargamesBoard.get(secondSpace).equals(wargamesBoard.get(thirdSpace))) {
                if ("X".equalsIgnoreCase(wargamesBoard.get(firstSpace))) {
                    result = "Other";

                } else {
                    result = "Jeremy";

                }
                turn = 2;
            }
        }
    }

    private boolean CheckForDraw() {

        ArrayList<Integer> availableNumbers = new ArrayList<>();

        CheckRowsForWin();
        CheckColumnsForWin();
        CheckDiagonalsForWin();

        if (!"".equals(result)) {
            return true;
        } else {
            for (int i = 1; i <= 9; i++) {
                String currentValue = wargamesBoard.get(i);
                if (!"x".equalsIgnoreCase(currentValue) && !"O".equalsIgnoreCase(currentValue)) {
                    availableNumbers.add(i);
                }
            }
            if (availableNumbers.isEmpty()) {
                result = "Draw";

                System.out.println();
                turn = 2;
                return true;
            }

            return false;
        }
    }

    private void EndGame() {
        mwc.GenerateResults(moves, result);

        if ("Other".equalsIgnoreCase(result)) {
            System.out.println("You Win! Congratulations");
        } else if ("Jeremy".equalsIgnoreCase(result)) {
            System.out.println("I Win!");
        } else if ("Draw".equalsIgnoreCase(result)) {
            System.out.println("It's a Draw!");
        } else {
            System.out.println("Something went peculiar");
        }

        System.out.println("That Was Fun. Let me commit this to Memory");
    }

    private void LoadPreviousResults() {
        mwc.QueryMongoCollection();
        _winResults = mwc.GetWinResults();
        _loseResults = mwc.GetLoseResults();
        _drawResults = mwc.GetDrawResults();
    }

    private int DetermineBestNextMove(ArrayList<Integer> availableNumbers) {

        //TODO: Count Highest Winning Move for this Move
        //TODO: Count Highest Losing Move remove from Array
        //TODO: If Highest Winning move does not exists choose Highest Draw Move
        HashMap<Integer, Integer> countOfWinningMoves = new HashMap<>();
        HashMap<Integer, Integer> countOfLosingMoves = new HashMap<>();
        HashMap<Integer, Integer> countOfDrawingMoves = new HashMap<>();

        int selectedNumber = 0;
        try {
            for (int i : availableNumbers) {
                int winCount = 0;
                int loseCount = 0;
                int drawCount = 0;

                for (Map.Entry<Integer, HashMap<Integer, Integer>> moveList : _winResults.entrySet()) {
                    HashMap<Integer, Integer> moveSet = moveList.getValue();
                    int nextMove = moveSet.get(move);
                    if (nextMove == i) {
                        winCount++;
                    }

                }
                countOfWinningMoves.put(i, winCount);
                for (Map.Entry<Integer, HashMap<Integer, Integer>> moveList : _loseResults.entrySet()) {
                    HashMap<Integer, Integer> moveSet = moveList.getValue();
                    int nextMove = moveSet.get(move);
                    if (nextMove == i) {
                        loseCount++;
                    }
                }
                countOfLosingMoves.put(i, loseCount);
                for (Map.Entry<Integer, HashMap<Integer, Integer>> moveList : _drawResults.entrySet()) {
                    HashMap<Integer, Integer> moveSet = moveList.getValue();
                    int nextMove = moveSet.get(move);
                    if (nextMove == i) {
                        drawCount++;
                    }
                }

                countOfDrawingMoves.put(i, drawCount);
            }

            Map.Entry<Integer, Integer> maxEntry = null;

            if (!countOfWinningMoves.isEmpty()) {
                for (Map.Entry<Integer, Integer> entry : countOfWinningMoves.entrySet()) {
                    if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0) {
                        maxEntry = entry;
                    }
                }
               return selectedNumber = maxEntry.getKey();
            }
            if (!countOfDrawingMoves.isEmpty()) {
                for (Map.Entry<Integer, Integer> entry : countOfDrawingMoves.entrySet()) {
                    if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0) {
                        maxEntry = entry;
                    }
                }
              return  selectedNumber = maxEntry.getKey();
            }
            if (!countOfLosingMoves.isEmpty()) {
                for (Map.Entry<Integer, Integer> entry : countOfLosingMoves.entrySet()) {
                    if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0) {
                        maxEntry = entry;
                    }
                }
               return selectedNumber = maxEntry.getKey();
            }
        } catch (Exception e) {
            selectedNumber = availableNumbers.get(ThreadLocalRandom.current().nextInt(0, availableNumbers.size()));
        }

        return selectedNumber;
    }

}
