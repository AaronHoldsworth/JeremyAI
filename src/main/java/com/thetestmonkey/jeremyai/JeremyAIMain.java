/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thetestmonkey.jeremyai;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author TTGAHX
 */
public class JeremyAIMain {

    static BufferedReader br;
    static Wargames wg;

    public static void main(String[] args) {
        br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Hello, my name is Jeremy.");
        System.out.println("Would you like to play a game?");
        StartJeremyAI();
    }

    private static void StartJeremyAI() {

        try {

            String response = br.readLine();

            if (response.equalsIgnoreCase("Yes")) {
                StartWargames();
            } else {
                System.exit(0);
            }
        } catch (IOException e) {
            System.out.println("HEART ATTACK.....");
            System.exit(4);
        }
    }

    private static void StartWargames() {
        wg = new Wargames();
        System.out.println("Would you like to play another game?");
        StartJeremyAI();
    }

}
