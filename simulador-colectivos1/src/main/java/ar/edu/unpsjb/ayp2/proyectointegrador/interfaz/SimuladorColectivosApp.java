package ar.edu.unpsjb.ayp2.proyectointegrador.interfaz;

/**
 * Clase principal y punto de entrada para la aplicación "Simulador de
 * Colectivos Urbanos".
 * <p>
 * Su única responsabilidad es inicializar y conectar los componentes clave de
 * la arquitectura (el Controlador y la Interfaz de Usuario) y luego iniciar la
 * aplicación.
 *
 * @author Miyo
 * @author Enzo
 * @version 1.2 // Versión refactorizada y clarificada
 */
public final class SimuladorColectivosApp {

	/**
	 * Constructor privado para evitar que la clase de utilidad sea instanciada.
	 */
	private SimuladorColectivosApp() {
	}

	/**
	 * Método principal que inicia la aplicación del simulador.
	 *
	 * @param args Argumentos de línea de comandos (no utilizados).
	 */
	public static void main(String[] args) {
		// DECLARACIÓN DE COMPONENTES
		SimuladorController controller = new SimuladorController(); // Crea el controlador que gestiona la lógica
		SimuladorUI ui = new SimuladorUI(controller); // Crea la UI y le inyecta el controlador

		// INICIO DE LA APLICACIÓN
		ui.start(); // Inicia la interfaz de usuario
	}

}