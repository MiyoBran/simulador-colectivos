package com.colectivos.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.TreeMap;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import com.colectivos.model.Stop;
import com.colectivos.model.Line;

/**
 * Utility class for loading bus stop and line data from resource files.
 * Provides methods to parse stop and line definitions from text files located
 * in the classpath. Each stop and line is represented using the {@link Stop} and {@link Line} classes.
 *
 * The input files must be well-formed and follow the expected format:
 *   Stop file: {@code id;address}
 *   Line file: {@code lineId;stopId1;stopId2;...;stopIdx}
*/
public class Data {
    
    /**
     * Loads bus stops from a text file located in the classpath.
     * Each line in the file must contain a stop ID and an address, separated by a semicolon, e.g.:
     * {@code 101;Some Street}.
     * Lines starting with {@code #} or empty lines are ignored.
     *
     * @param fileName the name of the file in the classpath (e.g., {@code parada.txt})
     * @return a map of stops with {@link Integer} as keys and {@link Stop} as values
     * @throws IOException if the file is not found or cannot be read
    */
    public static Map<Integer, Stop> loadStops(String fileName) throws IOException {
        Map<Integer, Stop> allStops = new TreeMap<>();
        
        InputStream inputStream = Data.class.getClassLoader().getResourceAsStream(fileName);
        if (inputStream == null) {
            throw new FileNotFoundException("Unable to find " + fileName + " in classpath.");
        }

        try (Scanner fileRead = new Scanner(inputStream)) {
            while (fileRead.hasNextLine()) {
                String currentLine = fileRead.nextLine().trim();
                if (currentLine.isEmpty() || currentLine.startsWith("#")) {
                    continue;
                }

                String[] parts = currentLine.split(";");
                if (parts.length < 2) {
                    System.err.println("Malformed line: " + currentLine);
                    continue;
                }

                try {
                    int stopId = Integer.parseInt(parts[0].trim());
                    String stopAddress = parts[1].trim();
                    Stop currentStop = new Stop(stopId, stopAddress, new ArrayList<>());
                    allStops.put(stopId, currentStop);
                } catch (NumberFormatException e) {
                    System.err.println("Invalid stop ID in line: " + currentLine);
                }
            }
        }

        return allStops;
    }


    /**
     * Loads bus lines from a text file located in the classpath.
     * Each line in the file must begin with a line code, followed by the IDs of its stops,
     * separated by semicolons, e.g.: {@code A;101;102;103}.
     * Any stop IDs that are not found in the provided stop map will be skipped with a warning.
     *
     * @param fileName the name of the file in the classpath (e.g., {@code linea.txt})
     * @param allStops a map of all available {@link Stop} objects, with {@link Integer} as its keys
     * @return a map of lines with {@link String} as keys and {@link Line} as its values
     * @throws IOException if the file is not found or cannot be read
     */
    public static Map<String, Line> loadLines(String fileName, Map<Integer, Stop> allStops) throws IOException {
        InputStream inputStream = Data.class.getClassLoader().getResourceAsStream(fileName);
        if (inputStream == null) {
            throw new FileNotFoundException("Unable to find " + fileName + " in classpath.");
        }

        Map<String, Line> allLines = new TreeMap<>();
        try (Scanner fileRead = new Scanner(inputStream)) {
            while (fileRead.hasNextLine()) {
                String currentLine = fileRead.nextLine().trim();
                if (currentLine.isEmpty() || currentLine.startsWith("#")) {
                    continue;
                }

                String[] parts = currentLine.split(";");
                if (parts.length < 3) {
                    System.err.println("Malformed line: " + currentLine);
                    continue;
                }

                String lineId = parts[0].trim();
                List<Stop> listStops = new ArrayList<>();   

                for (int i = 1; i < parts.length; i++) {
                    String stopIdStr = parts[i].trim();
                    if (!stopIdStr.isEmpty()) {
                        try {
                            int currentId = Integer.parseInt(stopIdStr);
                            if (allStops.containsKey(currentId)) {
                                Stop currentStop = allStops.get(currentId);
                                listStops.add(currentStop);
                            } else {
                                System.err.println("Stop with ID " + currentId + " not found for line " + lineId);
                            }
                        } catch (NumberFormatException e) {
                            System.err.println("Invalid stop ID: " + stopIdStr + " in line " + lineId);
                        }
                    }
                }
                if (listStops.isEmpty()) {
                    System.err.println("Warning: Line " + lineId + " has no valid stops.");
                }
                Line line = new Line(lineId, listStops);
                allLines.put(lineId, line);
            }
        }
        return allLines;
    }

}
