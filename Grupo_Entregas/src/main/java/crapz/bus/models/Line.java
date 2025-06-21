package crapz.bus.models;

import java.util.ArrayList;
import java.util.List;

public class Line {

    private String code;
    private List<Stop> stops; // independent copy

    // Constructor
    public Line(String code, List<Stop> stops) {
        this.code = code;
        this.stops = new ArrayList<>(stops); // copies all elements from the "stops" list
    }

    // Getters
    public String getCode() {
        return code;
    }

    public List<Stop> getStops() {
        return stops;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Line ").append(code).append(": ");
        for (Stop stop : stops) {
            sb.append(stop.getId()).append(" -> ");
        }
        if (!stops.isEmpty()) {
            sb.setLength(sb.length() - 4); // Remove the last " -> "
        }
        return sb.toString();
    }
}