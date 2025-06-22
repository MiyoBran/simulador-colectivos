package ar.edu.unpsjb.ayp2.proyectointegrador.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Representa a un vehículo de transporte (colectivo) que opera en una línea
 * específica. Mantiene su estado interno, incluyendo los pasajeros a bordo, su
 * capacidad y su posición actual dentro del recorrido de la línea asignada.
 *
 * @author Miyo
 * @version 2.1
 * 
 */
public class Colectivo {
	private String idColectivo;
	private Linea lineaAsignada;
	private int capacidadMaxima;
	private List<Pasajero> pasajerosABordo;
	private Parada paradaActual;
	private int indiceParadaActualEnRecorrido; // Índice de la paradaActual en el recorrido de la línea

	/** Capacidad máxima de pasajeros sentados en el colectivo. */
	private int capacidadSentados;
	/** Capacidad máxima de pasajeros de pie en el colectivo. */
	private int capacidadParados;
	/** Cantidad de recorridos que debe realizar este colectivo en la simulación. */
	private int recorridosRestantes;
	/** Estado del colectivo: EN_SERVICIO, FUERA_DE_SERVICIO, EN_ESPERA, etc. */
	private String estado;
	/** Tiempo (en minutos) hasta la próxima salida, útil para la simulación. */
	private int tiempoHastaProximaSalida;

	/**
	 * Constructor para un Colectivo.
	 * 
	 * @param idColectivo     El identificador único del colectivo.
	 * @param lineaAsignada   La línea de colectivo a la que está asignado.
	 * @param capacidadMaxima La capacidad máxima de pasajeros del colectivo.
	 * @throws IllegalArgumentException si alguno de los parámetros es inválido
	 *                                  (e.g., id nulo, línea nula, capacidad
	 *                                  negativa).
	 */
	public Colectivo(String idColectivo, Linea lineaAsignada, int capacidadMaxima, int capacidadSentados,
			int capacidadParados, int recorridosRestantes) {
		if (idColectivo == null || idColectivo.trim().isEmpty()) {
			throw new IllegalArgumentException("El ID del colectivo no puede ser nulo o vacío.");
		}
		if (lineaAsignada == null) {
			throw new IllegalArgumentException("La línea asignada no puede ser nula.");
		}
		if (capacidadMaxima < 0 || capacidadSentados < 0 || capacidadParados < 0 || recorridosRestantes < 0) {
			throw new IllegalArgumentException("Las capacidades y recorridos no pueden ser negativos.");
		}

		this.idColectivo = idColectivo;
		this.lineaAsignada = lineaAsignada;
		this.capacidadMaxima = capacidadMaxima;
		this.capacidadSentados = capacidadSentados;
		this.capacidadParados = capacidadParados;
		this.recorridosRestantes = recorridosRestantes;
		this.pasajerosABordo = new ArrayList<>();

		// Initialize paradaActual and indiceParadaActualEnRecorrido as before
		List<Parada> recorridoLinea = this.lineaAsignada.getRecorrido();
		if (recorridoLinea != null && !recorridoLinea.isEmpty()) {
			this.paradaActual = recorridoLinea.get(0);
			this.indiceParadaActualEnRecorrido = 0;
		} else {
			this.paradaActual = null;
			this.indiceParadaActualEnRecorrido = -1;
		}
	}

	// Getters
	public String getIdColectivo() {
		return idColectivo;
	}

	public Linea getLineaAsignada() {
		return lineaAsignada;
	}

	public int getCapacidadMaxima() {
		return capacidadMaxima;
	}

	public Parada getParadaActual() {
		return paradaActual;
	}

	public int getIndiceParadaActualEnRecorrido() {
		return indiceParadaActualEnRecorrido;
	}

	/**
	 * Devuelve una copia de la lista de pasajeros actualmente a bordo.
	 * 
	 * @return Una nueva lista con los pasajeros a bordo.
	 */
	public List<Pasajero> getPasajerosABordo() {
		return new ArrayList<>(this.pasajerosABordo);
	}

	public int getCantidadPasajerosABordo() {
		return this.pasajerosABordo.size();
	}

	public int getCapacidadDisponible() {
		return this.capacidadMaxima - getCantidadPasajerosABordo();
	}

	/**
	 * Devuelve la capacidad máxima de pasajeros sentados.
	 * 
	 * @return Capacidad de sentados.
	 */
	public int getCapacidadSentados() {
		return capacidadSentados;
	}

	/**
	 * Devuelve la capacidad máxima de pasajeros de pie.
	 * 
	 * @return Capacidad de parados.
	 */
	public int getCapacidadParados() {
		return capacidadParados;
	}

	/**
	 * Establece la cantidad de recorridos restantes para este colectivo.
	 * 
	 * @param recorridosRestantes cantidad de recorridos.
	 */
	public void setRecorridosRestantes(int recorridosRestantes) {
		this.recorridosRestantes = recorridosRestantes;
	}

	/**
	 * Devuelve la cantidad de recorridos restantes.
	 * 
	 * @return recorridos restantes.
	 */
	public int getRecorridosRestantes() {
		return recorridosRestantes;
	}

	/**
	 * Devuelve el estado actual del colectivo.
	 * 
	 * @return estado.
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * Establece el estado del colectivo.
	 * 
	 * @param estado nuevo estado.
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}

	/**
	 * Devuelve el tiempo hasta la próxima salida.
	 * 
	 * @return tiempo en minutos.
	 */
	public int getTiempoHastaProximaSalida() {
		return tiempoHastaProximaSalida;
	}

	/**
	 * Establece el tiempo hasta la próxima salida.
	 * 
	 * @param tiempo minutos hasta la próxima salida.
	 */
	public void setTiempoHastaProximaSalida(int tiempo) {
		this.tiempoHastaProximaSalida = tiempo;
	}

	/**
	 * Intenta subir un pasajero al colectivo.
	 * 
	 * @param pasajero El pasajero a subir.
	 * @return true si el pasajero subió exitosamente, false en caso contrario
	 *         (nulo, sin capacidad, ya a bordo).
	 */
	public boolean subirPasajero(Pasajero pasajero) {
		if (pasajero == null || getCapacidadDisponible() <= 0 || this.pasajerosABordo.contains(pasajero)) {
			return false;
		}
		this.pasajerosABordo.add(pasajero);
		return true;
	}

	/**
	 * Intenta bajar un pasajero del colectivo.
	 * 
	 * @param pasajero El pasajero a bajar.
	 * @return true si el pasajero bajó exitosamente, false si no estaba a bordo o
	 *         es nulo.
	 */
	public boolean bajarPasajero(Pasajero pasajero) {
		if (pasajero == null) {
			return false;
		}
		return this.pasajerosABordo.remove(pasajero);
	}

	/**
	 * Avanza el colectivo a la siguiente parada en su recorrido. Si el colectivo ya
	 * está en la última parada, su estado interno se actualizará pero no se moverá
	 * más allá. La próxima llamada a estaEnTerminal() devolverá true.
	 */
	public void avanzarAProximaParada() {
		if (this.paradaActual == null || this.lineaAsignada.getRecorrido().isEmpty()) {
			this.indiceParadaActualEnRecorrido = -1;
			return;
		}

		// Solo avanza el índice si no está ya en la última parada.
		if (this.indiceParadaActualEnRecorrido < this.lineaAsignada.getRecorrido().size() - 1) {
			this.indiceParadaActualEnRecorrido++;
			this.paradaActual = this.lineaAsignada.getRecorrido().get(this.indiceParadaActualEnRecorrido);
		}
	}

	/**
	 * Verifica si el colectivo se encuentra en la última parada de su recorrido.
	 * 
	 * @return true si está en la parada terminal, false en caso contrario o si no
	 *         tiene recorrido.
	 */
	public boolean estaEnTerminal() {
		// Considera terminal si no hay recorrido o la parada actual es nula.
		if (this.paradaActual == null || this.lineaAsignada.getRecorrido().isEmpty()) {
			return true;
		}
		// Compara el índice actual con el último índice válido del recorrido.
		return this.indiceParadaActualEnRecorrido >= this.lineaAsignada.getRecorrido().size() - 1;
	}

	@Override
	public String toString() {
		return "Colectivo{" + "idColectivo='" + idColectivo + '\'' + ", lineaAsignada="
				+ (lineaAsignada != null ? lineaAsignada.getId() : "N/A") + ", capacidadMaxima=" + capacidadMaxima
				+ ", pasajerosABordo=" + getCantidadPasajerosABordo() + ", paradaActual="
				+ (paradaActual != null ? paradaActual.getId() : "N/A") + ", indiceParadaActual="
				+ indiceParadaActualEnRecorrido + '}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Colectivo colectivo = (Colectivo) o;
		return Objects.equals(idColectivo, colectivo.idColectivo);
	}

	@Override
	public int hashCode() {
		return Objects.hash(idColectivo);
	}
}