package ar.edu.unpsjb.ayp2.proyectointegrador.logica;

import ar.edu.unpsjb.ayp2.proyectointegrador.modelo.Colectivo;
import ar.edu.unpsjb.ayp2.proyectointegrador.modelo.Pasajero;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase de utilidad (helper) con métodos estáticos para asistir en tareas
 * comunes de la simulación.
 */
public class SimuladorHelper {

	/**
	 * Fuerza la bajada de todos los pasajeros actualmente a bordo de un colectivo.
	 * 
	 * @param colectivo El colectivo a vaciar.
	 * @return El número de pasajeros que fueron bajados.
	 */
	public static int forzarBajadaDePasajeros(Colectivo colectivo) {
		if (colectivo == null || colectivo.getCantidadPasajerosABordo() == 0) {
			return 0;
		}

		int pasajerosBajados = 0;
		List<Pasajero> pasajerosABordo = new ArrayList<>(colectivo.getPasajerosABordo());
		for (Pasajero p : pasajerosABordo) {
			if (colectivo.bajarPasajero(p)) {
				pasajerosBajados++;
			}
		}
		return pasajerosBajados;
	}

	public static int limpiarTodosLosColectivos(List<Colectivo> colectivos) {
		int totalBajados = 0;
		for (Colectivo colectivo : colectivos) {
			totalBajados += forzarBajadaDePasajeros(colectivo);
		}
		return totalBajados;
	}
}
