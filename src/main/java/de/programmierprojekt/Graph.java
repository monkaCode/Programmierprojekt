package de.programmierprojekt;

import java.io.*;
import java.util.*;

import org.javatuples.Pair;

public class Graph {

    static double[][] nodesLatLon;
    static int[] edgeOffset;
    static int[][] edgeData;

    static double[] nodesLat;
    static int[] nodesLatIndex;

    static int numberOfNodes;
    private static int numberOfEdges;

    private static BufferedReader fileReader;

    public static void readGraph(String pathName) throws Exception {
        fileReader = new BufferedReader(new FileReader(pathName));
        skip();

        numberOfNodes = Integer.valueOf(fileReader.readLine());
        numberOfEdges = Integer.valueOf(fileReader.readLine());

        // System.out.println(numberOfEdges + " " + numberOfNodes);
        nodesLatLon = getNodesLatLon();

        edgeOffset = new int[numberOfNodes + 1];
        edgeData = new int[3][numberOfEdges];
        Arrays.fill(edgeOffset, 0);
        fillEdgeArrays();

        fileReader.close();
    }

    /**
     * Skips the first 5 Elements in file
     * 
     * @throws Exception
     */
    private static void skip() throws Exception {
        for (int i = 0; i < 5; i++) {
            fileReader.readLine();
        }
    }

    private static double[][] getNodesLatLon() throws Exception {
        double[][] result = new double[2][numberOfNodes];

        for (int i = 0; i < numberOfNodes; i++) {
            String line = fileReader.readLine();
            String[] spiltLineNodes = line.split(" ");

            int nodeID = Integer.valueOf(spiltLineNodes[0]);
            double latitude = Double.valueOf(spiltLineNodes[2]);
            double longitude = Double.valueOf(spiltLineNodes[3]);

            result[0][nodeID] = latitude;
            result[1][nodeID] = longitude;
        }
        return result;
    }

    private static void fillEdgeArrays() throws Exception {
        for (int i = 0; i < numberOfEdges; i++) {
            String line = fileReader.readLine();
            String[] values = line.split(" ");

            int srcNodeID = Integer.valueOf(values[0]);
            int trgNodeID = Integer.valueOf(values[1]);
            int cost = Integer.valueOf(values[2]);

            edgeOffset[srcNodeID]++;

            edgeData[0][i] = srcNodeID;
            edgeData[1][i] = trgNodeID;
            edgeData[2][i] = cost;
        }

        int curNumberOfEdges = 0;

        for (int i = 0; i <= numberOfNodes; i++) {
            if (i == 0) {
                curNumberOfEdges = edgeOffset[0];
                edgeOffset[0] = 0;
                // System.out.println("nodeID: " + 0 + " | offset: " + edgeOffset[0] + " |
                // edges: " + curNumberOfEdges);
            } else {
                int tmp = edgeOffset[i];
                edgeOffset[i] = edgeOffset[i - 1] + curNumberOfEdges;
                curNumberOfEdges = tmp;
                // System.out.println("nodeID: " + i + " | offset: " + edgeOffset[i] + " |
                // edges: " + tmp);
            }

        }
    }

    public static void prepareBinarySearch() {
        nodesLat = new double[numberOfNodes];
        nodesLatIndex = new int[numberOfNodes];

        for (int i = 0; i < numberOfNodes; i++) {
            nodesLat[i] = nodesLatLon[0][i];
            nodesLatIndex[i] = i;
        }
        sort(0, numberOfNodes - 1);
    }

    /**
     * Simple implementation of QuickSort
     * 
     * @param lowerBound
     * @param upperBound
     */
    private static void sort(int lowerBound, int upperBound) {
        if (upperBound > lowerBound) {
            int pivot = (lowerBound + upperBound) / 2;

            int newPivotPos = partition(lowerBound, upperBound, pivot);

            sort(lowerBound, newPivotPos - 1);
            sort(newPivotPos + 1, upperBound);

        }
    }

    private static int partition(int lowerBound, int upperBound, int positionPivot) {
        int newPivotPos = lowerBound;
        double pivotValue = nodesLat[positionPivot];

        swap(positionPivot, upperBound);
        for (int i = lowerBound; i < upperBound; i++) {
            if (nodesLat[i] < pivotValue) {
                swap(newPivotPos, i);
                newPivotPos++;
            }
        }
        swap(upperBound, newPivotPos);

        return newPivotPos;

    }

    private static void swap(int i, int j) {
        double temp = nodesLat[i];
        nodesLat[i] = nodesLat[j];
        nodesLat[j] = temp;

        int tempID = nodesLatIndex[i];
        nodesLatIndex[i] = nodesLatIndex[j];
        nodesLatIndex[j] = tempID;
    }

    /**
     * 
     * @return nodeID of the nearest node in same latitude
     */
    public static Pair<Integer, Integer> binarySearch(double latitude, int lowerBound, int upperBound) {

        if (upperBound == lowerBound) {
            return new Pair<Integer, Integer>(nodesLatIndex[upperBound], upperBound);
        }

        if (upperBound - lowerBound <= 1) {
            double distanceUpper = (double) Math.round((nodesLat[upperBound] - latitude) * 100000000d) / 100000000d;
            double distanceLower = (double) Math.round((latitude - nodesLat[lowerBound]) * 100000000d) / 100000000d;
            if (distanceUpper >= distanceLower) {
                return new Pair<Integer, Integer>(nodesLatIndex[lowerBound], lowerBound);
            } else {
                return new Pair<Integer, Integer>(nodesLatIndex[upperBound], upperBound);
            }
        }

        int median = (upperBound + lowerBound) / 2;

        if (latitude == nodesLat[median]) {
            // exakt der knoten mit dem selben Breitengrad
            return new Pair<Integer, Integer>(nodesLatIndex[median], median);
        } else if (latitude > nodesLat[median]) {
            return binarySearch(latitude, median, upperBound);
        } else {
            return binarySearch(latitude, lowerBound, median);
        }
    }

    public static int findClosestNode(double latitude, double longitude) {
        Pair<Integer, Integer> binarySearchResult = binarySearch(latitude, 0, nodesLat.length - 1);
        int currentClosestNode = binarySearchResult.getValue0();
        int nodesLatCCNIndex = binarySearchResult.getValue1();

        double maxDistance = Arithmetic.calcEuclideanDistance(
                nodesLatLon[0][currentClosestNode], nodesLatLon[1][currentClosestNode],
                latitude, longitude);

        int x = 1;
        while ((nodesLatCCNIndex - x >= 0 && nodesLatCCNIndex + x < nodesLatIndex.length) &&
                (latitude - nodesLat[nodesLatCCNIndex - x] > maxDistance
                        || nodesLat[nodesLatCCNIndex + x] - latitude > maxDistance)) {
            double currentLeftDistance = Arithmetic.calcEuclideanDistance(latitude, longitude,
                    getLatitude(nodesLatIndex[nodesLatCCNIndex - x]),
                    getLongitude(nodesLatIndex[nodesLatCCNIndex - x]));
            double currentRightDistance = Arithmetic.calcEuclideanDistance(latitude, longitude,
                    getLatitude(nodesLatIndex[nodesLatCCNIndex + x]),
                    getLongitude(nodesLatIndex[nodesLatCCNIndex + x]));

            if (currentLeftDistance < maxDistance) {
                maxDistance = currentLeftDistance;
                currentClosestNode = nodesLatIndex[nodesLatCCNIndex - x];
            } else if (currentRightDistance < maxDistance) {
                maxDistance = currentRightDistance;
                currentClosestNode = nodesLatIndex[nodesLatCCNIndex + x];
            }
            x++;
        }

        return currentClosestNode;

    }

    private static double getLatitude(int nodeID) {
        return nodesLatLon[0][nodeID];
    }

    private static double getLongitude(int nodeID) {
        return nodesLatLon[1][nodeID];
    }

}
