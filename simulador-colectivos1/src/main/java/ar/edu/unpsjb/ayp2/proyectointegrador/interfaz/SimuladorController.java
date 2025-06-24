package ar.edu.unpsjb.ayp2.proyectointegrador.interfaz;

import ar.edu.unpsjb.ayp2.proyectointegrador.datos.LectorArchivos;
import ar.edu.unpsjb.ayp2.proyectointegrador.logica.GeneradorPasajeros;
import ar.edu.unpsjb.ayp2.proyectointegrador.logica.GestorEstadisticas;
import ar.edu.unpsjb.ayp2.proyectointegrador.logica.Simulador;
import ar.edu.unpsjb.ayp2.proyectointegrador.modelo.Linea;
import ar.edu.unpsjb.ayp2.proyectointegrador.modelo.Parada;
import ar.edu.unpsjb.ayp2.proyectointegrador.modelo.Pasajero;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Controlador principal de la simulación.
 * <p>
 * Orquesta el flujo de inicialización de la aplicación, incluyendo la carga de
 * datos desde archivos, la generación de pasajeros y la configuración inicial
 * del simulador y sus componentes.
 *
 * @author Miyo
 * @author Enzo
 * @version 1.2
 */
public class SimuladorController {

	// =================================================================================
	// ATRIBUTOS
	// (Gestionan el estado y los componentes principales de la simulación)
	// =================================================================================

	private Simulador simulador;
	private Map<String, Parada> paradasCargadas;
	private Map<String, Linea> lineasCargadas;
	private Properties configProperties;
	private List<Pasajero> pasajerosGenerados;
	private GestorEstadisticas gestorEstadisticas;

	/**
	 * Constructor de la clase. La inicialización de los componentes se delega
	 * al método inicializar() para un mayor control del flujo.
	 */
	public SimuladorController() {
		// Constructor vacío, la inicialización se realiza en el método inicializar().
	}

	/**
	 * Realiza la carga y configuración completa de todos los componentes necesarios
	 * para ejecutar la simulación.
	 *
	 * @throws RuntimeException si ocurre un error fatal durante la carga de archivos.
	 */
	public void inicializar() {
		try {
			// 1. Carga de datos base desde archivos
			LectorArchivos lector = new LectorArchivos();
			lector.cargarDatosCompletos();
			this.paradasCargadas = lector.getParadasCargadas();
			this.lineasCargadas = lector.getLineasCargadas();
			this.configProperties = lector.getPropiedades();

			// 2. Creación del gestor de estadísticas y generación de pasajeros
			this.gestorEstadisticas = new GestorEstadisticas();
			GeneradorPasajeros generador = new GeneradorPasajeros(lineasCargadas, paradasCargadas, configProperties, gestorEstadisticas);
			this.pasajerosGenerados = generador.generarPasajeros();

			// 3. Creación del simulador principal
			this.simulador = new Simulador(lineasCargadas, paradasCargadas, pasajerosGenerados, gestorEstadisticas, null, configProperties);

			// 4. Lectura de configuración y inicialización de los colectivos
			int capacidadColectivo = SimuladorConfig.obtenerCapacidadColectivo(configProperties);
			int capacidadSentados = SimuladorConfig.obtenerCapacidadSentadosColectivo(configProperties);
			int recorridosPorColectivo = SimuladorConfig.obtenerRecorridosPorColectivo(configProperties);
			int capacidadParados = capacidadColectivo - capacidadSentados;

			simulador.inicializarColectivos(capacidadColectivo, capacidadSentados, capacidadParados, recorridosPorColectivo);

		} catch (IOException e) {
			System.err.println("Error fatal al cargar archivos de datos: " + e.getMessage());
			// Relanzamos como RuntimeException para detener la aplicación si los datos no se pueden cargar.
			throw new RuntimeException(e);
		}
	}

	// =================================================================================
	// GETTERS
	// (Permiten a la UI acceder a los componentes y datos del controlador)
	// =================================================================================

	public Simulador getSimulador() { return this.simulador; }
	public Map<String, Parada> getParadasCargadas() { return this.paradasCargadas; }
	public Map<String, Linea> getLineasCargadas() { return this.lineasCargadas; }
	public Properties getConfigProperties() { return this.configProperties; }
	public List<Pasajero> getPasajerosGenerados() { return this.pasajerosGenerados; }
	public GestorEstadisticas getGestorEstadisticas() { return this.gestorEstadisticas; }
}