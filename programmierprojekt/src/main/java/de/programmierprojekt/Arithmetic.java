package de.programmierprojekt;

public class Arithmetic {

    public static double calcEuclideanDistance(double latitude1, double longitude1, double latitude2,
            double longitude2) {
        double resultX = Math.pow(latitude1 - latitude2, 2);
        double resultY = Math.pow(longitude1 - longitude2, 2);

        return Math.sqrt(resultX + resultY);

    }

    // public static int min(int a, int b, int c) {

    // }

}
