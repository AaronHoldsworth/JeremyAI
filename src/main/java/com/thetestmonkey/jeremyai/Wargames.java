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
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author TTGAHX
 */
public class Wargames {

    HashMap<Integer, String> wargamesBoard;
    BufferedReader br;
    JeremyAIMain mainClass;
    private int turn = 1;

    public Wargames() {
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

        br = new BufferedReader(new InputStreamReader(System.in));
        BeginGame();
    }

    public void BeginGame() {
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
        if (turn == 1) {
            System.out.println("Please select a number from those available");
            HumansTurn();
        } else if (turn == 0) {
            JeremysTurn();
        } else {
            EndGame();
        }

    }

    private void HumansTurn() {

        try {
            int selectedNumber = Integer.parseInt(br.readLine());
            if (selectedNumber < 1 || selectedNumber > 9) {
                System.out.println("Please enter a valid number");
                System.out.println();
                HumansTurn();
            }
            if ("X".equalsIgnoreCase(wargamesBoard.get(selectedNumber)) || "O".equalsIgnoreCase(wargamesBoard.get(selectedNumber))) {
                System.out.println("Please select a number that hasn't already been picked.");
                System.out.println();
                HumansTurn();
            }
            wargamesBoard.put(selectedNumber, "X");

            DrawWargamesBoard();
            turn = 0;
            DecideWhoseTurn();
        } catch (IOException | NumberFormatException e) {
            System.out.println("Please enter a valid number");
            System.out.println();
            HumansTurn();
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
        int selectedNumber = availableNumbers.get(ThreadLocalRandom.current().nextInt(0, availableNumbers.size()));

        wargamesBoard.put(selectedNumber, "O");
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
                System.out.println("You Win! Congratulations!");

            } else {
                System.out.println("I Win!!");

            }
            turn = 2;

        }
        if (wargamesBoard.get(3).equals(wargamesBoard.get(5)) && wargamesBoard.get(5).equals(wargamesBoard.get(7))) {
            if ("X".equalsIgnoreCase(wargamesBoard.get(3))) {
                System.out.println("You Win! Congratulations!");

            } else {
                System.out.println("I Win!!");

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
                    System.out.println("You Win! Congratulations!");

                } else {
                    System.out.println("I Win!!");

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
                    System.out.println("You Win! Congratulations!");

                } else {
                    System.out.println("I Win!!");

                }
                turn = 2;
            }
        }
    }

    private void EndGame() {
        System.out.println("That Was Fun. Let me commit this to Memory");
    }

    private boolean CheckForDraw() {

        ArrayList<Integer> availableNumbers = new ArrayList<>();
        for (int i = 1; i <= 9; i++) {
            String currentValue = wargamesBoard.get(i);
            if (!"x".equalsIgnoreCase(currentValue) && !"O".equalsIgnoreCase(currentValue)) {
                availableNumbers.add(i);
            }
        }
        if (availableNumbers.size() == 0) {
            System.out.println("It's a draw!");
            System.out.println();
            turn = 2;
            return true;
        }
        return false;
    }

}
