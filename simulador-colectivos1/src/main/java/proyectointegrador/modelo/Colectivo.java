package proyectointegrador.modelo;

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
 * @version 2.7
 */
public class Colectivo {

	// =================================================================================
	// ATRIBUTOS
	// =================================================================================

	// --- Atributos de Identidad y Configuración (Inmutables tras la creación) ---
	private final String idColectivo;
	private final Linea lineaAsignada;
	private final int capacidadMaxima;
	private final int capacidadSentados;
	private final int capacidadParados;

	// --- Atributos de Estado (Cambian durante la simulación) ---
	private final List<Pasajero> pasajerosABordo;
	private Parada paradaActual;
	private int indiceParadaActualEnRecorrido;
	private int cantidadPasajerosSentados;
	private String estado;

	// --- Atributos de Control de Simulación ---
	private int recorridoActual;
	private int recorridosRestantes;
	private int pasoDeSalida;

	// =================================================================================
	// CONSTRUCTORES
	// =================================================================================

	/**
	 * Constructor base PRIVADO. Contiene toda la lógica de validación y asignación.
	 * Solo puede ser llamado por el otro constructor público de la clase.
	 */
	private Colectivo(String idColectivo, Linea lineaAsignada, int capacidadMaxima, int capacidadSentados,
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

		List<Parada> recorridoLinea = this.lineaAsignada.getRecorrido();
		if (recorridoLinea != null && !recorridoLinea.isEmpty()) {
			this.paradaActual = recorridoLinea.get(0);
			this.indiceParadaActualEnRecorrido = 0;
		} else {
			this.paradaActual = null;
			this.indiceParadaActualEnRecorrido = -1;
		}
	}

	/**
	 * ÚNICO constructor público para crear un Colectivo.
	 * Requiere todos los parámetros, incluyendo el paso de salida inicial.
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
	 */
	public void avanzarAProximaParada() {
		if (estaEnTerminal()) return;
		
		this.indiceParadaActualEnRecorrido++;
		this.paradaActual = this.lineaAsignada.getRecorrido().get(this.indiceParadaActualEnRecorrido);
	}

	/**
	 * Reinicia la posición del colectivo al inicio del recorrido de su línea.
	 */
	public void reiniciarParaNuevoRecorrido() {
		this.indiceParadaActualEnRecorrido = 0;
		if (this.lineaAsignada.getRecorrido() != null && !this.lineaAsignada.getRecorrido().isEmpty()) {
			this.paradaActual = this.lineaAsignada.getRecorrido().get(0);
		}
	}
	
	/**
	 * Actualiza los contadores de recorrido al finalizar una vuelta completa.
	 */
	public void actualizarRecorridosRestantes() {
		this.recorridoActual++;
		this.recorridosRestantes--;
	}

	// ... (El resto de la clase, getters, setters, etc., permanece igual que en la versión anterior) ...

	// =================================================================================
	// MÉTODOS DE CONSULTA DE ESTADO
	// =================================================================================

	public boolean estaEnTerminal() {
		if (this.paradaActual == null || this.lineaAsignada.getRecorrido().isEmpty()) {
			return true;
		}
		return this.indiceParadaActualEnRecorrido >= this.lineaAsignada.getRecorrido().size() - 1;
	}

	public int getAsientosDisponibles() {
		return this.capacidadSentados - this.cantidadPasajerosSentados;
	}

	public int getLugaresDePieDisponibles() {
		int pasajerosDePie = getCantidadPasajerosABordo() - this.cantidadPasajerosSentados;
		return this.capacidadParados - pasajerosDePie;
	}
	
	public String getEtiqueta() {
		return idColectivo + " (" + lineaAsignada.getNombre() + ")";
	}

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