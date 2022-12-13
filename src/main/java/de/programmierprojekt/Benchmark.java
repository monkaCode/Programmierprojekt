package de.programmierprojekt;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Scanner;

import org.javatuples.Pair;

public class Benchmark {

	public static void main(String[] args) throws Exception {
		// read parameters (parameters are expected in exactly this order)
		String graphPath = args[1];
		double lon = Double.parseDouble(args[3]);
		double lat = Double.parseDouble(args[5]);
		String quePath = args[7];
		int sourceNodeId = Integer.parseInt(args[9]);

		// run benchmarks
		System.out.println("Reading graph file and creating graph data structure (" + graphPath + ")");
		long graphReadStart = System.currentTimeMillis();

		Graph.readGraph(graphPath);

		long graphReadEnd = System.currentTimeMillis();
		System.out.println("\tgraph read took " + (graphReadEnd - graphReadStart) + "ms");

		System.out.println("Setting up closest node data structure...");

		Graph.prepareBinarySearch();
		// find temporary closest node with binarySearch
		Pair<Integer, Integer> binarySearchResult = Graph.binarySearch(lat, 0, Graph.nodesLat.length - 1);
		System.out.println("Finding closest node to coordinates " + lon + " " + lat);

		double[] coords = { 0.0, 0.0 };

		long nodeFindStart = System.currentTimeMillis();
		int closestNode = Graph.findClosestNode(lat, lon, binarySearchResult);
		long nodeFindEnd = System.currentTimeMillis();

		double latClosestNode = Graph.getLatitude(closestNode);
		double lonClosestNode = Graph.getLongitude(closestNode);

		coords[0] = latClosestNode;
		coords[1] = lonClosestNode;

		System.out.println(
				"\tfinding node took " + (nodeFindEnd - nodeFindStart) + "ms: " + coords[0] + ", " + coords[1]);

		System.out.println("Running one-to-one Dijkstras for queries in .que file " + quePath);
		long queStart = System.currentTimeMillis();
		try (BufferedReader bufferedReader = new BufferedReader(new FileReader(quePath))) {
			String currLine;
			int i = 1;
			while ((currLine = bufferedReader.readLine()) != null) {
				int oneToOneSourceNodeId = Integer.parseInt(currLine.substring(0, currLine.indexOf(" ")));
				int oneToOneTargetNodeId = Integer.parseInt(currLine.substring(currLine.indexOf(" ") + 1));
				int oneToOneDistance = -42;

				Dijkstra dijkstra = new Dijkstra(oneToOneSourceNodeId);
				oneToOneDistance = dijkstra.oneToOneDijkstra(oneToOneTargetNodeId);
				System.out.println(i + " " + oneToOneDistance);
				i++;
			}
		} catch (Exception e) {
			System.out.println("Exception...");
			e.printStackTrace();
		}
		long queEnd = System.currentTimeMillis();
		System.out.println("\tprocessing .que file took " + (queEnd - queStart) + "ms");

		System.out.println("Computing one-to-all Dijkstra from node id " + sourceNodeId);
		long oneToAllStart = System.currentTimeMillis();

		Dijkstra dijkstra = new Dijkstra(sourceNodeId);
		int[] distances = dijkstra.oneToAllDijkstra();

		long oneToAllEnd = System.currentTimeMillis();
		System.out.println("\tone-to-all Dijkstra took " + (oneToAllEnd - oneToAllStart) + "ms");

		// ask user for a target node id
		System.out.print("Enter target node id... ");
		int targetNodeId = (new Scanner(System.in)).nextInt();
		int oneToAllDistance = -42;
		// TODO set oneToAllDistance to the distance from sourceNodeId to
		// targetNodeId as computed by the one-to-all Dijkstra
		oneToAllDistance = distances[targetNodeId];
		System.out.println("Distance from " + sourceNodeId + " to " + targetNodeId + " is " + oneToAllDistance);
	}

}
