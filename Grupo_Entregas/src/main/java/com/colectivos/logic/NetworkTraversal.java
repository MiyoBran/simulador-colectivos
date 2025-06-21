package com.colectivos.logic;

import java.util.List;

import com.colectivos.model.Bus;
import com.colectivos.model.Passenger;

/**
 * Provides logic to simulate the traversal of a bus through its assigned line.
 * 
 * This class contains a utility method that execute the behavior of a {@link Bus} moving through its stops,
 * picking up and dropping off passengers as it progresses from the first to the last stop on its {@link com.colectivos.model.Line}.
 *
 * The class is intended for simulation purposes and uses console output to display the state of the bus and its passengers at each stop.
*/
public class NetworkTraversal {

    /**
     * Simulates the full traversal of a bus through its line of stops.
     * 
     * At each stop, the bus drops off passengers whose destination matches the stop,
     * and picks up new passengers waiting at the stop with valid destinations on the line.
     * 
     * The method prints the state of the bus and passenger activity to the console at each stop, including:
     *    Which passengers got off the bus
     *    Which passengers boarded the bus
     *    The current stop and next stop
     * @param currentBus the {@link Bus} to simulate traversal for; must be initialized and assigned a {@link com.colectivos.model.Line}
    */
    public static void busTraversal(Bus currentBus) {
        int stopIteration = 1;

        System.out.println("===== Stop " + stopIteration + " =====");
        System.out.println("Bus " + currentBus.getId() + " is at standby\n");

        currentBus.traverseLine();
        System.out.println("Next stop: " + currentBus.getCurrentStop().printNoPassengers());

        while (currentBus.getCurrentStop() != null) {
            stopIteration++;
            System.out.println("===== Stop " + stopIteration + " =====");
            System.out.println(currentBus);

            if (!currentBus.getPassengers().isEmpty()) {
                List<Passenger> removedPassengers = currentBus.removePassengers(currentBus.getCurrentStop());

                if (removedPassengers.isEmpty()) {
                    System.out.println("[No passengers got off the bus]");
                }   else {
                    System.out.println("[Passengers who got off the bus]");
                    for (Passenger person : removedPassengers) {
                        System.out.println(person);
                    }
                }
            }

            List<Passenger> hoppedOn = currentBus.addPassengers(currentBus.getCurrentStop());
            currentBus.getCurrentStop().removePassengers(hoppedOn);
            if (hoppedOn.isEmpty()) {
                System.out.println("[No passengers got on the bus]");
            }   else {
                System.out.println("[Passengers who got on the bus]");
                for (Passenger person : hoppedOn) {
                    System.out.println(person);
                }
            }

            if (currentBus.getCurrentStop().equals(currentBus.getLine().getLastStop())) {
                break;
            }

            currentBus.traverseLine();
            System.out.println("\nNext stop: " + currentBus.getCurrentStop().printNoPassengers());
        }

        System.out.println("===== The bus " + currentBus.getId() +" has ended its route =====");

    }
}