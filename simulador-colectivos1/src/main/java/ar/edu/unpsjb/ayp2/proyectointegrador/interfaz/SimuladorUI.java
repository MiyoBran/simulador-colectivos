package ar.edu.unpsjb.ayp2.proyectointegrador.interfaz;

import ar.edu.unpsjb.ayp2.proyectointegrador.modelo.Parada;
import ar.edu.unpsjb.ayp2.proyectointegrador.reporte.ReporteSimulacion;
import java.util.*;

public class SimuladorUI {
    private final SimuladorController controller;
    private final Scanner scanner = new Scanner(System.in);

    public SimuladorUI(SimuladorController controller) {
        this.controller = controller;
    }

    public void start() {
        imprimirBienvenida();
        controller.inicializar();
        var simulador = controller.getSimulador();
        System.out.println("Simulación lista para comenzar con " + controller.getPasajerosGenerados().size() + " pasajeros.");
        System.out.println("Se han inicializado " + simulador.getColectivosEnSimulacion().size() + " colectivos.");
        boolean salir = false;
        Map<String, List<String>> simulacionColectivo = new LinkedHashMap<>();
        while (!salir) {
            System.out.println("\n--- MENÚ PRINCIPAL ---");
            System.out.println("1. Ejecutar paso de simulación");
            System.out.println("2. Ejecutar simulación completa");
            System.out.println("3. Calcular ruta óptima entre paradas");
            System.out.println("4. Ver estadísticas de la simulación");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");
            String opcion = scanner.nextLine();
            switch (opcion) {
                case "1":
                    if (!simulador.isSimulacionTerminada()) {
                        List<String> eventosDelPaso = simulador.ejecutarPasoDeSimulacion();
                        Map<String, List<String>> eventosPorColectivo = SimuladorColectivosApp.mostrarEventosAgrupadosPorColectivo(eventosDelPaso);
                        for (Map.Entry<String, List<String>> entry : eventosPorColectivo.entrySet()) {
                            System.out.println("Colectivo " + entry.getKey() + ":");
                            for (String evento : entry.getValue()) {
                                System.out.println("  " + evento);
                            }
                        }
                    } else {
                        System.out.println("La simulación ya ha finalizado.");
                    }
                    break;
                case "2":
                    while (!simulador.isSimulacionTerminada()) {
                        List<String> eventosDelPaso = simulador.ejecutarPasoDeSimulacion();
                        Map<String, List<String>> eventosPorColectivo = SimuladorColectivosApp.mostrarEventosAgrupadosPorColectivo(eventosDelPaso);
                        for (Map.Entry<String, List<String>> entry : eventosPorColectivo.entrySet()) {
                            simulacionColectivo.putIfAbsent(entry.getKey(), new ArrayList<String>());
                            simulacionColectivo.get(entry.getKey()).addAll(entry.getValue());
                        }
                        System.out.println();
                    }
                    for (Map.Entry<String, List<String>> entry : simulacionColectivo.entrySet()) {
                        System.out.println("Colectivo " + entry.getKey() + ":");
                        for (String evento : entry.getValue()) {
                            System.out.println("  " + evento);
                        }
                    }
                    System.out.println("\n" + String.join("\n", simulador.getReporteFinal()));
                    System.out.println("\n--- SIMULACIÓN FINALIZADA ---");
                    ReporteSimulacion.imprimirEstadisticasCompletas(simulador);
                    break;
                case "3":
                    System.out.print("Ingrese ID de parada origen: ");
                    String idOrigen = scanner.nextLine();
                    System.out.print("Ingrese ID de parada destino: ");
                    String idDestino = scanner.nextLine();
                    Parada origen = controller.getParadasCargadas().get(idOrigen);
                    Parada destino = controller.getParadasCargadas().get(idDestino);
                    if (origen == null || destino == null) {
                        System.out.println("Parada origen o destino no encontrada.");
                    } else if (simulador.getPlanificadorRutas() == null) {
                        System.out.println("Funcionalidad de rutas no disponible en este simulador.");
                    } else {
                        List<Parada> ruta = simulador.getPlanificadorRutas().calcularRutaOptima(origen, destino);
                        if (ruta.isEmpty()) {
                            System.out.println("No existe ruta entre las paradas seleccionadas.");
                        } else {
                            System.out.println("Ruta óptima: ");
                            for (Parada p : ruta) {
                                System.out.println("- " + p.getId() + " | " + p.getDireccion());
                            }
                        }
                    }
                    break;
                case "4":
                    if (simulador.getGestorEstadisticas() == null) {
                        System.out.println("Funcionalidad de estadísticas no disponible en este simulador.");
                    } else {
                        ReporteSimulacion.imprimirEstadisticasCompletas(simulador);
                        simulador.imprimirReportePasajeros();
                    }
                    break;
                case "0":
                    salir = true;
                    break;
                default:
                    System.out.println("Opción no válida. Intente nuevamente.");
            }
            if (simulador.isSimulacionTerminada()) {
                System.out.println("\nLa simulación ha finalizado. Puede consultar estadísticas o salir.");
            }
        }
    }

    private void imprimirBienvenida() {
        System.out.println("===================================");
        System.out.println("  Simulador de Colectivos Urbanos  ");
        System.out.println("===================================\n");
    }
}