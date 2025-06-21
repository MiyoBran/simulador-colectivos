package crapz.bus.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import crapz.bus.models.Line; // Renamed from Linea
import crapz.bus.models.Stop; // Renamed from Parada

import java.util.TreeMap;

public class ObjetcLists {

    private ObjetcLists() {
        // This empty and private constructor prevents instances of ObjetcLists from being created.
        // You don't need to initialize anything here if all your variables are static.
    }

    private static Map<Integer, Stop> allStops; // Renamed todasLasParadas
    private static Map<String, Line> allLines; // Renamed todasLasLineas
    private static int defaultBusCapacity = 30; // Renamed capacidadColectivoDefault

    // Load stop objects into Map
    private static void loadDataStops(Map<Integer, List<String>> stopMap) { // Renamed loadDataStops, mapaDeParadas
        allStops = new TreeMap<>();
        for (Entry<Integer, List<String>> entry : stopMap.entrySet()) {
            int stopId = entry.getKey(); // Renamed idParada
            String stopAddress = entry.getValue().get(0); // Renamed direccionParada
            Stop stop = new Stop(stopId, stopAddress);
            allStops.put(stop.getId(), stop);
        }
    }

    // Load line objects into Map
    private static void loadDataLines(Map<String, List<Integer>> lineMap) { // Renamed loadDataLines, mapaDeLineas
        allLines = new TreeMap<>();

        for (Entry<String, List<Integer>> kv : lineMap.entrySet()) {
            String lineCode = kv.getKey(); // Renamed codigoLinea
            List<Integer> lineStopIds = kv.getValue(); // Renamed idsParadasLinea
            List<Stop> stopsForThisLine = new ArrayList<>(); // Renamed paradasDeEstaLinea, objects Parada

            for (Integer stopId : lineStopIds) { // Renamed idParada
                Stop foundStop = allStops.get(stopId); // Renamed paradaEncontrada

                if (foundStop != null) {
                    stopsForThisLine.add(foundStop);
                } else {
                    System.err.println("Warning: Stop with ID " + stopId + " for line " + lineCode
                            + " not found.");
                }
            }

            if (stopsForThisLine.size() >= 5) { // A line must have at least 5 stops
                Line line = new Line(lineCode, stopsForThisLine); // Renamed Linea
                allLines.put(lineCode, line);
            } else {
                System.err.println(
                        "Warning: Line " + lineCode + " has fewer than 5 valid stops and would be ignored.");
            }
        }
    }

    public static void loadStops(Map<Integer, List<String>> stopMap) { // Renamed loadStops, mapaDeParadas
        loadDataStops(stopMap);
    }

    public static void loadLines(Map<String, List<Integer>> lineMap) { // Renamed loadLines, mapaDeLineas
        loadDataLines(lineMap);
    }

    public static Map<Integer, Stop> getAllStops() { // Renamed getTodasLasParadas
        return allStops;
    }

    public static Map<String, Line> getAllLines() { // Renamed getTodasLasLineas
        return allLines;
    }

    public static int getDefaultBusCapacity() { // Renamed getCapacidadColectivoDefault
        return defaultBusCapacity;
    }
}