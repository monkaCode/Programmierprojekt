import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;

public class Test {
    
    public static void main(String[] args) throws Exception{
        
        final BufferedReader deutschland = new BufferedReader(new FileReader("C:/Users/maxis/Documents/Universität/Universität_Stuttgart/ÜbungenPSEOral/BufferedReaderExample/src/germany.fmi"));
        final BufferedReader stuttgart = new BufferedReader(new FileReader("C:/Users/maxis/Documents/Universität/Universität_Stuttgart/ÜbungenPSEOral/BufferedReaderExample/src/stgtregbz.fmi"));
        final BufferedReader currentBufferedReader = deutschland;
        String currentLine;
        int numberOfNodes = 0;
        int numberOfEdges = 0;
        final long startTime = System.currentTimeMillis();
        final long beforeUsedMem = Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
        
        ArrayList<Integer> nodeID = new ArrayList<>();
        //ArrayList<String> nodeID2 = new ArrayList<>();
        ArrayList<String> latitude = new ArrayList<>();
        ArrayList<String> longitude = new ArrayList<>();
        //ArrayList<String> elevation = new ArrayList<>();

        ArrayList<Integer> srcIDX = new ArrayList<>();
        ArrayList<Integer> trgIDX = new ArrayList<>();
        ArrayList<Integer> cost = new ArrayList<>();
        //ArrayList<String> type = new ArrayList<>();
        //ArrayList<String> maxspeed = new ArrayList<>();

        for(int i = 0;i <= 5; i++){
            if(i == 5){
                numberOfNodes = Integer.valueOf(currentBufferedReader.readLine());
                System.out.println("Number of nodes in graph: " + numberOfNodes);
                numberOfEdges = Integer.valueOf(currentBufferedReader.readLine());
                System.out.println("Number of edges in the graph: " + numberOfEdges);
            }
            else{   
                currentBufferedReader.readLine();
            }
        }
        for(int i = 0; i < numberOfNodes; i++) {
            currentLine = currentBufferedReader.readLine();
            String[] spiltLineNodes = currentLine.split(" ");
            int nodeIDStr = Integer.valueOf(spiltLineNodes[0]);
            //String nodeID2Str = spiltLineNodes[1];
            String latitudeStr = spiltLineNodes[2];
            String longitudeStr = spiltLineNodes[3];
            //String elevationStr = spiltLineNodes[4];
            nodeID.add(nodeIDStr);
            //nodeID2.add(nodeID2Str);
            latitude.add(latitudeStr);
            longitude.add(longitudeStr);
            //elevation.add(elevationStr);
        }

        for(int j = 0; j < numberOfEdges; j++){
            currentLine = currentBufferedReader.readLine();
            String[] spiltLineEdges = currentLine.split(" ");
            int srcIDXStr = Integer.valueOf(spiltLineEdges[0]);
            int trgIDXStr = Integer.valueOf(spiltLineEdges[1]);
            int costStr = Integer.valueOf(spiltLineEdges[2]);
            //String typeStr = spiltLineEdges[3];
            //String maxspeedStr = spiltLineEdges[4];
            srcIDX.add(srcIDXStr);
            trgIDX.add(trgIDXStr);
            cost.add(costStr);
            //type.add(typeStr);
            //maxspeed.add(maxspeedStr);
        }
            
        currentBufferedReader.close();

        final long endTime = System.currentTimeMillis();
        long afterUsedMem = Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
        System.out.println("Time to create arrays: " + (endTime - startTime) + "ms");
        System.out.println("Memory usage: " + (afterUsedMem - beforeUsedMem) /1000000 + "MB");

        //Throws exception because index starts at 0
        try {
            System.out.println(nodeID.get(numberOfNodes));
        } catch (IndexOutOfBoundsException e) {
            System.out.println("This index can't be assigned to any nodeID");
        }
        try {
            System.out.println(srcIDX.get(numberOfEdges));
        } catch (IndexOutOfBoundsException e) {
            System.out.println("This index can't be assigned to any srcIDX");
        }
    }
}
