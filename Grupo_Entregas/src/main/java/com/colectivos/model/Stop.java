package com.colectivos.model;
import java.util.Collections;
import java.util.List;

/**
 * This class represents a bus stop.
 * A Stop object has a unique ID, a physical address, and a list of passengers currently waiting at the stop.
 * Passengers can be added or removed, and the class provides a string representation of the stop
 * with or without its passengers
 * The class is designed to be used in simulations of bus network traversal, where buses
 * pick up and drop off passengers at different stops.
*/
public class Stop {
    private final int id;
    private final String address;
    private List<Passenger> passengers;

    /**
     * Constructs a new {@code Stop} with a unique ID, address and a list of passengers waiting on it.
     * @param id the unique identifier of the stop
     * @param address the string which represents the stop's address
     * @param passengers the list of passengers waiting on the stop
    */
    public Stop(int id, String address, List<Passenger> passengers) {
        this.id = id;
        this.address = address;
        this.passengers = passengers;
    }

    /**
     * Returns the unique ID of the stop
     * @return the stop's ID
    */
    public int getId() {
        return id;
    }

    /**
     * Returns the unique address of the stop
     * @return the stop's address
    */
    public String getAddress() {
        return address;
    }

    /**
     * Returns an unmodifiable list of passengers currently waiting at the stop.
     * @return a read-only list of passengers
    */
    public List<Passenger> getPassengers() {
        return Collections.unmodifiableList(passengers);
    }
    
    /**
     * Returns the number of passengers currently waiting at the stop.
     * @return the passenger count
    */
    public int passengerAmount() {
        return passengers.size();
    }

    /**
     * Adds a single passenger to the stop
     * @param passenger Passenger object to be added to the list
    */
    public void addPassengers(Passenger passenger) {
        passengers.add(passenger);
    }

    /**
     * Sets a list of passengers and replaces the current one
     * @param passengers new list of passengers
    */
    public void setPassengers(List<Passenger> passengers) {
        if (passengers == null) {
            return;
        }
        this.passengers = passengers;
    }

    /**
     * Removes all the passengers included in the toRemove list, unless the list is null
     * @param toRemove list of passengers to be removed
    */
    public void removePassengers(List<Passenger> toRemove) {
        if (toRemove == null) {
            return;
        }
        passengers.removeAll(toRemove);
    }

    /**
     * Checks whether a Stop object is equal to another Stop object
     * @param object the object to be compared to
     * @return true if objects are equal, false otherwise
    */
    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        Stop stop = (Stop) object;
        return id == stop.id && address.equals(stop.address);
    }

    /**
     * Returns a string representing the stop without including any passengers
     * @return stop's ID and address
    */
    public String printNoPassengers() {
    return String.format("Stop %d, address: %s", id, address);
    }

    /**
     * Returns a string representation of the stop, including waiting passengers if there are any.
     * @return stop's ID, address and all passengers waiting, if any
    */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Stop %d, address: %s", id, address));
    
        if (passengers == null || passengers.isEmpty()) {
            sb.append("\n[No passengers waiting on the stop]");
        } else {
            sb.append("\n[Passengers waiting on the stop]");
            for (Passenger passenger : passengers) {
                sb.append("\n").append(passenger.toString());
            }
        }
        return sb.toString();
    }

}
