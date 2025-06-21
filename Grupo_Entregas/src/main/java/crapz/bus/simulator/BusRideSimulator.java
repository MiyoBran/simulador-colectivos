package crapz.bus.simulator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import crapz.bus.data.ObjetcLists;
import crapz.bus.models.Bus;
import crapz.bus.models.Line;
import crapz.bus.models.Stop;
import crapz.bus.models.Passenger;

public class BusRideSimulator {

    public void simulate() {
        // Obtener las lineas cargadas y la capacidad por defecto del colectivo
        Map<String, Line> lineasDisponibles = ObjetcLists.getAllLines();
        int capacidadColectivo = ObjetcLists.getDefaultBusCapacity();

        // Crear un colectivo por cada linea disponible
        List<Bus> colectivos = new ArrayList<>();
        int colectivoId = 1;
        for (Entry<String, Line> entry : lineasDisponibles.entrySet()) {
            Line linea = entry.getValue();
            Bus colectivo = new Bus(colectivoId++, linea, capacidadColectivo);
            colectivos.add(colectivo);
        }

        // --- SIMULACIoN DEL RECORRIDO PARA CADA COLECTIVO ---
        for (Bus colectivo : colectivos) {
            System.out.println("\n>>> SIMULACION PARA EL COLECTIVO ID: " + colectivo.getId() + " (Line: "
                    + colectivo.getLine().getCode() + ") <<<");
            boolean enRecorrido = true;
            int pasoSimulacion = 1;

            while (enRecorrido) {
                Stop paradaActual = colectivo.getCurrentStop();
                System.out.println("\nPaso " + pasoSimulacion + ": Bus " + colectivo.getId()
                        + " - llega a Parada " + paradaActual.getId() + " (" + paradaActual.getAddress()
                        + ") [Pasajeros a bordo: " + colectivo.getPassengerCountOnBoard() + "/"
                        + colectivo.getMaxCapacity() + "]");

                // Mostrar detalles de los pasajeros esperando en la parada
                List<Passenger> pasajerosEsperando = paradaActual.getPassengers();
                System.out.println(
                        "  + Pasajeros esperando en Parada " + paradaActual.getId() + ": " + pasajerosEsperando.size());
                if (!pasajerosEsperando.isEmpty()) {
                    for (Passenger p : pasajerosEsperando) {
                        System.out.println(
                                "     - (Pasajero ID: " + p.getId() + ", Destino: " + p.getDestination().getId() + ")");
                    }
                } else {
                    System.out.println("   * No hay pasajeros esperando en esta parada.");
                }

                // 1. Bajar pasajeros en la parada actual
                List<Passenger> pasajerosBajados = colectivo.dropOffPassengers();
                if (!pasajerosBajados.isEmpty()) {
                    System.out.println("   *** BAJAN PASAJEROS ***");
                    for (Passenger p : pasajerosBajados) {
                        System.out.println("     - Bajo (Pasajero ID: " + p.getId() + ", Destino: "
                                + p.getDestination().getId() + ")");
                    }
                } else {
                    System.out.println("   * No bajan pasajeros en esta parada.");
                }

                // 2. Subir pasajeros en la parada actual
                List<Passenger> pasajerosSubidos = colectivo.pickUpPassengers();
                if (!pasajerosSubidos.isEmpty()) {
                    System.out.println("   *** SUBEN PASAJEROS ***");
                    for (Passenger p : pasajerosSubidos) {
                        System.out.println(
                                "     - Subio (Pasajero ID: " + p.getId() + ", Destino: " + p.getDestination().getId() + ")");
                    }
                } else {
                    System.out.println("   * No suben pasajeros en esta parada.");
                }

                // Mostrar cuantos pasajeros quedan esperando despues de la operacion
                System.out.println("  + Pasajeros restantes esperando en Parada " + paradaActual.getId() + ": "
                        + paradaActual.getPassengers().size());

                // 3. Avanzar a la siguiente parada
                enRecorrido = colectivo.advanceToNextStop();
                if (!enRecorrido) {
                    System.out.println("\n>>> Bus " + colectivo.getId() + " ha llegado al final de su linea: "
                            + colectivo.getLine().getCode() + " <<<");
                    if (colectivo.getPassengerCountOnBoard() > 0) {
                        System.out.println("¡Atencion! Aun quedan " + colectivo.getPassengerCountOnBoard()
                                + " pasajeros a bordo al final del recorrido.");
                    }
                }
                pasoSimulacion++;
            }
        }
    }

}
