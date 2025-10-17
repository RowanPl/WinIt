package nl.datavortex.winit.helper;

import java.lang.Math;


//TODO Make this class useable within the application

public class EloRating {

    // Function to calculate the Probability
    public static double Probability(double rating1, double rating2) {
        // Calculate and return the expected score
        return 1.0 / (1 + Math.pow(10, (rating1 - rating2) / 400.0));
    }

    // Function to calculate Elo rating
    // K is a constant.
    // outcome determines the outcome: 1 for Player A win, 0 for Player B win, 0.5 for draw.
    public static void EloRating(double Ra, double Rb, int K, double outcome) {
        // Calculate the Winning Probability of Player B
        double Pb = Probability(Ra, Rb);

        // Calculate the Winning Probability of Player A
        double Pa = Probability(Rb, Ra);

        // Update the Elo Ratings
        Ra = Ra + K * (outcome - Pa);
        Rb = Rb + K * ((1 - outcome) - Pb);

        // Print updated ratings
        System.out.println("Updated Ratings:-");
        System.out.println("Ra = " + Ra + " Rb = " + Rb);
    }

    public static void main(String[] args) {
        // Current ELO ratings
        double Ra = 1200, Rb = 1000;

        // K is a constant
        int K = 30;

        // Outcome: 1 for Player A win, 0 for Player B win, 0.5 for draw
        double outcome = 1;

        // Function call
        EloRating(Ra, Rb, K, outcome);
    }
}