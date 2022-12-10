package de.programmierprojekt;

import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

import org.javatuples.Pair;

public class main {
    public static void main(String[] args) throws Exception {
        final long startTime = System.currentTimeMillis();
        final long beforeUsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        String pathName = "DataSets/toy.fmi";

        Graph.readGraph(pathName);
        System.out.println("graph read finished");

        final long endTime = System.currentTimeMillis();
        System.out.println("Time to create arrays: " + (endTime - startTime) + "ms");

        System.out.println("Reading complete, starting data structure");
        Graph.prepareBinarySearch();
        System.out.println("Preparing finished!");

        // Pair<Integer, Integer> res1 = Graph.binarySearch(48.012d, 0, Graph.nodesLat.length - 1);
        // Pair<Integer, Integer> res2 = Graph.binarySearch(49.02d, 0, Graph.nodesLat.length - 1);
        // Pair<Integer, Integer> res3 = Graph.binarySearch(50.14422d, 0, Graph.nodesLat.length - 1);
        // Pair<Integer, Integer> res4 = Graph.binarySearch(48.012, 0, Graph.nodesLat.length - 1);

        final long findStart1 = System.currentTimeMillis();
        Dijkstra.prepareDijkstra(2);
        System.out.println("Dijsktra is prepared");
        Dijkstra.oneToAllDijkstra(2);
        final long findEnd1 = System.currentTimeMillis();
        System.out.println("Find did take: " + (findEnd1 - findStart1)/1000 + " s");

        // System.out.println(Arrays.toString(Dijkstra.queue));
        // System.out.println(Arrays.toString(Dijkstra.queueIndex));
        // System.out.println(Arrays.toString(Dijkstra.queueNodeIndex));

        // final long findStart = System.currentTimeMillis();
        // System.out.println(Graph.findClosestNode(49.02d, 10.045d, res2));
        // final long findEnd = System.currentTimeMillis();
        // System.out.println("Find did take: " + (findEnd - findStart) + " ms");

        // final long findStart2 = System.currentTimeMillis();
        // System.out.println(Graph.findClosestNode(50.14422d, 8.045d, res3));
        // final long findEnd2 = System.currentTimeMillis();
        // System.out.println("Find did take: " + (findEnd2 - findStart2) + " ms");

        // final long findStart3 = System.currentTimeMillis();
        // System.out.println(Graph.findClosestNode(48.012d, 9.045d, res4));
        // final long findEnd3 = System.currentTimeMillis();
        // System.out.println("Find did take: " + (findEnd3 - findStart3) + " ms");

        // System.out.println(Graph.findClosestNode(48.3, 8.8d));
        // System.out.println(Graph.findClosestNode(48.9d, 11.9d));
        // System.out.println(Graph.findClosestNode(49.6d, 10.1d));
        // System.out.println(Graph.findClosestNode(49.8d, 7.5d));
        // System.out.println(Graph.findClosestNode(50.5d, 11.5d));
        // System.out.println(Graph.findClosestNode(51.2d, 14.5d));
        // System.out.println(Graph.findClosestNode(51.3d, 8.6d));
        // System.out.println(Graph.findClosestNode(51.7d, 11.8d));
        // System.out.println(Graph.findClosestNode(52.4d, 8.3d));
        // System.out.println(Graph.findClosestNode(53.8d, 12.6d));
        // System.out.println(Graph.findClosestNode(54.1d, 9.7d));

        long afterUsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        System.out.println("Memory usage: " + (afterUsedMem - beforeUsedMem) / 1000000 + "MB");

    }
}
