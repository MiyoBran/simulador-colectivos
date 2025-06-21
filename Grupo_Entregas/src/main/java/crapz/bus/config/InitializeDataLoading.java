package crapz.bus.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.function.Function;

import crapz.bus.data.ObjetcLists;
import crapz.bus.models.Line; // Renamed Linea to Line
import crapz.bus.models.Stop; // Renamed Parada to Stop
import crapz.bus.models.Passenger; // Renamed Pasajero to Passenger
import crapz.bus.utils.GenericDataLoading;

public class InitializeDataLoading {
    private Map<Integer, List<String>> stopMap; // Renamed mapaDeParadas to stopMap
    private Map<String, List<Integer>> lineMap; // Renamed mapaDeLineas to lineMap
    private Map<Integer, Passenger> allPassengers; // Renamed todosLosPasajeros to allPassengers
    private int simulationPassengerCount; // Renamed cantidadPasajerosSimulacion to simulationPassengerCount

    public void initialize() throws IOException, IllegalArgumentException { // Renamed inicializar to initialize
        initializeMethods(); // Renamed inicializarMetodos to initializeMethods
    }

    private void initializeMethods() throws IOException, IllegalArgumentException {
        loadConfigurationData(); // Renamed cargaDatosConfiguracion to loadConfigurationData
        ObjetcLists.loadStops(stopMap);
        ObjetcLists.loadLines(lineMap);
        generateAndDistributePassengers(); // Renamed generarYDistribuirPasajeros to generateAndDistributePassengers
    }

    /**
     * Loads all necessary data and configuration for the application.
     *
     * @throws IOException If there are I/O problems reading files.
     * @throws IllegalArgumentException If there are format errors or
     * invalid data in the files.
     */
    private void loadConfigurationData() throws IOException, IllegalArgumentException { // Renamed cargaDatosConfiguracion to loadConfigurationData
        // Load configuration parameters
        LoadingParameters.parameters();
        String linesFile = LoadingParameters.getLinesFile(); // Renamed archivoLineas to linesFile
        String stopsFile = LoadingParameters.getStopsFile(); // Renamed archivoParadas to stopsFile
        simulationPassengerCount = LoadingParameters.getPassengerCount();

        // --- Load Bus Lines ---
        loadLines(linesFile);

        // --- Load Stops ---
        loadStops(stopsFile);
    }

    private void loadStops(String stopsFile) throws IOException { // Renamed loadStops
        GenericDataLoading<Integer, String> loadData = new GenericDataLoading<>();

        // Define mapping functions for stops
        Function<String, Integer> stopIdMapper = new Function<>() { // Renamed stoppedIntegerKeyMapper
            @Override
            public Integer apply(String s) {
                // Here, the String is expected to be a number. If not, parseInt would throw
                // NumberFormatException.
                return Integer.parseInt(s);
            }
        };

        Function<String, String> stopAddressMapper = new Function<>() { // Renamed stringValueMapperStops
            @Override
            public String apply(String s) {
                return s; // The key is already a String, no complex conversion needed
            }
        };

        stopMap = loadData.loadFromFile(
                stopsFile,
                stopIdMapper,
                stopAddressMapper);
    }

    private void loadLines(String linesFile) throws IOException { // Renamed loadLines
        // --- Load Bus Lines ---
        GenericDataLoading<String, Integer> loadData = new GenericDataLoading<>();

        // Define mapping functions for Lines
        Function<String, String> lineCodeMapper = new Function<>() { // Renamed stringKeyMapperLines
            @Override
            public String apply(String s) {
                return s; // The key is already a String, no conversion needed
            }
        };

        Function<String, Integer> lineStopIdMapper = new Function<>() { // Renamed integerValueMapperLines
            @Override
            public Integer apply(String s) {
                // Here, the String is expected to be a number. If not, parseInt would throw
                // NumberFormatException.
                return Integer.parseInt(s);
            }
        };

        lineMap = loadData.loadFromFile(
                linesFile,
                lineCodeMapper,
                lineStopIdMapper);
    }

    // Load passenger objects into Map
    private void generateAndDistributePassengers() { // Renamed generarYDistribuirPasajeros
        Map<String, Line> allLines = ObjetcLists.getAllLines(); // Renamed todasLasLineas
        if (allLines == null || allLines.isEmpty()) {
            System.err
                    .println("No lines loaded to generate passengers. Make sure to load the lines first.");
            return;
        }

        // Initialize the map if it's the first time passengers are generated
        if (allPassengers == null) { // Renamed todosLosPasajeros
            allPassengers = new TreeMap<>(); // Renamed todosLosPasajeros
        }

        Random rand = new Random();
        List<Line> lineList = new ArrayList<>(allLines.values()); // Renamed listaDeLineas

        // Get the current highest ID to continue the sequence without duplicates
        int currentMaxPassengerId = 0;
        if (!allPassengers.isEmpty()) { // Renamed todosLosPasajeros
            if (allPassengers instanceof TreeMap) { // Renamed todosLosPasajeros
                currentMaxPassengerId = ((TreeMap<Integer, Passenger>) allPassengers).lastKey(); // Renamed todosLosPasajeros
            } else {
                currentMaxPassengerId = Collections.max(allPassengers.keySet()); // Renamed todosLosPasajeros
            }
        }

        for (int i = 0; i < simulationPassengerCount; i++) {
            // 1. Select a random line
            Line selectedLine = lineList.get(rand.nextInt(lineList.size())); // Renamed lineaSeleccionada

            List<Stop> stopsOnLine = selectedLine.getStops(); // Renamed paradasDeLaLinea

            // Ensure the line has at least 2 stops to generate a trip
            if (stopsOnLine.size() < 2) {
                System.err.println("Warning: Line " + selectedLine.getCode() // Renamed lineaSeleccionada.getCodigo
                        + " does not have enough stops to generate a trip. Skipping passenger.");
                continue; // Skip generating this passenger and continue with the next one
            }

            Stop origin;
            Stop destination;

            // 2. Select origin and destination WITHIN the same line, ensuring that
            // destination is AFTER the origin
            int originIndex;
            int destinationIndex;

            do {
                originIndex = rand.nextInt(stopsOnLine.size());
                destinationIndex = rand.nextInt(stopsOnLine.size());
            } while (destinationIndex <= originIndex); // Destination must be a stop after the origin

            origin = stopsOnLine.get(originIndex);
            destination = stopsOnLine.get(destinationIndex);

            Passenger passenger = new Passenger(++currentMaxPassengerId, origin, destination);
            allPassengers.put(passenger.getId(), passenger); // Renamed todosLosPasajeros
            origin.addPassenger(passenger); // Add the passenger to their origin stop
        }
    }
}