package CleanCode;

import java.io.*;
import java.util.Arrays;

public class Graph {

    static double[][] nodesLatLon;
    static int[] edgeOffset;
    static int[][] edgeData;
    private int numberOfNodes;
    private int numberOfEdges;

    private final BufferedReader fileReader;

    public Graph(String pathName) throws Exception {
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
    private void skip() throws Exception {
        for (int i = 0; i < 5; i++) {
            fileReader.readLine();
        }
    }

    private void fillEdgeArrays() throws Exception {
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

    private double[][] getNodesLatLon() throws Exception {
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

}
