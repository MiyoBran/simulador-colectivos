package ar.edu.unpsjb.ayp2.proyectointegrador.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Representa a un vehículo (colectivo) que opera en una línea específica.
 * <p>
 * Mantiene su estado interno, incluyendo los pasajeros a bordo, su capacidad,
 * su posición actual en el recorrido y su estado dentro de la simulación.
 *
 * @author Miyo
 * @author Enzo
 * @version 2.6
 */
public class Colectivo {

	// =================================================================================
	// ATRIBUTOS
	// =================================================================================

	// --- Atributos de Identidad y Configuración (Inmutables tras la creación) ---
	/** Identificador único del colectivo (ej: "C1-1"). */
	private final String idColectivo;
	/** Línea de colectivo a la que está asignado y cuyo recorrido sigue. */
	private final Linea lineaAsignada;
	/** Capacidad máxima total de pasajeros (sentados + de pie). */
	private final int capacidadMaxima;
	/** Capacidad máxima de pasajeros sentados. */
	private final int capacidadSentados;
	/** Capacidad máxima de pasajeros de pie. */
	private final int capacidadParados;

	// --- Atributos de Estado (Cambian durante la simulación) ---
	/** Lista de los pasajeros que se encuentran actualmente a bordo. */
	private final List<Pasajero> pasajerosABordo;
	/** Parada actual en la que se encuentra el colectivo. */
	private Parada paradaActual;
	/** Índice de la parada actual dentro del recorrido de la línea. */
	private int indiceParadaActualEnRecorrido;
	/** Cantidad de pasajeros que actualmente viajan sentados. */
	private int cantidadPasajerosSentados;
	/** Estado descriptivo del colectivo (ej: "EN_SERVICIO", "FUERA_DE_SERVICIO"). */
	private String estado;

	// --- Atributos de Control de Simulación ---
	/** Contador de recorridos completados por el colectivo. Empieza en 1. */
	private int recorridoActual;
	/** Cantidad de recorridos que aún le quedan por realizar en la simulación. */
	private int recorridosRestantes;
	/** Paso de simulación en el que este colectivo tiene permitido salir de la terminal. */
	private int pasoDeSalida;


	// =================================================================================
	// CONSTRUCTORES
	// =================================================================================

	/**
	 * Constructor principal para un Colectivo.
	 *
	 * @param idColectivo        Identificador único.
	 * @param lineaAsignada      Línea a la que está asignado.
	 * @param capacidadMaxima    Capacidad máxima total.
	 * @param capacidadSentados  Capacidad de pasajeros sentados.
	 * @param capacidadParados   Capacidad de pasajeros de pie.
	 * @param recorridosRestantes Cantidad de recorridos a realizar.
	 */
	public Colectivo(String idColectivo, Linea lineaAsignada, int capacidadMaxima, int capacidadSentados,
			int capacidadParados, int recorridosRestantes) {
		// Validación de parámetros
		if (idColectivo == null || idColectivo.trim().isEmpty())
			throw new IllegalArgumentException("El ID del colectivo no puede ser nulo o vacío.");
		if (lineaAsignada == null)
			throw new IllegalArgumentException("La línea asignada no puede ser nula.");
		if (capacidadMaxima < 0 || capacidadSentados < 0 || capacidadParados < 0 || recorridosRestantes < 0)
			throw new IllegalArgumentException("Las capacidades y recorridos no pueden ser negativos.");

		// Asignación de atributos de configuración
		this.idColectivo = idColectivo;
		this.lineaAsignada = lineaAsignada;
		this.capacidadMaxima = capacidadMaxima;
		this.capacidadSentados = capacidadSentados;
		this.capacidadParados = capacidadParados;
		this.recorridosRestantes = recorridosRestantes;

		// Inicialización de atributos de estado
		this.pasajerosABordo = new ArrayList<>();
		this.cantidadPasajerosSentados = 0;
		this.recorridoActual = 1;
		
		// Posiciona el colectivo en la primera parada de la línea.
		List<Parada> recorridoLinea = this.lineaAsignada.getRecorrido();
		if (recorridoLinea != null && !recorridoLinea.isEmpty()) {
			this.paradaActual = recorridoLinea.get(0);
			this.indiceParadaActualEnRecorrido = 0;
		} else {
			this.paradaActual = null;
			this.indiceParadaActualEnRecorrido = -1; // Indica que no hay recorrido
		}
	}

	/**
	 * Constructor secundario que permite especificar un paso de salida inicial.
	 * Utiliza el constructor principal para la lógica de inicialización.
	 */
	public Colectivo(String idColectivo, Linea lineaAsignada, int capacidadMaxima, int capacidadSentados,
			int capacidadParados, int recorridosRestantes, int pasoDeSalida) {
		this(idColectivo, lineaAsignada, capacidadMaxima, capacidadSentados, capacidadParados, recorridosRestantes);
		this.pasoDeSalida = pasoDeSalida;
	}

	// =================================================================================
	// MÉTODOS DE LÓGICA PRINCIPAL
	// =================================================================================

	/**
	 * Intenta agregar un pasajero al colectivo.
	 * El pasajero sube si hay capacidad disponible y no está ya a bordo.
	 * Se le asigna un asiento si hay disponibles.
	 *
	 * @param pasajero El pasajero a subir.
	 * @return true si el pasajero subió con éxito, false en caso contrario.
	 */
	public boolean subirPasajero(Pasajero pasajero) {
		if (pasajero == null || getCapacidadDisponible() <= 0 || this.pasajerosABordo.contains(pasajero)) {
			return false;
		}

		if (this.cantidadPasajerosSentados < this.capacidadSentados) {
			pasajero.setViajoSentado(true);
			this.cantidadPasajerosSentados++;
		} else {
			pasajero.setViajoSentado(false);
		}
		
		this.pasajerosABordo.add(pasajero);
		return true;
	}

	/**
	 * Intenta quitar un pasajero del colectivo.
	 * Si el pasajero viajaba sentado, se libera un asiento.
	 *
	 * @param pasajero El pasajero a bajar.
	 * @return true si el pasajero estaba a bordo y fue quitado, false en caso contrario.
	 */
	public boolean bajarPasajero(Pasajero pasajero) {
		if (pasajero == null) return false;

		boolean removido = this.pasajerosABordo.remove(pasajero);
		if (removido && pasajero.isViajoSentado()) {
			this.cantidadPasajerosSentados--;
		}
		return removido;
	}

	/**
	 * Mueve el colectivo a la siguiente parada de su recorrido.
	 * No hace nada si ya se encuentra en la última parada (la terminal).
	 */
	public void avanzarAProximaParada() {
		if (estaEnTerminal()) return; // Si ya está en la terminal, no avanza.
		
		this.indiceParadaActualEnRecorrido++;
		this.paradaActual = this.lineaAsignada.getRecorrido().get(this.indiceParadaActualEnRecorrido);
	}

	/**
	 * Reinicia la posición del colectivo al inicio del recorrido de su línea.
	 * Usado cuando el colectivo finaliza un recorrido y se prepara para el siguiente.
	 */
	public void reiniciarParaNuevoRecorrido() {
		this.indiceParadaActualEnRecorrido = 0;
		if (this.lineaAsignada.getRecorrido() != null && !this.lineaAsignada.getRecorrido().isEmpty()) {
			this.paradaActual = this.lineaAsignada.getRecorrido().get(0);
		}
	}
	
	/**
	 * Actualiza los contadores de recorrido al finalizar una vuelta completa.
	 * Incrementa el contador de recorridos actuales y decrementa los restantes.
	 */
	public void actualizarRecorridosRestantes() {
		this.recorridoActual++;
		this.recorridosRestantes--;
	}

	// =================================================================================
	// MÉTODOS DE CONSULTA DE ESTADO
	// =================================================================================

	/**
	 * Verifica si el colectivo se encuentra en la última parada de su recorrido.
	 *
	 * @return true si está en la parada terminal, false en caso contrario.
	 */
	public boolean estaEnTerminal() {
		if (this.paradaActual == null || this.lineaAsignada.getRecorrido().isEmpty()) {
			return true;
		}
		return this.indiceParadaActualEnRecorrido >= this.lineaAsignada.getRecorrido().size() - 1;
	}

	/**
	 * Devuelve la cantidad de asientos disponibles en el colectivo.
	 * @return el número de asientos libres.
	 */
	public int getAsientosDisponibles() {
		return this.capacidadSentados - this.cantidadPasajerosSentados;
	}

	/**
	 * Devuelve la capacidad disponible para pasajeros de pie.
	 * @return el número de lugares de pie libres.
	 */
	public int getLugaresDePieDisponibles() {
		int pasajerosDePie = getCantidadPasajerosABordo() - this.cantidadPasajerosSentados;
		return this.capacidadParados - pasajerosDePie;
	}
	
	/**
	 * Genera una etiqueta descriptiva del colectivo.
	 * @return Una cadena con el formato "ID (Nombre de Línea)".
	 */
	public String getEtiqueta() {
		return idColectivo + " (" + lineaAsignada.getNombre() + ")";
	}

	/**
	 * [Sugerencia de Legibilidad]
	 * Genera un reporte de estado multilínea y formateado del colectivo.
	 * Ideal para imprimir un bloque de información clara en la consola.
	 *
	 * @return Un String con el estado detallado del colectivo.
	 */
	public String getReporteDeEstado() {
		String separador = "---------------------------------------------";
		StringBuilder sb = new StringBuilder();
		sb.append(separador).append("\n");
		sb.append("  Estado del Colectivo: ").append(getEtiqueta()).append("\n");
		sb.append(separador).append("\n");
		if (paradaActual != null) {
			sb.append("  > Posición: Parada ").append(paradaActual.getId()).append(" (").append(paradaActual.getDireccion()).append(")\n");
		} else {
			sb.append("  > Posición: Sin definir\n");
		}
		sb.append("  > Ocupación: ").append(getCantidadPasajerosABordo()).append(" / ").append(capacidadMaxima).append(" pasajeros\n");
		sb.append("    - Sentados: ").append(cantidadPasajerosSentados).append(" / ").append(capacidadSentados).append("\n");
		sb.append("    - De Pie: ").append(getCantidadPasajerosABordo() - cantidadPasajerosSentados).append(" / ").append(capacidadParados).append("\n");
		sb.append("  > Recorrido: ").append(recorridoActual).append(" (quedan ").append(recorridosRestantes).append(")\n");
		sb.append("  > Estado Sim: ").append(estado != null ? estado : "No definido").append("\n");
		sb.append(separador);
		return sb.toString();
	}

	// =================================================================================
	// GETTERS Y SETTERS
	// =================================================================================

	public String getIdColectivo() { return this.idColectivo; }
	public Linea getLineaAsignada() { return this.lineaAsignada; }
	public int getCapacidadMaxima() { return this.capacidadMaxima; }
	public int getCapacidadSentados() { return this.capacidadSentados; }
	public int getCapacidadParados() { return this.capacidadParados; }
	public Parada getParadaActual() { return this.paradaActual; }
	public int getIndiceParadaActualEnRecorrido() { return this.indiceParadaActualEnRecorrido; }
	public int getCantidadPasajerosABordo() { return this.pasajerosABordo.size(); }
	public int getCapacidadDisponible() { return this.capacidadMaxima - getCantidadPasajerosABordo(); }
	public List<Pasajero> getPasajerosABordo() { return new ArrayList<>(this.pasajerosABordo); }
	public int getRecorridoActual() { return this.recorridoActual; }
	public int getRecorridosRestantes() { return this.recorridosRestantes; }
	public String getEstado() { return this.estado; }
	public void setEstado(String estado) { this.estado = estado; }
	public int getPasoDeSalida() { return this.pasoDeSalida; }
	public void setPasoDeSalida(int pasoDeSalida) { this.pasoDeSalida = pasoDeSalida; }
	
	// =================================================================================
	// MÉTODOS SOBREESCRITOS (Object)
	// =================================================================================

	@Override
	public String toString() {
		return "Colectivo{" + "id='" + idColectivo + '\'' + ", linea='"
				+ (lineaAsignada != null ? lineaAsignada.getId() : "N/A") + "', pasajeros="
				+ getCantidadPasajerosABordo() + ", parada='"
				+ (paradaActual != null ? paradaActual.getId() : "N/A") + "'}";
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Colectivo colectivo = (Colectivo) o;
		return Objects.equals(idColectivo, colectivo.idColectivo);
	}

	@Override
	public int hashCode() {
		return Objects.hash(idColectivo);
	}
}