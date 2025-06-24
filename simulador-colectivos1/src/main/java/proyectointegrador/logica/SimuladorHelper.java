package proyectointegrador.logica;

import java.util.ArrayList;
import java.util.List;

import proyectointegrador.modelo.Colectivo;
import proyectointegrador.modelo.Pasajero;

/**
 * Clase de utilidad (helper) con métodos estáticos para asistir en tareas
 * comunes de la simulación, como la manipulación de grupos de entidades.
 */
public final class SimuladorHelper {

	/**
	 * Constructor privado para prevenir la instanciación de esta clase de utilidad.
	 */
	private SimuladorHelper() {
	}

	/**
	 * Fuerza la bajada de todos los pasajeros actualmente a bordo de un colectivo.
	 * <p>
	 * Crea una copia de la lista de pasajeros para evitar errores de modificación
	 * concurrente mientras se itera.
	 *
	 * @param colectivo El colectivo a vaciar. No puede ser nulo.
	 * @return El número de pasajeros que fueron bajados.
	 */
	public static int forzarBajadaDePasajeros(final Colectivo colectivo) {
		if (colectivo == null || colectivo.getCantidadPasajerosABordo() == 0) {
			return 0;
		}

		int pasajerosBajados = 0;
		// Se crea una copia para poder modificar la lista original mientras se itera.
		final List<Pasajero> pasajerosABordo = new ArrayList<>(colectivo.getPasajerosABordo());
		
		for (final Pasajero p : pasajerosABordo) {
			if (colectivo.bajarPasajero(p)) {
				pasajerosBajados++;
			}
		}
		return pasajerosBajados;
	}

	/**
	 * Aplica la función de forzar bajada a todos los colectivos de una lista.
	 *
	 * @param colectivos La lista de colectivos a limpiar. No puede ser nula.
	 * @return El número total de pasajeros que fueron bajados de todos los colectivos.
	 */
	public static int limpiarTodosLosColectivos(final List<Colectivo> colectivos) {
		int totalBajados = 0;
		for (final Colectivo colectivo : colectivos) {
			totalBajados += forzarBajadaDePasajeros(colectivo);
		}
		return totalBajados;
	}
}