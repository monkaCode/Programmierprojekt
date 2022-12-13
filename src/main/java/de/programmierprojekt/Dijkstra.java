package de.programmierprojekt;

public class Dijkstra {

    static int[] queue;

    static int[] queueIndex;

    static int[] queueNodeIndex;

    static int min2;
    static int amountOfComparisons = 0;

    static int getCostOfNodeID(int nodeID) {
        return queue[queueIndex[nodeID]];
    }

    static int getNodeIDOfPositionInQueue(int positionInQueue) {
        return queueNodeIndex[positionInQueue];
    }

    static int getPositionInQueueOfNodeID(int nodeID) {
        return queueIndex[nodeID];
    }

    static void swap(int i, int j) {
        int tempQueue = queue[i];
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
        queue = new int[Graph.numberOfNodes];
        queueIndex = new int[Graph.numberOfNodes];
        queueNodeIndex = new int[Graph.numberOfNodes];

        for(int i=0; i<Graph.numberOfNodes; i++) {
            queue[i] = Integer.MAX_VALUE;
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
            
            getMinimum(start, end, min2);
            int nodeID = getNodeIDOfPositionInQueue(start);
            int numberOfEdges = Graph.edgeOffset[nodeID+1]-Graph.edgeOffset[nodeID];
            int startOffset = Graph.edgeOffset[nodeID];
            for(int i=0; i<numberOfEdges; i++) {
                int targetNodeID = Graph.edgeData[1][startOffset+i];
                int edgeCost = Graph.edgeData[2][startOffset+i];
                int oldCost = getCostOfNodeID(targetNodeID);
                int newCost = getCostOfNodeID(nodeID)+edgeCost;
                if(newCost < oldCost) {
                    if(newCost < getCostOfNodeID(min2)) {
                        min2 = targetNodeID;
                    }
                    if(Integer.MAX_VALUE == getCostOfNodeID(targetNodeID)) {
                        end++;
                    }
                    queue[getPositionInQueueOfNodeID(targetNodeID)] = newCost;
                    
                    swap(getPositionInQueueOfNodeID(targetNodeID), end);
                }
            }
            start++;
        }
        System.out.println(amountOfComparisons);
    }

    static void getMinimum(int position, int end, int lastMin2) {
        int tempMinimum = position;
        if(position < queue.length-1 && position % 2 == 0) {
            min2 = getNodeIDOfPositionInQueue(position+1);
            for(int i=position; i<=end; i++) {
                amountOfComparisons++;
                if(queue[i] < queue[tempMinimum]) {
                    min2 = getNodeIDOfPositionInQueue(tempMinimum);
                    tempMinimum = i;
                }
            }
            swap(tempMinimum, position);
        } else {
            swap(getPositionInQueueOfNodeID(lastMin2), position);
        }
        
        
    }

}
