package com.colectivos.model;

/**
 * This class represents a passenger within a the bus network.
 * Each passenger has a unique ID and a destination stop.
 * Passengers are assigned to stops and travel via buses toward their destination.
*/
public class Passenger {
    private final int id;
    private Stop destination;

    /**
     * Constructs a new {@code Passenger} with a unique ID and a destination stop.
     * @param id the unique identifier of the passenger
     * @param destination the stop the passenger wants to arrive at
    */
    public Passenger(int id, Stop destination) {
        this.id = id;
        this.destination = destination;
    }

    /**
     * Returns the unique ID of the passenger
     * @return the passenger's ID
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the passenger's Stop destination
     * @return the {@link Stop} object which represents the passenger's destination
     */
    public Stop getDestination() {
        return destination;
    }

    /**
     * Sets the passenger's destination
     * @param destination the new destination to be used
    */
    public void setDestination(Stop destination) {
        this.destination = destination;
    }

    /**
     * Returns a string representation of the passenger, including the destination if available
     * @return string description of the passenger
    */
    @Override   
    public String toString() {
        if (destination == null) {
            return "Passenger id: " + id + ", destination: unknown";
        }
        return "Passenger id: " + id + ", destination: " + destination.printNoPassengers();
    }
}
