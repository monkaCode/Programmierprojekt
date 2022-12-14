package de.programmierprojekt;

import java.io.*;
import java.util.*;

import org.javatuples.Pair;

public class Graph {

    /**
     * nodesLat[0][X] gives Latitude of node with ID X
     * nodesLat[1][X] gives Longitude of node with ID X
     */
    private static double[][] nodesLatLon;

    /**
     * edgeOffset[X] gives the index from which the node with
     * ID X hast the first edge in edgeData
     * 
     * edgeData[0][X] gives the sourceNodeID from edge with index X
     * edgeData[1][X] gives the targetNodeID from edge with index X
     * edgeData[2][X] gives the cost of this edge with index X
     */
    private static int[] edgeOffset;
    private static int[][] edgeData;

    /**
     * nodesLat[] sorted Array with all the Latitude values of the nodes
     * nodesLatIndex[X] gives the nodeID from the latitude value of nodeLat[X]
     */
    private static double[] nodesLat;
    private static int[] nodesLatIndex;

    private static int numberOfNodes;
    private static int numberOfEdges;

    private static BufferedReader fileReader;

    public static void readGraph(String pathName) throws Exception {
        fileReader = new BufferedReader(new FileReader(pathName));

        skip(5);

        numberOfNodes = Integer.valueOf(fileReader.readLine());
        numberOfEdges = Integer.valueOf(fileReader.readLine());

        nodesLatLon = new double[2][numberOfNodes];
        fillNodesLatLon();

        edgeOffset = new int[numberOfNodes + 1];
        edgeData = new int[3][numberOfEdges];
        Arrays.fill(edgeOffset, 0);
        fillEdgeArrays();

        fileReader.close();
    }

    /**
     * Skips the next n elements of the list
     * 
     * @param n
     * @throws Exception
     */
    private static void skip(int n) throws Exception {
        for (int i = 0; i < n; i++) {
            fileReader.readLine();
        }
    }

    private static void fillNodesLatLon() throws Exception {
        for (int i = 0; i < numberOfNodes; i++) {
            String line = fileReader.readLine();

            // Format of the File: nodeID nodeID2 latitude longitude elevation
            String[] spiltLineNodes = line.split(" ");

            int nodeID = Integer.valueOf(spiltLineNodes[0]);
            double latitude = Double.valueOf(spiltLineNodes[2]);
            double longitude = Double.valueOf(spiltLineNodes[3]);

            nodesLatLon[0][nodeID] = latitude;
            nodesLatLon[1][nodeID] = longitude;
        }
    }

    private static void fillEdgeArrays() throws Exception {
        for (int i = 0; i < numberOfEdges; i++) {
            String line = fileReader.readLine();

            // Format of the File: srcNodeID trgNodeID cost type maxspeed
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
            } else {
                int tmp = edgeOffset[i];
                edgeOffset[i] = edgeOffset[i - 1] + curNumberOfEdges;
                curNumberOfEdges = tmp;
            }

        }
    }

    /**
     * Fills and sorts nodeLat[],
     * fills nodeLatIndex[]
     */
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
     */
    private static void sort(int lowerBound, int upperBound) {
        if (upperBound > lowerBound) {
            int pivot = (lowerBound + upperBound) / 2;

            int newPivotPos = partition(lowerBound, upperBound, pivot);

            sort(lowerBound, newPivotPos - 1);
            sort(newPivotPos + 1, upperBound);

        }
    }

    /**
     * Helper funtion for Quicksort
     */
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

    /**
     * Helper function for Quicksort
     */
    private static void swap(int i, int j) {
        double temp = nodesLat[i];
        nodesLat[i] = nodesLat[j];
        nodesLat[j] = temp;

        int tempID = nodesLatIndex[i];
        nodesLatIndex[i] = nodesLatIndex[j];
        nodesLatIndex[j] = tempID;
    }

    /**
     * nodeID of the nearest node in same latitude
     * 
     * @return Pair<Left, Right>:
     *         Left := NodeID of the Node which latitude value is the closest to the
     *         searched latitude value
     *         Right := Index of the Node in nodesLat[]
     */
    public static Pair<Integer, Integer> binarySearch(double latitude, int lowerBound, int upperBound) {

        if (upperBound == lowerBound) {
            return new Pair<Integer, Integer>(nodesLatIndex[upperBound], upperBound);
        }

        if (upperBound - lowerBound <= 1) {
            double precision = 100000000d;

            double distanceUpper = Math.round((nodesLat[upperBound] - latitude) * precision) / precision;
            double distanceLower = Math.round((latitude - nodesLat[lowerBound]) * precision) / precision;

            // take new latitude value closer to searched latitude value
            if (distanceUpper >= distanceLower) {
                return new Pair<Integer, Integer>(nodesLatIndex[lowerBound], lowerBound);
            } else {
                return new Pair<Integer, Integer>(nodesLatIndex[upperBound], upperBound);
            }
        }

        int median = (upperBound + lowerBound) / 2;

        if (latitude == nodesLat[median]) {
            return new Pair<Integer, Integer>(nodesLatIndex[median], median);
        } else if (latitude > nodesLat[median]) {
            return binarySearch(latitude, median, upperBound);
        } else {
            return binarySearch(latitude, lowerBound, median);
        }
    }

    public static int findClosestNode(double latitude, double longitude) {
        Pair<Integer, Integer> binarySearchResult = Graph.binarySearch(latitude, 0, Graph.nodesLat.length - 1);

        int currentNodeIndex = binarySearchResult.getValue0();
        int nodeSortedLatIndex = binarySearchResult.getValue1();

        double latCurrent = nodesLatLon[0][currentNodeIndex];
        double lonCurrent = nodesLatLon[1][currentNodeIndex];

        double maxDistance = Arithmetic.calcEuclideanDistance(
                latCurrent, lonCurrent,
                latitude, longitude);

        /**
         * checking whether there is a node with higher latitude value
         * that is closer to the given lat and long values
         */
        for (int i = 1; nodeSortedLatIndex - i >= 0; i++) {
            int leftIndex = nodeSortedLatIndex - i;

            /**
             * if distance between the longtitude of the given coordinate
             * and the longitude of the current searched node is higher than
             * maxDistance then this node cannot be the closest node
             */
            if (Math.abs(getLongitude(nodesLatIndex[leftIndex]) - longitude) > maxDistance) {
                continue;
            }

            /**
             * if the distance between the latitude of the given coordinate
             * and the latitude of the current searched node is higher than maxDistance
             * then there is no need to look further
             */
            if (latitude - nodesLat[leftIndex] > maxDistance) {
                break;
            }

            double currentLeftDistance = Arithmetic.calcEuclideanDistance(latitude, longitude,
                    getLatitude(nodesLatIndex[leftIndex]), getLongitude(nodesLatIndex[leftIndex]));

            if (currentLeftDistance < maxDistance) {
                maxDistance = currentLeftDistance;
                currentNodeIndex = nodesLatIndex[leftIndex];
            }
        }

        /**
         * checking whether there is a node with lower latitude value
         * that is closer to the given lat and long values
         */
        for (int i = 1; nodeSortedLatIndex + i < nodesLatIndex.length; i++) {
            int rightIndex = nodeSortedLatIndex + i;

            /**
             * if distance between the longtitude of the given coordinate
             * and the longitude of the current searched node is higher than
             * maxDistance then this node cannot be the closest node
             */
            if (Math.abs(getLongitude(nodesLatIndex[rightIndex]) - longitude) > maxDistance) {
                continue;
            }
            /**
             * if the distance between the latitude of the given coordinate
             * and the latitude of the current searched node is higher than maxDistance
             * then there is no need to look further
             */

            if (nodesLat[rightIndex] - latitude > maxDistance) {
                break;
            }

            double currentRightDistance = Arithmetic.calcEuclideanDistance(latitude, longitude,
                    getLatitude(nodesLatIndex[rightIndex]), getLongitude(nodesLatIndex[rightIndex]));

            if (currentRightDistance < maxDistance) {
                maxDistance = currentRightDistance;
                currentNodeIndex = nodesLatIndex[rightIndex];
            }
        }
        return currentNodeIndex;
    }

    public static double getLatitude(int nodeID) {
        return Graph.nodesLatLon[0][nodeID];
    }

    public static double getLongitude(int nodeID) {
        return Graph.nodesLatLon[1][nodeID];
    }

    public static int getNumberOfNodes() {
        return Graph.numberOfNodes;
    }

    public static int getNumberOfEdges(int nodeID) {
        return Graph.edgeOffset[nodeID + 1] - Graph.edgeOffset[nodeID];
    }

    public static int getOffsetForEdges(int nodeID) {
        return Graph.edgeOffset[nodeID];
    }

    public static int getTargetNodeID(int edgeID) {
        return Graph.edgeData[1][edgeID];
    }

    public static int getCost(int edgeID) {
        return Graph.edgeData[2][edgeID];
    }

}
