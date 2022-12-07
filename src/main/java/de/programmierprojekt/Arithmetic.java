package de.programmierprojekt;

public class Arithmetic {

    /**
     * @param latitude1  of Node X
     * @param longitude1 of Node X
     * @param latitude2  of Node Y
     * @param longitude2 of Node Y
     * @return Distance between Node X and Y
     */
    public static double calcEuclideanDistance(double latitude1, double longitude1, double latitude2,
            double longitude2) {
        double resultX = Math.pow(latitude1 - latitude2, 2);
        double resultY = Math.pow(longitude1 - longitude2, 2);

        return Math.sqrt(resultX + resultY);

    }
}
