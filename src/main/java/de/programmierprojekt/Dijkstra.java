package de.programmierprojekt;

import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

public class Dijkstra {

    static double[] queue;

    static int[] queueIndex;

    static int[] queueNodeIndex;

    static double getCostOfNodeID(int nodeID) {
        return queue[queueIndex[nodeID]];
    }

    static int getNodeIDOfPositionInQueue(int positionInQueue) {
        return queueNodeIndex[positionInQueue];
    }

    static int getPositionInQueueOfNodeID(int nodeID) {
        return queueIndex[nodeID];
    }

    static void swap(int i, int j) {
        double tempQueue = queue[i];
        queue[i] = queue[j];
        queue[j] = tempQueue;

        int nodeIDi = getNodeIDOfPositionInQueue(i);
        int nodeIDj = getNodeIDOfPositionInQueue(j);
        int tempQueueIndex = getPositionInQueueOfNodeID(nodeIDi);
        queueIndex[nodeIDi] = getPositionInQueueOfNodeID(nodeIDj);
        queueIndex[nodeIDj] = tempQueueIndex;

        int tempQueueNodeIndex = getNodeIDOfPositionInQueue(i);
        queueNodeIndex[i] = getNodeIDOfPositionInQueue(j);
        queueNodeIndex[j] = tempQueueNodeIndex;
    }

    static void prepareDijkstra(int srcNodeID) {
        queue = new double[Graph.numberOfNodes];
        queueIndex = new int[Graph.numberOfNodes];
        queueNodeIndex = new int[Graph.numberOfNodes];

        for(int i=0; i<Graph.numberOfNodes; i++) {
            queue[i] = Double.POSITIVE_INFINITY;
            queueIndex[i] = i;
            queueNodeIndex[i] = i;
        }
        queue[srcNodeID] = 0;
        swap(0, srcNodeID);
    }

    static void oneToAllDijkstra(int srcNodeID) {
        int start = 0;
        int end = 0;
        while(start < queue.length) {
            if(start % 1000000 == 0) {
                System.out.println(start + " | " + end);
            }
            
            getMinimum(start, end);
            int nodeID = getNodeIDOfPositionInQueue(start);
            int numberOfEdges = Graph.edgeOffset[nodeID+1]-Graph.edgeOffset[nodeID];
            int startOffset = Graph.edgeOffset[nodeID];
            for(int i=0; i<numberOfEdges; i++) {
                int targetNodeID = Graph.edgeData[1][startOffset+i];
                // if(getPositionInQueueOfNodeID(targetNodeID) > end) {
                //     end = getPositionInQueueOfNodeID(targetNodeID);
                // }
                int edgeCost = Graph.edgeData[2][startOffset+i];
                if(getCostOfNodeID(nodeID)+edgeCost < getCostOfNodeID(targetNodeID)) {
                    if(Double.isInfinite(getCostOfNodeID(targetNodeID))) {
                        end++;
                    }
                    queue[getPositionInQueueOfNodeID(targetNodeID)] = getCostOfNodeID(nodeID)+edgeCost;
                    // for(int j=end; j<queue.length; j++) {
                    //     if(Double.isInfinite(queue[j])) {
                    //         // System.out.println(j);
                    //         s
                    //         end = j;
                    //         break;
                    //     }
                    // }
                    swap(getPositionInQueueOfNodeID(targetNodeID), end);
                }
            }
            start++;
        }
    }

    static void getMinimum(int position, int end) {
        double tempMinimum = queue[position];
        int tempI = position;
        for(int i=position; i<=end; i++) {
            if(queue[i] < tempMinimum) {
                tempMinimum = queue[i];
                tempI = i;
            }
        }
        swap(tempI, position);
    }

}
