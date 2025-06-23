package ar.edu.unpsjb.ayp2.proyectointegrador.interfaz;

import ar.edu.unpsjb.ayp2.proyectointegrador.logica.Simulador;
import ar.edu.unpsjb.ayp2.proyectointegrador.modelo.Linea;
import ar.edu.unpsjb.ayp2.proyectointegrador.modelo.Parada;
import ar.edu.unpsjb.ayp2.proyectointegrador.modelo.Pasajero;
import ar.edu.unpsjb.ayp2.proyectointegrador.datos.LectorArchivos;
import ar.edu.unpsjb.ayp2.proyectointegrador.logica.GestorEstadisticas;
import java.util.*;

public class SimuladorController {
    private Simulador simulador;
    private Map<String, Parada> paradasCargadas;
    private Map<String, Linea> lineasCargadas;
    private Properties configProperties;
    private List<Pasajero> pasajerosGenerados;
    private GestorEstadisticas gestorEstadisticas;

    public SimuladorController() {
        // La inicialización real se hará en un método separado
    }

    public void inicializar() {
        try {
            LectorArchivos lector = new LectorArchivos();
            lector.cargarDatosCompletos();
            paradasCargadas = lector.getParadasCargadas();
            lineasCargadas = lector.getLineasCargadas();
            configProperties = lector.getPropiedades();
            // Crear gestor de estadísticas único
            gestorEstadisticas = new GestorEstadisticas();
            // Generar pasajeros
            ar.edu.unpsjb.ayp2.proyectointegrador.logica.GeneradorPasajeros generador =
                new ar.edu.unpsjb.ayp2.proyectointegrador.logica.GeneradorPasajeros(lineasCargadas, paradasCargadas, configProperties, gestorEstadisticas);
            pasajerosGenerados = generador.generarPasajeros();
            // Crear simulador
            simulador = new Simulador(lineasCargadas, paradasCargadas, pasajerosGenerados, gestorEstadisticas, null, configProperties);
            int capacidadColectivo = SimuladorConfig.obtenerCapacidadColectivo(configProperties);
            int capacidadSentadosColectivo = SimuladorConfig.obtenerCapacidadSentadosColectivo(configProperties);
            int recorridosPorColectivo = SimuladorConfig.obtenerRecorridosPorColectivo(configProperties);
            int capacidadParados = capacidadColectivo - capacidadSentadosColectivo;
            int cantidadColectivosPorLinea = SimuladorConfig.obtenerCantidadColectivosSimultaneosPorLinea(configProperties);
            simulador.inicializarColectivos(capacidadColectivo, capacidadSentadosColectivo, capacidadParados, recorridosPorColectivo, cantidadColectivosPorLinea);
        } catch (java.io.IOException e) {
            System.err.println("Error al cargar archivos de datos: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public Simulador getSimulador() {
        return simulador;
    }
    public Map<String, Parada> getParadasCargadas() { return paradasCargadas; }
    public Map<String, Linea> getLineasCargadas() { return lineasCargadas; }
    public Properties getConfigProperties() { return configProperties; }
    public List<Pasajero> getPasajerosGenerados() { return pasajerosGenerados; }
    public GestorEstadisticas getGestorEstadisticas() { return gestorEstadisticas; }
}