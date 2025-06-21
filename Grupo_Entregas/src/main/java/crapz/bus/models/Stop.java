package crapz.bus.models;

import java.util.ArrayList;
import java.util.List;

public class Stop {

    private int id;
    private String address;
    private List<Passenger> passengers; // passengers waiting

    // Constructor
    public Stop(int id, String address) {
        this.id = id;
        this.address = address;
        this.passengers = new ArrayList<>();
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public List<Passenger> getPassengers() {
        return passengers;
    }

    // Methods to handle passengers
    public void addPassenger(Passenger passenger) {
        if (passenger != null) {
            this.passengers.add(passenger);
        }
    }

    public void removePassengers(List<Passenger> passengersToRemove) {
        this.passengers.removeAll(passengersToRemove);
    }

    @Override
    public String toString() {
        return "Stop{"
                + "id=" + id
                + ", address='" + address + '\''
                + '}';
    }
}