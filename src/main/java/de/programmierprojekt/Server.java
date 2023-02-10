package de.programmierprojekt;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import de.programmierprojekt.backend.Dijkstra;
import de.programmierprojekt.backend.Graph;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class Server {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);

        server.createContext("/js/index.js")
                .setHandler(exchange -> Server.handleGetRequest(exchange, "js", "index.js"));
        server.createContext("/js/path.js").setHandler(exchange -> Server.handleGetRequest(exchange, "js", "path.js"));

        server.createContext("/css/index.css")
                .setHandler(exchange -> Server.handleGetRequest(exchange, "css", "index.css"));
        server.createContext("/css/path.css")
                .setHandler(exchange -> Server.handleGetRequest(exchange, "css", "path.css"));

        server.createContext("/main").setHandler(exchange -> Server.handleGetRequest(exchange, "html", "index.html"));
        server.createContext("/start").setHandler(exchange -> Server.handleGetRequest(exchange, "html", "path.html"));

        server.createContext("/prepare").setHandler(Server::handlePrepareRequest);
        server.createContext("/closestNode").setHandler(Server::handleRequestClosestNode);
        server.createContext("/dijkstra").setHandler(Server::handleRequestDijkstra);

        server.start();
    }

    /**
     * Server Provides Client with requested files
     * e.g. fileType: js fileName: index.js -> Server sends index.js to Client
     * 
     * @param exchange
     * @param fileType
     * @param fileName
     * @throws IOException
     */
    private static void handleGetRequest(HttpExchange exchange, String fileType, String fileName) throws IOException {

        String response = new String(Files.readAllBytes(
                Paths.get("src/main/java/de/programmierprojekt/frontend/" + fileType + "/"
                                + fileName)));
                            
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.sendResponseHeaders(200, response.getBytes().length);

        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    /**
     * Server prepares Graph with for given pathname (for .fmi File)
     * and answers with a OK. if done correctly
     * 
     * @param exchange
     * @throws IOException
     */
    private static void handlePrepareRequest(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        Scanner scanner = new Scanner(query);
        String pathName = "";

        while (scanner.hasNext()) {
            pathName = scanner.next();
        }

        Graph.readGraph(pathName);
        Graph.prepareBinarySearch();

        String response = "OK.";

        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.sendResponseHeaders(200, response.length());

        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    /**
     * For given Latitude and Longitude value from the Client
     * it calculates the closest Node (with Lat. and Lon.) values
     * and sends it as coded String: nodeID+Latitude+Longitude
     * 
     * @param exchange
     * @throws IOException
     */
    private static void handleRequestClosestNode(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        Scanner scanner = new Scanner(query).useDelimiter("&");
        double lat = 0;
        double lng = 0;

        while (scanner.hasNext()) {
            String[] pair = scanner.next().split("=");
            if (pair[0].equals("lat")) {
                lat = Double.parseDouble(pair[1]);
            }
            if (pair[0].equals("lng")) {
                lng = Double.parseDouble(pair[1]);
            }
        }

        int closestNode = Graph.findClosestNode(lat, lng);

        double latClosestNode = Graph.getLatitude(closestNode);
        double lonClosestNode = Graph.getLongitude(closestNode);

        String response = String.valueOf(closestNode + "+" + latClosestNode + "+" + lonClosestNode);

        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.sendResponseHeaders(200, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();

    }

    /**
     * For given start and end nodeID from the Client 
     * it calculates the shortes path and sends the result
     * as geoJSON String
     * @param exchange
     * @throws IOException
     */
    private static void handleRequestDijkstra(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        Scanner scanner = new Scanner(query).useDelimiter("&");
        int startID = 0;
        int endID = 0;

        while (scanner.hasNext()) {
            String[] pair = scanner.next().split("=");
            if (pair[0].equals("start")) {
                startID = Integer.parseInt(pair[1]);
            }
            if (pair[0].equals("end")) {
                endID = Integer.parseInt(pair[1]);
            }
        }

        Dijkstra dijkstra = new Dijkstra(startID);
        dijkstra.oneToOneDijkstra(endID);

        String response = dijkstra.pathToJSON(startID, endID);

        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.sendResponseHeaders(200, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

}
