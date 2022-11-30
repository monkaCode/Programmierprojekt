package CleanCode;

import java.util.*;

public class BinarySearch {

    private static Graph graph;
    static double[] nodesLat;
    static int[] nodesLatIndex;
     
    public BinarySearch(Graph graph) {
        this.graph = graph;
        this.nodesLat = new double[graph.numberOfNodes];
        this.nodesLatIndex = new int[graph.numberOfNodes];
        
    }

    public void sort() {
        for(int i=0; i<graph.numberOfNodes; i++) {
            nodesLat[i] = graph.nodesLatLon[0][i];
            nodesLatIndex[i] = i;
        }
        for(int j=0; j<nodesLat.length; j++) {
            int swaps = 0;
            if(j % 1000 == 0) {
                System.out.println(j);
            }
            for(int i=0; i<nodesLat.length -1; i++) {
                if(nodesLat[i] > nodesLat[i+1]) {
                    swap(i, i+1);
                    swaps++;
                    // System.out.println("swap");
                } else {
                    // System.out.println("no swap");
                }
            }
            if(swaps == 0) {
                break;
            }
        }
        // Arrays.stream(nodesLat).forEach(d -> System.out.println(d));
        // System.out.println("--------------");
        // Arrays.stream(nodesLatIndex).forEach(d -> System.out.println(d));
    }

    private void swap(int i, int j) {
        double temp = nodesLat[i];
        nodesLat[i] = nodesLat[j];
        nodesLat[j] = temp;
        int tempID = nodesLatIndex[i];
        nodesLatIndex[i] = nodesLatIndex[j];
        nodesLatIndex[j] = tempID;
    }
}
