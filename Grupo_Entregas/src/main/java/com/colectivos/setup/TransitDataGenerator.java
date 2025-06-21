package com.colectivos.setup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.colectivos.data.Data;
import com.colectivos.data.LoadParameters;
import com.colectivos.model.Bus;
import com.colectivos.model.Line;
import com.colectivos.model.Passenger;
import com.colectivos.model.Stop;

public class TransitDataGenerator {
    // integer that keeps track of total number of passengers to avoid duplicated IDs
    private static int totalPassengers = 0;

    /**
     * Generates a list of {@link Passenger} objects with random destinations, excluding the current stop.
     * Each passenger is assigned a unique ID and a random valid destination from the provided stops map,
     * ensuring the destination is not the same as the origin (stopId).
     * The number of passengers generated is a random number between 0 and limit, inclusive.
     *
     * @param limit   the maximum number of passengers that can be generated at this stop
     * @param stops   a map of all available stops
     * @param stopId  the ID of the stop where the passengers are currently waiting (to be excluded as a destination)
     * @return a list of randomly generated passengers with valid destinations
     */
    private static List<Passenger> generatePassengers(int limit, Map<Integer, Stop> stops, int stopId) {
        List<Passenger> list = new ArrayList<>();
        Random random = new Random();
        int passengerCount = random.nextInt(limit + 1);
                //START OF THE GREATEST WAY TO GENERATE A PASSENGER I HAVE EVER SEEN
        while (list.size() < passengerCount) {
            int destinationId = random.nextInt(104) + 1;
            if (destinationId != stopId) {
                Stop destination = stops.get(destinationId);
                if (destination != null) {
                    totalPassengers++;
                    list.add(new Passenger(totalPassengers, destination));
                    //VERY CLEVER WAY TO GENERATE PASSENGERS. WAY TO GO ME
                    //THE COMMENT ABOVE IS CORRECT
                }
            }
        }
        return list;
    }

    /**
     * Loads a stop map from {@link Data} using a file specified by {@link LoadParameters} and populates each stop with randomly generated passengers.
     * Each stop is assigned a list of passengers with random destinations, which is different from their start position,
     * using the {@code generatePassengers} method. The number of passengers per stop is randomly determined.
     *
     * @param limit the maximum number of passengers that can be generated per stop
     * @return a map of stop IDs to {@link Stop} objects, each populated with passengers
     * @throws IOException if there is an error reading the stop data file
     */
    public static Map<Integer, Stop> loadAndPopulateStops(int limit) throws IOException {
        Map<Integer, Stop> stops = Data.loadStops(LoadParameters.getStopFile());
        for (Stop currentStop: stops.values()) {
            List<Passenger> list = generatePassengers(limit, stops, currentStop.getId());
            currentStop.setPassengers(list);
        }
        return stops;
    }


    /**
     * Generates a list of {@link Bus} objects, each associated with a different {@link Line}.
     * Loads a map of lines from {@link Data} using a file specified by {@link LoadParameters}, associates each line with the provided stops,
     * and creates one bus per line. Each bus is assigned a unique ID starting from 1.
     *
     * @param stops a map of stop IDs to {@link Stop} objects used to associate stops with lines
     * @return a list of {@link Bus} objects, each linked to a corresponding line
     * @throws IOException if there is an error reading the line data file
     */
    public static List<Bus> generateBuses(Map<Integer, Stop> stops) throws IOException{
        Map<String, Line> lines = Data.loadLines(LoadParameters.getLineFile(), stops);

        List<Bus> buses = new ArrayList<>();
        int i = 1;
        for (Line line : lines.values()) {
            buses.add(new Bus(i++, line));
        }
        return buses;
    }

}
