package crapz.bus.app;

import java.io.IOException;

import crapz.bus.config.InitializeDataLoading;
import crapz.bus.simulator.BusRideSimulator;

public class App {
    public static void main(String[] args) {
        InitializeDataLoading dataLoader = new InitializeDataLoading();
        BusRideSimulator simulator = new BusRideSimulator(); // Renamed 's' to 'simulator' for clarity
        try {
            // Loads all data: stops, lines, passengers (according to config.properties)
            dataLoader.initialize();
            simulator.simulate();

        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Error initializing the simulation: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
