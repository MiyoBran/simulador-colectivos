package crapz.bus.models;

public class Passenger {

    private int id;
    private Stop destination;
    private Stop origin; // Additional attribute

    // Constructor
    public Passenger(int id, Stop origin, Stop destination) {
        this.id = id;
        this.origin = origin;
        this.destination = destination;
    }

    // Getters
    public int getId() {
        return id;
    }

    public Stop getOrigin() {
        return origin;
    }

    public Stop getDestination() {
        return destination;
    }

    @Override
    public String toString() {
        return "Passenger [ID: " + id + ", Origin: " + origin.getId() + ", Destination: " + destination.getId() + "]";
    }
}
