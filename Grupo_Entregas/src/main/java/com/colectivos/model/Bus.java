package com.colectivos.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Represents a bus in a transit network.
 * A bus has a unique ID, a reference to the {@link Line} it travels, and a list of passengers.
 * It maintains its current position along the line using an index and can pick up and drop off passengers at each stop.
 * This class provides methods for adding/removing passengers based on stops,
 * advancing through the route, and generating string representations of the bus and its stops.
*/
public class Bus {
    private final int id;
    private List<Passenger> passengers;
    private final Line line;
    private int nextStopIndex;
    private Stop currentStop;

    /**
     * Constructs a Bus with the given ID and assigned line.
     * @param id the unique bus' ID
     * @param line the {@link Line} the bus will traverse
    */
    public Bus(int id, Line line) {
        this.id = id;
        this.passengers = new ArrayList<>();
        this.line = line;
        currentStop = null;
        nextStopIndex = 0;
    }

    /**
     * Returns the unique identifier of the bus.
     * @return the bus ID
    */
    public int getId() {
        return id;
    }

    /**
     * Returns the line this bus is assigned to.
     * @return the {@link Line} object
    */
    public Line getLine() {
        return line;
    }

    /**
     * Returns the current stop the bus is at.
     * @return the current {@link Stop}, or {@code null} if the bus has not started its route
    */
    public Stop getCurrentStop() {
        return currentStop;
    }

    /**
     * Returns the list of passengers currently on the bus.
     * @return a list of {@link Passenger} objects
    */
    public List<Passenger> getPassengers() {
        return passengers;
    }

    /**
     * Adds a single passenger to the bus.
     * @param passenger the {@link Passenger} to add
    */
    public void addPassenger(Passenger passenger) {
        passengers.add(passenger);
    }

    /**
     * Adds passengers from a given stop to the bus, if their destinations are on the bus's line.
     * @param stop the stop from which to pick up passengers
     * @return the list of passengers that were successfully added to the bus
    */
    public List<Passenger> addPassengers(Stop stop) {
        List<Passenger> removedPassengers = new ArrayList<>();
        if (stop.passengerAmount() == 0) {
            return removedPassengers;
        }
        Set<Stop> stopsSet = new HashSet<>();
        for (Stop currentStop : line.getStops()) {
            stopsSet.add(currentStop);
        }

        for (Passenger newPassenger : stop.getPassengers()) {
            if (stopsSet.contains(newPassenger.getDestination())) {
                passengers.add(newPassenger);
                removedPassengers.add(newPassenger);
            }
        }
        return removedPassengers;
    }

    /**
     * Removes all passengers from the bus whose destination is the given stop.
     * @param stop the stop where passengers will get off the bus
     * @return the list of passengers that were removed from the bus
    */
    public List<Passenger> removePassengers(Stop stop) {
        List<Passenger> removedPassengers = new ArrayList<>();
        Iterator<Passenger> iterator = passengers.iterator();
        while (iterator.hasNext()) {
            Passenger removed = iterator.next();
            if (removed.getDestination().equals(stop)) {
                removedPassengers.add(removed);
                iterator.remove();
            }
        }

        return removedPassengers;
    }

    /**
     * Advances the bus to the next stop on its line, updating its current stop.
     * Does nothing if the bus has already reached the end of the line.
    */
    public void traverseLine() {
        List<Stop> stopList = line.getStops();
        if (nextStopIndex < stopList.size()) {
            currentStop = stopList.get(nextStopIndex);
            nextStopIndex++;
        }
    }

    /**
     * Returns a string representing all the stops on the line, in travel order.
     * @return a formatted string of all stops with their IDs and addresses
    */
    public String printAllStops() {
        StringBuilder sb = new StringBuilder();
        sb.append("All stops on line ").append(line.getCode()).append(": ");
        
        List<Stop> stops = line.getStops();
        for (int i = 0; i < stops.size(); i++) {
            Stop stop = stops.get(i);
            sb.append("Stop ").append(stop.getId()).append(" (").append(stop.getAddress()).append(")");
            if (i < stops.size() - 1) {
                sb.append(" -> ");
            }
        }
        return sb.toString();
    }

    /**
     * Returns a detailed string representation of the bus, including its current stop
     * and the list of passengers currently riding the bus
     * @return a formatted string describing the bus state
    */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Bus ").append(id);
        
        sb.append(", current stop: ").append(currentStop).append("\n");
        
        if (passengers.isEmpty()) {
            sb.append("[No passengers are riding the bus]");
        } else {
            sb.append("[Passengers riding the bus]");
            for (Passenger p : passengers) {
                sb.append("\n").append(p.toString());
            }
        }
        
        return sb.toString();
    }

}
