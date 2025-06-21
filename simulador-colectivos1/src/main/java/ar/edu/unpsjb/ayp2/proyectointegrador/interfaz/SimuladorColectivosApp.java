package ar.edu.unpsjb.ayp2.proyectointegrador.interfaz;

import ar.edu.unpsjb.ayp2.proyectointegrador.logica.Simulador;
import ar.edu.unpsjb.ayp2.proyectointegrador.modelo.Linea;
import ar.edu.unpsjb.ayp2.proyectointegrador.modelo.Parada;
import ar.edu.unpsjb.ayp2.proyectointegrador.modelo.Pasajero;
import ar.edu.unpsjb.ayp2.proyectointegrador.datos.LectorArchivos;
import ar.edu.unpsjb.ayp2.proyectointegrador.logica.GeneradorPasajeros;

import java.util.*;

public class SimuladorColectivosApp {

    public static void main(String[] args) {
        imprimirBienvenida();

        LectorArchivos lector = new LectorArchivos();

        try {
            // 1. Carga de Datos y Configuración
            lector.cargarDatosCompletos();
            Map<String, Parada> paradasCargadas = lector.getParadasCargadas();
            Map<String, Linea> lineasCargadas = lector.getLineasCargadas();

            if (paradasCargadas.isEmpty() || lineasCargadas.isEmpty()) {
                System.err.println("No se pudieron cargar paradas o líneas. No se puede iniciar la simulación.");
                return;
            }

            Properties configProperties = lector.getPropiedades();

            // 2. Generación de Entidades
            GeneradorPasajeros generador = new GeneradorPasajeros(lineasCargadas, paradasCargadas, configProperties);
            List<Pasajero> pasajerosGenerados = generador.generarPasajeros();
            System.out.println("Simulación lista para comenzar con " + pasajerosGenerados.size() + " pasajeros.");

            int capacidadColectivo = obtenerCapacidadColectivo(configProperties);

            // 3. Creación e Inicialización del Simulador
            Simulador simulador = new Simulador(lineasCargadas, paradasCargadas, pasajerosGenerados);
            simulador.inicializarColectivos(capacidadColectivo);
            System.out.println("Se han inicializado " + simulador.getColectivosEnSimulacion().size() + " colectivos.");

            // 4. Menú interactivo para nuevas funcionalidades
            Scanner scanner = new Scanner(System.in);
            boolean salir = false;
            int paso = 1;
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
                            System.out.println("\n--- Paso de Simulación " + paso + " ---\n");
                            mostrarEventosAgrupadosPorColectivo(eventosDelPaso);
                            paso++;
                        } else {
                            System.out.println("La simulación ya ha finalizado.");
                        }
                        break;
                    case "2":
                        while (!simulador.isSimulacionTerminada()) {
                            List<String> eventosDelPaso = simulador.ejecutarPasoDeSimulacion();
                            System.out.println("\n--- Paso de Simulación " + paso + " ---\n");
                            mostrarEventosAgrupadosPorColectivo(eventosDelPaso);
                            paso++;
                        }
                        System.out.println("\n" + String.join("\n", simulador.getReporteFinal()));
                        System.out.println("\n--- SIMULACIÓN FINALIZADA ---");
                        break;
                    case "3":
                        System.out.print("Ingrese ID de parada origen: ");
                        String idOrigen = scanner.nextLine();
                        System.out.print("Ingrese ID de parada destino: ");
                        String idDestino = scanner.nextLine();
                        Parada origen = paradasCargadas.get(idOrigen);
                        Parada destino = paradasCargadas.get(idDestino);
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
                            var gestor = simulador.getGestorEstadisticas();
                            System.out.println("\n--- Estadísticas de la Simulación ---");
                            System.out.println("Pasajeros transportados: " + gestor.getPasajerosTransportados());
                            System.out.println("Tiempo promedio de espera: " + gestor.getTiempoEsperaPromedio());
                            System.out.println("Tiempo promedio de viaje: " + gestor.getTiempoViajePromedio());
                            System.out.println("Satisfacción promedio: " + gestor.getSatisfaccionPromedio());
                            System.out.println("% Satisfechos: " + gestor.getPorcentajeSatisfechos());
                            System.out.println("% Insatisfechos: " + gestor.getPorcentajeInsatisfechos());
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
            scanner.close();

        } catch (Exception e) {
            System.err.println("Ocurrió un error durante la simulación: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Agrupa y muestra los eventos de la simulación por colectivo, mejorando la legibilidad.
     * Cada bloque de eventos comienza con el nombre del colectivo, seguido de los eventos de ese colectivo.
     */
    private static void mostrarEventosAgrupadosPorColectivo(List<String> eventos) {
        // Mapa: Colectivo ID -> Lista de eventos
        LinkedHashMap<String, List<String>> eventosPorColectivo = new LinkedHashMap<>();
        String colectivoActual = null;

        for (String evento : eventos) {
            String idDetectado = detectarColectivoEnLinea(evento);
            if (idDetectado != null) {
                colectivoActual = idDetectado;
                eventosPorColectivo.putIfAbsent(colectivoActual, new ArrayList<>());
                eventosPorColectivo.get(colectivoActual).add(evento.trim());
            } else if (colectivoActual != null) {
                eventosPorColectivo.get(colectivoActual).add("  " + evento.trim());
            } else {
                // Eventos generales o de sistema (poco común, pero se muestran igual)
                eventosPorColectivo.putIfAbsent("GENERAL", new ArrayList<>());
                eventosPorColectivo.get("GENERAL").add(evento.trim());
            }
        }

        // Mostrar eventos agrupados
        for (Map.Entry<String, List<String>> entry : eventosPorColectivo.entrySet()) {
            for (String linea : entry.getValue()) {
                System.out.println(linea);
            }
            System.out.println(); // Separador entre colectivos
        }
    }

    /**
     * Detecta el ID del colectivo en la línea de evento.
     * Asume que los eventos relevantes de colectivo arrancan con "Colectivo <ID>" o "  Colectivo <ID>"
     * Devuelve el ID (ej: "C2-1"), o null si no detecta un colectivo.
     */
    private static String detectarColectivoEnLinea(String linea) {
        // Ejemplo: "Colectivo C2-1 de la línea ...", "  Colectivo C2-1 ha llegado a la terminal."
        String pattern = "Colectivo (C\\d+-\\d+)";
        java.util.regex.Matcher matcher = java.util.regex.Pattern.compile(pattern).matcher(linea);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    /**
     * Extrae la capacidad de colectivos de las propiedades de configuración, o retorna un valor por defecto.
     */
    private static int obtenerCapacidadColectivo(Properties configProperties) {
        String valor = configProperties.getProperty("capacidad.colectivo");
        if (valor != null) {
            try {
                int capacidad = Integer.parseInt(valor);
                if (capacidad > 0) {
                    return capacidad;
                }
            } catch (NumberFormatException e) {
                System.err.println("Valor de capacidad.colectivo inválido en configuración, se usará 30 por defecto.");
            }
        }
        return 30; // Valor por defecto
    }

    /**
     * Imprime la bienvenida al usuario.
     */
    private static void imprimirBienvenida() {
        System.out.println("===================================");
        System.out.println("  Simulador de Colectivos Urbanos  ");
        System.out.println("===================================\n");
    }
}