package de.programmierprojekt;

import java.util.Comparator;
import java.util.PriorityQueue;

public class Dijkstra {

    static double[] costs = new double[Graph.numberOfNodes];
    static PriorityQueue<double[]> queue = new PriorityQueue<>(new Comparator<double[]>() {

        @Override
        public int compare(double[] o1, double[] o2) {
            if (o1[1] > o2[1]) {
                return 1;
            } else if (o1[1] < o2[1]) {
                return -1;
            } else {
                return 0;
            }
        }

    });

    // public static void main(String[] args) {
    // prepareDijkstra(5);
    // }

    public static void prepareDijkstra(int srcNodeID) {
        queue.clear();

        for (int i = 0; i < Graph.numberOfNodes; i++) {
            if (i == srcNodeID) {
                double[] r = { i, 0d };
                queue.add(r);

            } else {
                double[] r = { i, Double.POSITIVE_INFINITY };
                queue.add(r);
            }
        }
    }

    public static void oneToAllDijkstra(int srcNodeID) {
        while (!queue.isEmpty()) {
            double[] current = queue.poll();
            int currentNodeID = (int) current[0];
            double cost = current[1];

        }
    }

}
