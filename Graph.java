import java.util.ArrayList;
import java.util.Arrays;

import javax.management.ValueExp;
import javax.xml.namespace.QName;

import java.io.BufferedReader;
import java.io.FileReader;

public class Graph {
    
    public static void main(String[] args) throws Exception{
        
        //final BufferedReader deutschland = new BufferedReader(new FileReader("DataSets/germany.fmi"));
        // final BufferedReader stuttgart = new BufferedReader(new FileReader("DataSets/stgtregbz.txt"));
        final BufferedReader toy = new BufferedReader(new FileReader("DataSets/toy.txt"));
        final BufferedReader currentBufferedReader = toy;
        String currentLine;
        int numberOfNodes = 0;
        int numberOfEdges = 0;
        final long startTime = System.currentTimeMillis();
        final long beforeUsedMem = Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
        

        // ArrayList<Integer> nodeID = new ArrayList<>();
        // //ArrayList<String> nodeID2 = new ArrayList<>();
        // ArrayList<String> latitude = new ArrayList<>();
        // ArrayList<String> longitude = new ArrayList<>();

        // ArrayList<Integer> srcIDX = new ArrayList<>();
        // ArrayList<Integer> trgIDX = new ArrayList<>();
        // ArrayList<Integer> cost = new ArrayList<>();
        //ArrayList<String> type = new ArrayList<>();
        //ArrayList<String> maxspeed = new ArrayList<>();

        for(int i = 0;i <= 5; i++){
            if(i == 5){
                numberOfNodes = Integer.valueOf(currentBufferedReader.readLine());
                System.out.println("Number of nodes in graph: " + numberOfNodes);
                numberOfEdges = Integer.valueOf(currentBufferedReader.readLine());
                System.out.println("Number of edges in the graph: " + numberOfEdges);
            }
            else{
                currentBufferedReader.readLine();
            }
        }

        double[][] nodes = new double[2][numberOfNodes];

        for(int i = 0; i < numberOfNodes; i++) {
            currentLine = currentBufferedReader.readLine();
            String[] spiltLineNodes = currentLine.split(" ");
            int nodeID = (int) Integer.valueOf(spiltLineNodes[0]);
            double latitude = Double.valueOf(spiltLineNodes[2]);
            double longitude = Double.valueOf(spiltLineNodes[3]);
            nodes[0][nodeID] = latitude;
            nodes[1][nodeID] = longitude;
        }

        int[] edgeOffset = new int[numberOfNodes+1];
        Arrays.fill(edgeOffset, 0);
        int[][] edgeData = new int[3][numberOfEdges];

        for(int j = 0; j < numberOfEdges; j++) {
            currentLine = currentBufferedReader.readLine();
            String[] values = currentLine.split(" ");
            int srcNodeID = Integer.valueOf(values[0]);
            int trgNodeID = Integer.valueOf(values[1]);
            int cost = Integer.valueOf(values[2]);
            
            edgeOffset[srcNodeID]++;
            edgeData[0][j] = srcNodeID;
            edgeData[1][j] = trgNodeID;
            edgeData[2][j] = cost;
        }
        edgeOffset[0] = 0;
        currentBufferedReader.close();

        int curNumberOfEdges = 0;
        System.out.println("nodeID: " + 0 + " | offset: " + edgeOffset[0] + " | edges: " + 3);
        for(int i = 1; i <= numberOfNodes; i++) {
            curNumberOfEdges = edgeOffset[i-1];
            int x = edgeOffset[i];
            edgeOffset[i] += curNumberOfEdges;
            System.out.println("nodeID: " + i + " | offset: " + edgeOffset[i] + " | edges: " + x);
        }

        final long endTime = System.currentTimeMillis();
        long afterUsedMem = Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
        System.out.println("Time to create arrays: " + (endTime - startTime) + "ms");
        System.out.println("Memory usage: " + (afterUsedMem - beforeUsedMem) /1000000 + "MB");
    }
}
