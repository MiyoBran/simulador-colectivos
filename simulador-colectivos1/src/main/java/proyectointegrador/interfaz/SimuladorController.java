package proyectointegrador.interfaz;

import proyectointegrador.datos.LectorArchivos;
import proyectointegrador.logica.GeneradorPasajeros;
import proyectointegrador.logica.GestorEstadisticas;
import proyectointegrador.logica.PlanificadorRutas;
import proyectointegrador.logica.Simulador;
import proyectointegrador.modelo.Linea;
import proyectointegrador.modelo.Parada;
import proyectointegrador.modelo.Pasajero;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Controlador principal de la simulación.
 * <p>
 * Orquesta el flujo de inicialización de la aplicación, incluyendo la carga de
 * datos, la construcción de la red de rutas, la generación de pasajeros y la
 * configuración inicial del simulador y sus componentes.
 *
 * @author Miyo
 * @author Enzo
 * @version 1.3
 */
public class SimuladorController {

	// =================================================================================
	// ATRIBUTOS
	// =================================================================================

	private Simulador simulador;
	private Map<String, Parada> paradasCargadas;
	private Map<String, Linea> lineasCargadas;
	private Properties configProperties;
	private List<Pasajero> pasajerosGenerados;
	private GestorEstadisticas gestorEstadisticas;
	private PlanificadorRutas planificadorRutas;

	// =================================================================================
	// CONSTRUCTOR
	// =================================================================================

	/**
	 * Constructor de la clase. La inicialización se delega al método inicializar().
	 */
	public SimuladorController() {
		// Constructor vacío.
	}
	
	// =================================================================================
	// MÉTODOS DE INICIALIZACIÓN
	// =================================================================================

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

			// 2. Creación de componentes de lógica
			this.gestorEstadisticas = new GestorEstadisticas();
			this.planificadorRutas = new PlanificadorRutas();
			this.planificadorRutas.construirGrafoDesdeLineas(lineasCargadas);
			
			// 3. Generación de pasajeros
			GeneradorPasajeros generador = new GeneradorPasajeros(lineasCargadas, configProperties, gestorEstadisticas);
			this.pasajerosGenerados = generador.generarPasajeros();

			// 4. Creación del simulador principal, inyectando todos los componentes
			this.simulador = new Simulador(lineasCargadas, paradasCargadas, pasajerosGenerados, gestorEstadisticas,
					this.planificadorRutas, configProperties);

			// 5. Lectura de configuración e inicialización de los colectivos
			int capacidadColectivo = SimuladorConfig.obtenerCapacidadColectivo(configProperties);
			int capacidadSentados = SimuladorConfig.obtenerCapacidadSentadosColectivo(configProperties);

			simulador.inicializarColectivos(capacidadColectivo, capacidadSentados);

		} catch (IOException e) {
			System.err.println("Error fatal al cargar archivos de datos: " + e.getMessage());
			throw new RuntimeException("No se pudieron cargar los datos iniciales de la simulación.", e);
		}
	}

	// =================================================================================
	// GETTERS (API para la UI)
	// =================================================================================

	public Simulador getSimulador() { return this.simulador; }
	public Map<String, Parada> getParadasCargadas() { return this.paradasCargadas; }
	public Map<String, Linea> getLineasCargadas() { return this.lineasCargadas; }
	public Properties getConfigProperties() { return this.configProperties; }
	public List<Pasajero> getPasajerosGenerados() { return this.pasajerosGenerados; }
	public GestorEstadisticas getGestorEstadisticas() { return this.gestorEstadisticas; }
	
	/**
	 * Devuelve el planificador de rutas para que la UI pueda usarlo.
	 * @return La instancia del PlanificadorRutas.
	 */
	public PlanificadorRutas getPlanificadorRutas() { return this.planificadorRutas; }
}