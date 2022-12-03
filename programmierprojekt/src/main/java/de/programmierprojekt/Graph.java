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
     * @throws Exception
     */
    public static Pair<Integer, Integer> binarySearch(double latitude, int lowerBound, int upperBound)
            throws Exception {
        System.out.println("upper: " + upperBound + " lower: " + lowerBound);

        if (upperBound == lowerBound) {
            double distanceLeft = Math.abs(latitude - nodesLat[upperBound - 1]);
            double distanceMid = Math.abs(latitude - nodesLat[upperBound]);
            double distanceRight = Math.abs(latitude - nodesLat[upperBound + 1]);

            int currentMinIndex = 0;

            if (distanceLeft < distanceMid) {
                currentMinIndex = upperBound - 1;
                if (distanceLeft >= distanceRight) {
                    currentMinIndex = upperBound + 1;
                }
            } else {
                currentMinIndex = upperBound;
                if (distanceMid >= distanceRight) {
                    currentMinIndex = upperBound + 1;
                }
            }

            return new Pair<Integer, Integer>(nodesLatIndex[currentMinIndex], currentMinIndex);
        }

        // if (upperBound < lowerBound) {
        // double distanceUpper = Math.abs(latitude - nodesLat[upperBound]);
        // double distanceLower = Math.abs(latitude - nodesLat[lowerBound]);

        // return distanceLower > distanceUpper ? upperBound : lowerBound;
        // }

        int median = (upperBound + lowerBound) / 2;
        // System.out.println("latitude: " + latitude + "median: " + median + " val: " +
        // nodesLat[median]);
        Thread.sleep(100);

        if (latitude == nodesLat[median]) {
            // exakt der knoten mit dem selben Breitengrad
            return new Pair<Integer, Integer>(nodesLatIndex[median], median);
        } else if (latitude > nodesLat[median]) {
            return binarySearch(latitude, median + 1, upperBound);
        } else {
            return binarySearch(latitude, lowerBound, median - 1);
        }
    }

    public static int findClosestNode(double latitude, double longitude) throws Exception {
        int currentClosestNode = binarySearch(latitude, 0, nodesLat.length - 1).getValue0();

        double currentDistance = Arithmetic.calcEuclideanDistance(
                nodesLatLon[1][currentClosestNode], nodesLatLon[2][currentClosestNode],
                latitude, longitude);

        return 0;

    }

}
