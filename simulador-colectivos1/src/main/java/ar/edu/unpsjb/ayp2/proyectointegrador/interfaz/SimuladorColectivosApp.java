package ar.edu.unpsjb.ayp2.proyectointegrador.interfaz;

import ar.edu.unpsjb.ayp2.proyectointegrador.logica.Simulador;
import ar.edu.unpsjb.ayp2.proyectointegrador.modelo.Linea;
import ar.edu.unpsjb.ayp2.proyectointegrador.modelo.Parada;
import ar.edu.unpsjb.ayp2.proyectointegrador.modelo.Pasajero;
import ar.edu.unpsjb.ayp2.proyectointegrador.datos.LectorArchivos;
import ar.edu.unpsjb.ayp2.proyectointegrador.logica.GeneradorPasajeros;
import ar.edu.unpsjb.ayp2.proyectointegrador.reporte.ReporteSimulacion;

import java.util.*;

/**
 * Clase principal de la aplicación del Simulador de Colectivos Urbanos. Se
 * encarga de la carga de datos, configuración de la simulación y la interacción
 * con el usuario a través de un menú por consola.
 * 
 * @author MiyoBran
 * @version 1.0
 */
public class SimuladorColectivosApp {
    public static void main(String[] args) {
        SimuladorController controller = new SimuladorController();
        SimuladorUI ui = new SimuladorUI(controller);
        ui.start();
    }

    /**
     * Este método ha quedado potencialmente inutilizado tras la integración de ReporteSimulacion.imprimirReportePasajeros.
     * Verificar si sigue siendo utilizado en el proyecto antes de eliminarlo o refactorizarlo.
     * Agrupa y muestra los eventos de la simulación por colectivo, mejorando la
     * legibilidad. Cada bloque de eventos comienza con el nombre del colectivo,
     * seguido de los eventos de ese colectivo.
     *
     * @param eventos Lista de eventos generados durante la simulación.
     * @return Un mapa donde la clave es el ID del colectivo (String) y el valor es una lista de eventos (List<String>)
     *         asociados a ese colectivo. Los eventos generales o de sistema se agrupan bajo la clave "GENERAL".
     *         Este mapa permite acceder fácilmente a todos los eventos relacionados con cada colectivo para su presentación o análisis.
     */
    public static Map<String, List<String>> mostrarEventosAgrupadosPorColectivo(List<String> eventos) {
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

        return eventosPorColectivo;
    }

    /**
     * Detecta el ID del colectivo en la línea de evento. Asume que los eventos
     * relevantes de colectivo arrancan con "Colectivo <ID>" o " Colectivo <ID>"
     * Devuelve el ID (ej: "C2-1"), o null si no detecta un colectivo.
     */
    public static String detectarColectivoEnLinea(String linea) {
        // Ejemplo: "Colectivo C2-1 de la línea ...", " Colectivo C2-1 ha llegado a la
        // terminal."
        String pattern = "Colectivo (C\\d+-\\d+)";
        java.util.regex.Matcher matcher = java.util.regex.Pattern.compile(pattern).matcher(linea);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    /**
     * Extrae la capacidad de colectivos de las propiedades de configuración, o
     * retorna un valor por defecto.
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