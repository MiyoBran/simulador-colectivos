package com.colectivos.data;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.colectivos.model.Stop;
import com.colectivos.setup.TransitDataGenerator;
import com.colectivos.logic.NetworkTraversal;
import com.colectivos.model.Bus;

public class DataRetrievalTest {

    public static void main (String arg[]) throws IOException {
        LoadParameters.initialize();

        Map<Integer, Stop> stops = TransitDataGenerator.loadAndPopulateStops(10);
        List<Bus> buses = TransitDataGenerator.generateBuses(stops);
        //This will go in main file, in program folder
        for (Stop stop : stops.values()) {
            if (stop.getId() == 66 || stop.getId() == 32 || stop.getId() == 39 || stop.getId() == 12|| stop.getId() == 22|| stop.getId() == 33|| stop.getId() == 20||
            stop.getId() ==  23|| stop.getId() == 25 || stop.getId() ==  24|| stop.getId() == 5|| stop.getId() == 1) {
                System.out.println(stop);
            }
        }
            System.out.println("------------------------------------------------------------------------------------------------------------------------");
        Bus bus = buses.getLast();
        //System.out.println(bus.printAllStops());
        NetworkTraversal.busTraversal(bus);

    }
}
