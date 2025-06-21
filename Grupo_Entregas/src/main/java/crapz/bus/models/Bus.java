package crapz.bus.models;

import java.util.ArrayList;
import java.util.List;

public class Bus {

    private int id;
    private Line line; // assigned line
    private List<Passenger> passengers; // passengers on board
    private Stop currentStop; // Additional attribute, 1-to-1 relationship with Stop
    private int maxCapacity; // Additional attribute, since buses have a capacity.

    // Constructor
    public Bus(int id, Line line, int maxCapacity) {
        this.id = id;
        this.line = line;
        this.maxCapacity = maxCapacity;
        this.passengers = new ArrayList<>();
        // The bus initially starts at the first stop of its line
        if (!line.getStops().isEmpty()) {
            this.currentStop = line.getStops().get(0);
        } else {
            this.currentStop = null; // Or handle an error if the line has no stops
        }
    }

    // Getters
    public int getId() {
        return id;
    }

    public Line getLine() {
        return line;
    }

    public Stop getCurrentStop() {
        return currentStop;
    }

    public List<Passenger> getPassengersOnBoard() {
        return passengers;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public int getPassengerCountOnBoard() {
        return passengers.size();
    }

    // Simulation methods
    /**
     * Simulates passengers getting off at the current stop.
     *
     * @return List of passengers who got off.
     */
    public List<Passenger> dropOffPassengers() {
        List<Passenger> passengersGettingOff = new ArrayList<>();
        // Iterate safely to avoid ConcurrentModificationException
        for (int i = 0; i < passengers.size(); i++) {
            Passenger p = passengers.get(i);
            if (p.getDestination().equals(currentStop)) { // Compare by Stop object
                passengersGettingOff.add(p);
                // Do not remove here, they are removed later
            }
        }
        passengers.removeAll(passengersGettingOff); // Remove all who are getting off
        return passengersGettingOff;
    }

    /**
     * Simulates passengers boarding at the current stop. Passengers board if
     * their destination is on the line's route and if there is available
     * capacity.
     *
     * @return List of passengers who boarded.
     */
    public List<Passenger> pickUpPassengers() {
        List<Passenger> passengersBoarding = new ArrayList<>();
        List<Passenger> waitingAtStop = new ArrayList<>(currentStop.getPassengers()); // Copy to avoid issues

        // Calculate these values once
        int currentStopIndex = line.getStops().indexOf(currentStop);
        int availableCapacity = maxCapacity - passengers.size();

        for (Passenger p : waitingAtStop) {
            // Check capacity at the beginning of the loop for efficiency
            if (availableCapacity <= 0) {
                break; // Bus is full, no more passengers to check
            }

            // Check if the passenger's destination is further along the line's route
            int destinationIndex = line.getStops().indexOf(p.getDestination());
            if (destinationIndex > currentStopIndex) {
                passengers.add(p);
                passengersBoarding.add(p);
                availableCapacity--; // Decrease available capacity
            }
        }

        currentStop.removePassengers(passengersBoarding); // Remove boarded passengers from the stop
        return passengersBoarding;
    }

    /**
     * Advances the bus to the next stop on its line.
     *
     * @return true if it advances to the next stop, false if it reaches the end of
     * the line.
     */
    public boolean advanceToNextStop() {
        List<Stop> route = line.getStops();
        int currentIndex = route.indexOf(currentStop);

        if (currentIndex != -1 && currentIndex < route.size() - 1) {
            currentStop = route.get(currentIndex + 1);
            return true;
        }
        return false; // Reaches the end of the line
    }

    @Override
    public String toString() {
        return "Bus [ID: " + id + ", Line: " + line.getCode() + ", Passengers on board: " + passengers.size() + "/" + maxCapacity + "]";
    }
}