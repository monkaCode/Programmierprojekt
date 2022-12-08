package de.programmierprojekt;

public class main {
    public static void main(String[] args) throws Exception {
        final long startTime = System.currentTimeMillis();
        final long beforeUsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

        String pathName = "DataSets/toy.fmi";

        Graph.readGraph(pathName);

        System.out.println("Reading complete, starting data structure");
        Graph.prepareBinarySearch();

        // System.out.println(Graph.findClosestNode(49.01, 10.020d));
        System.out.println(Graph.findClosestNode(49.02d, 10.045d));
        // System.out.println(Graph.findClosestNode(49.016d, 10.045d));
        // System.out.println(Graph.findClosestNode(49.056d, 10.045d));
        // System.out.println(Graph.findClosestNode(49.0123d, 10.045d));
        // System.out.println(Graph.findClosestNode(49.02222d, 10.045d));
        // System.out.println(Graph.findClosestNode(49.04222d, 10.045d));
        // System.out.println(Graph.findClosestNode(49.04422d, 10.045d));

        final long endTime = System.currentTimeMillis();
        long afterUsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        System.out.println("Time to create arrays: " + (endTime - startTime) + "ms");
        System.out.println("Memory usage: " + (afterUsedMem - beforeUsedMem) / 1000000 + "MB");

    }
}
