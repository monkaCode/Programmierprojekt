package de.programmierprojekt;

public class Arithmetic {

    /**
     * @param latitude1  of Node 1
     * @param longitude1 of Node 1
     * @param latitude2  of Node 2
     * @param longitude2 of Node 2
     * @return Distance between Node 1 and 2
     */
    public static double calcEuclideanDistance(double latitude1, double longitude1, double latitude2,
            double longitude2) {
        double resultX = Math.pow(latitude1 - latitude2, 2);
        double resultY = Math.pow(longitude1 - longitude2, 2);

        return Math.sqrt(resultX + resultY);

    }
}
