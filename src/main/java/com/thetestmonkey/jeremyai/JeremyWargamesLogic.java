/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thetestmonkey.jeremyai;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author TTGAHX
 */
public class JeremyWargamesLogic {

    private ArrayList<Integer> _availableNumbers;
    private int _move;
    private HashMap<Integer, HashMap<Integer, Integer>> _winResults;
    private HashMap<Integer, HashMap<Integer, Integer>> _loseResults;
    private HashMap<Integer, HashMap<Integer, Integer>> _drawResults;
    private HashMap<Integer, String> _wargamesBoard;

    public void SetAvailableNumbers(ArrayList<Integer> availableNumbers) {
        _availableNumbers = availableNumbers;
    }

    public void SetMove(int move) {
        _move = move;
    }

    public void SetWinResults(HashMap<Integer, HashMap<Integer, Integer>> winResults) {
        _winResults = winResults;

    }

    public void SetLoseResults(HashMap<Integer, HashMap<Integer, Integer>> loseResults) {
        _loseResults = loseResults;

    }

    public void SetDrawResults(HashMap<Integer, HashMap<Integer, Integer>> drawResults) {
        _drawResults = drawResults;
    }

    public void SetWargamesBoard(HashMap<Integer, String> wargamesBoard) {
        _wargamesBoard = wargamesBoard;
    }

    int DetermineBestNextMove() {

        int selectedNumber = 0;

        selectedNumber = CheckForWinningMove();
        if (selectedNumber == 0) {
            selectedNumber = CheckForNextBestMove();
        }

        return selectedNumber;

    }

    private int CheckForNextBestMove() {
        //TODO: Count Highest Winning Move for this Move
        //TODO: Count Highest Losing Move remove from Array
        //TODO: If Highest Winning move does not exists choose Highest Draw Move
        HashMap<Integer, Integer> countOfWinningMoves = new HashMap<>();
        HashMap<Integer, Integer> countOfLosingMoves = new HashMap<>();
        HashMap<Integer, Integer> countOfDrawingMoves = new HashMap<>();
        int selectedNumber = 0;
        try {
            for (int i : _availableNumbers) {
                int winCount = 0;
                int loseCount = 0;
                int drawCount = 0;
                for (Map.Entry<Integer, HashMap<Integer, Integer>> moveList : _winResults.entrySet()) {
                    HashMap<Integer, Integer> moveSet = moveList.getValue();
                    int nextMove = moveSet.get(_move);
                    if (nextMove == i) {
                        winCount++;
                    }
                }
                countOfWinningMoves.put(i, winCount);
                for (Map.Entry<Integer, HashMap<Integer, Integer>> moveList : _loseResults.entrySet()) {
                    HashMap<Integer, Integer> moveSet = moveList.getValue();
                    int nextMove = moveSet.get(_move + 1);
                    if (nextMove == i) {
                        loseCount++;
                    }
                }
                countOfLosingMoves.put(i, loseCount);
                for (Map.Entry<Integer, HashMap<Integer, Integer>> moveList : _drawResults.entrySet()) {
                    HashMap<Integer, Integer> moveSet = moveList.getValue();
                    int nextMove = moveSet.get(_move);
                    if (nextMove == i) {
                        drawCount++;
                    }
                }
                countOfDrawingMoves.put(i, drawCount);
            }
            Map.Entry<Integer, Integer> maxEntry = null;
            if (!countOfLosingMoves.isEmpty()) {
                for (Map.Entry<Integer, Integer> entry : countOfLosingMoves.entrySet()) {
                    if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0) {
                        maxEntry = entry;
                    }
                }
                return selectedNumber = maxEntry.getKey();
            }

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
                return selectedNumber = maxEntry.getKey();
            }
        } catch (Exception e) {
            selectedNumber = _availableNumbers.get(ThreadLocalRandom.current().nextInt(0, _availableNumbers.size()));
        }
        return selectedNumber;
    }

    private int CheckForWinningMove() {
        int selectedNumber = 0;
        selectedNumber = CheckDiagonalsForWin();
        if (selectedNumber == 0) {
            selectedNumber = CheckColumnsForWin();
        }
        if (selectedNumber == 0) {
            selectedNumber = CheckRowsForWin();
        }

        return selectedNumber;
    }

    private int CheckDiagonalsForWin() {
        int bestPick = 0;
        if (_wargamesBoard.get(1).equals(_wargamesBoard.get(5))) {
            if ("O".equalsIgnoreCase(_wargamesBoard.get(1)) && !"X".equalsIgnoreCase(_wargamesBoard.get(9))) {
                bestPick = 9;

            }
        }
        if (_wargamesBoard.get(1).equals(_wargamesBoard.get(9))) {
            if ("O".equalsIgnoreCase(_wargamesBoard.get(1)) && !"X".equalsIgnoreCase(_wargamesBoard.get(5))) {
                bestPick = 5;

            }
        }
        if (_wargamesBoard.get(9).equals(_wargamesBoard.get(5))) {
            if ("O".equalsIgnoreCase(_wargamesBoard.get(5)) && !"X".equalsIgnoreCase(_wargamesBoard.get(1))) {
                bestPick = 1;
            }
        }

        if (_wargamesBoard.get(3).equals(_wargamesBoard.get(5))) {
            if ("O".equalsIgnoreCase(_wargamesBoard.get(3)) && !"X".equalsIgnoreCase(_wargamesBoard.get(7))) {
                bestPick = 7;

            }
        }
        if (_wargamesBoard.get(3).equals(_wargamesBoard.get(7))) {
            if ("O".equalsIgnoreCase(_wargamesBoard.get(7)) && !"X".equalsIgnoreCase(_wargamesBoard.get(5))) {
                bestPick = 5;

            }
        }
        if (_wargamesBoard.get(7).equals(_wargamesBoard.get(5))) {
            if ("O".equalsIgnoreCase(_wargamesBoard.get(5)) && !"X".equalsIgnoreCase(_wargamesBoard.get(3))) {
                bestPick = 3;
            }
        }

        return bestPick;
    }

    private int CheckColumnsForWin() {
        int bestPick = 0;
        for (int i = 1; i <= 3; i++) {
            int firstSpace = i;
            int secondSpace = i + 3;
            int thirdSpace = i + 6;
            if (_wargamesBoard.get(firstSpace).equals(_wargamesBoard.get(secondSpace)) && !"X".equalsIgnoreCase(_wargamesBoard.get(thirdSpace))) {
                if ("O".equalsIgnoreCase(_wargamesBoard.get(firstSpace))) {
                    bestPick = thirdSpace;

                }
            }
            if (_wargamesBoard.get(firstSpace).equals(_wargamesBoard.get(thirdSpace)) && !"X".equalsIgnoreCase(_wargamesBoard.get(secondSpace))) {
                if ("O".equalsIgnoreCase(_wargamesBoard.get(firstSpace))) {
                    bestPick = secondSpace;

                }
            }
            if (_wargamesBoard.get(thirdSpace).equals(_wargamesBoard.get(secondSpace)) && !"X".equalsIgnoreCase(_wargamesBoard.get(firstSpace))) {
                if ("O".equalsIgnoreCase(_wargamesBoard.get(secondSpace))) {
                    bestPick = firstSpace;

                }
            }
        }

        return bestPick;
    }

    private int CheckRowsForWin() {

        int bestPick = 0;

        for (int i = 0; i < 9; i += 3) {
            int firstSpace = i + 1;
            int secondSpace = i + 2;
            int thirdSpace = i + 3;
            if (_wargamesBoard.get(firstSpace).equals(_wargamesBoard.get(secondSpace)) && !"X".equalsIgnoreCase(_wargamesBoard.get(thirdSpace))) {
                if ("O".equalsIgnoreCase(_wargamesBoard.get(firstSpace))) {
                    bestPick = thirdSpace;

                }
            }
            if (_wargamesBoard.get(firstSpace).equals(_wargamesBoard.get(thirdSpace)) && !"X".equalsIgnoreCase(_wargamesBoard.get(secondSpace))) {
                if ("O".equalsIgnoreCase(_wargamesBoard.get(firstSpace))) {
                    bestPick = secondSpace;

                }
            }
            if (_wargamesBoard.get(thirdSpace).equals(_wargamesBoard.get(secondSpace)) && !"X".equalsIgnoreCase(_wargamesBoard.get(firstSpace))) {
                if ("O".equalsIgnoreCase(_wargamesBoard.get(secondSpace))) {
                    bestPick = firstSpace;

                }
            }
        }
        return bestPick;
    }

}
