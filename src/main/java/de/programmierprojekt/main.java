package de.programmierprojekt;

public class main {
    public static void main(String[] args) throws Exception {
        final long startTime = System.currentTimeMillis();
        final long beforeUsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

        String pathName = "DataSets/toy.fmi";
        Graph graph = new Graph();
        graph.readGraph(pathName);

        System.out.println("Reading complete, starting data structure");
        graph.prepareBinarySearch();

        System.out.println(graph.findClosestNode(49.045d, 10.045d));
        

        final long endTime = System.currentTimeMillis();
        long afterUsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        System.out.println("Time to create arrays: " + (endTime - startTime) + "ms");
        System.out.println("Memory usage: " + (afterUsedMem - beforeUsedMem) / 1000000 + "MB");

    }
}
