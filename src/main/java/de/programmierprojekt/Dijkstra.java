package de.programmierprojekt;

import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

public class Dijkstra {

    /**
     * Convention:
     * For every element "node" of queue:
     * node := {nodeID, cost}
     */
    private PriorityQueue<int[]> queue;

    private int[] costs;
    private boolean[] visited;

    public Dijkstra(int srcNodeID) {
        prepareDijkstra(srcNodeID);
    }

    private void prepareDijkstra(int srcNodeID) {
        /**
         * queue compares every int[] node := {nodeID, cost}
         * by the cost value
         */
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

        visited = new boolean[Graph.getNumberOfNodes()];
        Arrays.fill(visited, false);

        costs = new int[Graph.getNumberOfNodes()];
        Arrays.fill(costs, Integer.MAX_VALUE);

        costs[srcNodeID] = 0;
        int[] srcNode = { srcNodeID, 0 };
        queue.add(srcNode);
    }

    /**
     * @return costs[], where:
     *         cost[nodeID] := gives the distance from srcNodeID to nodeID if there
     *         ist a path available,
     *         cost[nodeID] := Integer.maxValue otherwise
     */
    public int[] oneToAllDijkstra() {

        while (!queue.isEmpty()) {
            int[] minNode = queue.poll();
            int minNodeID = minNode[0];

            if (visited[minNodeID]) {
                continue;
            }

            updateValues(minNodeID);
        }
        return costs;
    }

    public int oneToOneDijkstra(int trgNodeID) {

        while (!queue.isEmpty()) {

            int[] minNode = queue.poll();
            int minNodeID = minNode[0];

            if (minNodeID == trgNodeID) {
                return costs[trgNodeID];
            }

            if (visited[minNodeID]) {
                continue;
            }

            updateValues(minNodeID);

        }
        // if there is no way to the targetNode
        return -1;
    }

    /**
     * One Iteration of Dijkstras Algorithm for specific nodeID
     * 
     * @param minNodeID node with highest priority
     */
    private void updateValues(int minNodeID) {
        visited[minNodeID] = true;

        int numberOfEdges = Graph.getNumberOfEdges(minNodeID);
        int startOffset = Graph.getOffsetForEdges(minNodeID);

        for (int i = 0; i < numberOfEdges; i++) {
            int edgeID = startOffset + i;

            int targetNodeID = Graph.getTargetNodeID(edgeID);
            ;

            if (!visited[targetNodeID]) {
                int edgeCost = Graph.getCost(edgeID);

                int calcDistance = costs[minNodeID] + edgeCost;

                if (costs[targetNodeID] > calcDistance) {

                    costs[targetNodeID] = calcDistance;
                    int[] tmp = { targetNodeID, calcDistance };
                    queue.add(tmp);

                }
            }
        }
    }
}
