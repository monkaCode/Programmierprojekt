package de.programmierprojekt.backend;

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
        double resultX = (latitude1 - latitude2) * (latitude1 - latitude2);
        double resultY = (longitude1 - longitude2) * (longitude1 - longitude2);

        return Math.sqrt(resultX + resultY);

    }
}
