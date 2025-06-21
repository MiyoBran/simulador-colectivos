package com.colectivos.model;

import java.util.Collections;
import java.util.List;

/**
 * Represents a bus line in the transit network.
 * A line is identified by a unique code and an ordered list of {@link Stop} objects that the buses follow.
 * This class is intended to use in simulations where buses are assigned to a line
*/
public class Line {
    private final String code;
    private final List<Stop> stops;

    /**
     * Constructs a Line with the given code and list of stops.
     * @param code  the unique identifier of the line
     * @param stops the ordered list of stops for the line
    */
    public Line(String code, List<Stop> stops) {
        this.code = code;
        this.stops = stops;
    }

    /**
     * Returns the line's unique code
     * @return the line's code
    */
    public String getCode() {
        return code;
    }

    /**
     * Returns the first stop in the line
     * @return the first {@link Stop} in the line
    */
    public Stop getFirstStop() {
        return stops.getFirst();
    }

    /**
     * Returns the last stop in the line
     * @return the last {@link Stop} in the line
     */
    public Stop getLastStop() {
        return stops.getLast();
    }
        
    /**
     * Returns an unmodifiable list of stops on this line
     * @return the list of {@link Stop} objects
    */
    public List<Stop> getStops() {
        return Collections.unmodifiableList(stops);
    }


    /**
     * Returns a string representation of the line, including its code and stops.
     * @return a string describing the line
    */
    @Override
    public String toString() {
        return "Line " + code + ", stops: " + stops;
    }
    
}
