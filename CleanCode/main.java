package CleanCode;

public class main {
    public static void main(String[] args) throws Exception {
        final long startTime = System.currentTimeMillis();
        final long beforeUsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

        String pathName = "DataSets/stuttgart.fmi";
        Graph graph = new Graph();
        graph.createGraph(pathName);
        System.out.println("input data finished");

        BinarySearch bs = new BinarySearch(graph);
        bs.sort();

        final long endTime = System.currentTimeMillis();
        long afterUsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        System.out.println("Time to create arrays: " + (endTime - startTime) + "ms");
        System.out.println("Memory usage: " + (afterUsedMem - beforeUsedMem) / 1000000 + "MB");

    }
}
