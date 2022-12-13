package de.programmierprojekt;

import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

public class Dijkstra {

    private PriorityQueue<int[]> queue;
    private int[] costs;
    private boolean[] visited;

    public Dijkstra(int srcNodeID) {
        prepareDijkstra(srcNodeID);
    }

    private void prepareDijkstra(int srcNodeID) {
        queue = new PriorityQueue<>(new Comparator<int[]>() {

            @Override
            public int compare(int[] o1, int[] o2) {
                if (o1[1] > o2[1]) {
                    return 1;
                } else if (o1[1] < o2[1]) {
                    return -1;
                } else {
                    return 0;
                }
            }

        });

        visited = new boolean[Graph.numberOfNodes];
        Arrays.fill(visited, false);

        costs = new int[Graph.numberOfNodes];
        Arrays.fill(costs, Integer.MAX_VALUE);
        costs[srcNodeID] = 0;

        int[] srcNode = { srcNodeID, 0 };
        queue.add(srcNode);
    }

    public int[] oneToAllDijkstra() {

        while (!queue.isEmpty()) {
            int[] minNodeIDArr = queue.poll();
            int minNodeID = minNodeIDArr[0];

            if (visited[minNodeID]) {
                continue;
            }

            visited[minNodeID] = true;

            int numberOfEdges = Graph.edgeOffset[minNodeID + 1] - Graph.edgeOffset[minNodeID];
            int startOffset = Graph.edgeOffset[minNodeID];

            for (int i = 0; i < numberOfEdges; i++) {
                int targetNodeID = Graph.edgeData[1][startOffset + i];

                if (!visited[targetNodeID]) {

                    int edgeCost = Graph.edgeData[2][startOffset + i];
                    int calcDistance = costs[minNodeID] + edgeCost;

                    if (costs[targetNodeID] > calcDistance) {
                        costs[targetNodeID] = calcDistance;
                        int[] tmp = { targetNodeID, calcDistance };
                        queue.add(tmp);
                    }
                }
            }
        }
        return costs;
    }

    public int oneToOneDijkstra(int trgNodeID) throws Exception {
        while (!queue.isEmpty()) {
            int[] minNodeIDArr = queue.poll();
            int minNodeID = minNodeIDArr[0];

            if (minNodeID == trgNodeID) {
                return costs[trgNodeID];
            }

            if (visited[minNodeID]) {
                continue;
            }

            visited[minNodeID] = true;

            int numberOfEdges = Graph.edgeOffset[minNodeID + 1] - Graph.edgeOffset[minNodeID];
            int startOffset = Graph.edgeOffset[minNodeID];

            for (int i = 0; i < numberOfEdges; i++) {
                int targetNodeID = Graph.edgeData[1][startOffset + i];

                if (!visited[targetNodeID]) {

                    int edgeCost = Graph.edgeData[2][startOffset + i];
                    int calcDistance = costs[minNodeID] + edgeCost;

                    if (costs[targetNodeID] > calcDistance) {
                        costs[targetNodeID] = calcDistance;
                        int[] tmp = { targetNodeID, calcDistance };
                        queue.add(tmp);
                    }
                }
            }
        }
        return -1;
    }
}
