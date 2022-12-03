package de.programmierprojekt;

public class main {
    public static void main(String[] args) throws Exception {
        final long startTime = System.currentTimeMillis();
        final long beforeUsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

        String pathName = "DataSets/toy.txt";
        Graph graph = new Graph();
        graph.readGraph(pathName);

        System.out.println("Reading complete, starting data structure");
        graph.prepareBinarySearch();

        // System.out.println(graph.binarySearch(48.9d, 0, graph.nodesLat.length - 1));
        System.out.println(graph.binarySearch(49.0d, 0, graph.nodesLat.length - 1));
        System.out.println(graph.binarySearch(49.005d, 0, graph.nodesLat.length - 1));
        System.out.println(graph.binarySearch(49.006d, 0, graph.nodesLat.length - 1));
        System.out.println(graph.binarySearch(49.30d, 0, graph.nodesLat.length - 1));
        System.out.println(graph.binarySearch(49.03d, 0, graph.nodesLat.length - 1));
        System.out.println(graph.binarySearch(49.033d, 0, graph.nodesLat.length - 1));

        final long endTime = System.currentTimeMillis();
        long afterUsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        System.out.println("Time to create arrays: " + (endTime - startTime) + "ms");
        System.out.println("Memory usage: " + (afterUsedMem - beforeUsedMem) / 1000000 + "MB");

    }
}
