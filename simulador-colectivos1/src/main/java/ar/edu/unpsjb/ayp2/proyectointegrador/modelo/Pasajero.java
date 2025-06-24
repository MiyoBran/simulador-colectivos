package ar.edu.unpsjb.ayp2.proyectointegrador.modelo;

import java.util.Objects;
import java.util.UUID;

/**
 * Representa a un usuario del sistema de transporte que desea viajar desde una
 * parada de origen a una de destino. Contiene atributos para gestionar su
 * estado durante la simulación y calcular su satisfacción.
 *
 * @author Miyo
 * @version 1.1
 */
public class Pasajero {

	// =================================================================================
	// ATRIBUTOS
	// =================================================================================

	// --- Atributos de Identidad (Inmutables) ---
	/** Identificador único del pasajero. */
	private final String id;
	/** Parada donde el pasajero inicia su viaje. */
	private final Parada paradaOrigen;
	/** Parada donde el pasajero desea finalizar su viaje. */
	private final Parada paradaDestino;

	// --- Atributos de Estado de la Simulación ---
	/** Cuántos colectivos llenos pasaron antes de que pudiera subir. */
	private int colectivosEsperados;
	/** Indica si el pasajero consiguió un asiento durante su viaje. */
	private boolean viajoSentado;
	/** Indica si el pasajero logró abordar algún colectivo. */
	private boolean pudoSubir;
	/** Indica si fue bajado en una terminal sin haber llegado a su destino. */
	private boolean bajadaForzosa;
	/** Almacena la calificación de satisfacción final del pasajero (1-5). */
	private int satisfaccion;
	
	// Atributo comentado mantenido según solicitud (no es de manejo de tiempo)
	// private boolean subioAlPrimerColectivoQuePaso;

	// =================================================================================
	// CONSTRUCTORES
	// =================================================================================

	/**
	 * Constructor principal para crear un pasajero con un ID específico.
	 *
	 * @param id            El identificador único del pasajero.
	 * @param paradaOrigen  La parada de origen del pasajero.
	 * @param paradaDestino La parada de destino del pasajero.
	 */
	public Pasajero(String id, Parada paradaOrigen, Parada paradaDestino) {
		if (id == null || id.trim().isEmpty())
			throw new IllegalArgumentException("El ID del pasajero no puede ser nulo o vacío.");
		if (paradaOrigen == null)
			throw new IllegalArgumentException("La parada de origen no puede ser nula.");
		if (paradaDestino == null)
			throw new IllegalArgumentException("La parada de destino no puede ser nula.");
		if (paradaOrigen.equals(paradaDestino))
			throw new IllegalArgumentException("La parada de origen y destino no pueden ser la misma.");

		this.id = id;
		this.paradaOrigen = paradaOrigen;
		this.paradaDestino = paradaDestino;
		
		this.colectivosEsperados = 0;
		// this.subioAlPrimerColectivoQuePaso = false;
		this.viajoSentado = false;
		this.pudoSubir = false;
		this.bajadaForzosa = false;
		this.satisfaccion = 0;
	}

	/**
	 * Constructor de conveniencia que genera un ID aleatorio.
	 *
	 * @param paradaOrigen  La parada de origen del pasajero.
	 * @param paradaDestino La parada de destino del pasajero.
	 */
	public Pasajero(Parada paradaOrigen, Parada paradaDestino) {
		this(UUID.randomUUID().toString(), paradaOrigen, paradaDestino);
	}

	// =================================================================================
	// MÉTODOS PÚBLICOS
	// =================================================================================

	/**
	 * Incrementa el contador de colectivos que el pasajero tuvo que esperar.
	 */
	public void incrementarColectivosEsperados() {
		this.colectivosEsperados++;
	}

	/**
	 * Calcula la satisfacción del pasajero (escala 1-5) según los resultados de su viaje.
	 * @return Un valor de satisfacción entre 1 y 5.
	 */
	public int calcularSatisfaccion() {
		if (this.satisfaccion != 0) {
			// Si la satisfacción fue forzada (ej. bajada en terminal), devuelve ese valor.
			return this.satisfaccion;
		}
		
		if (!pudoSubir) {
			return 1; // La peor calificación si nunca pudo subir.
		}

		switch (colectivosEsperados) {
			case 0: // No esperó ningún colectivo extra
				return viajoSentado ? 5 : 4; // 5 si fue sentado, 4 si fue de pie.
			case 1: // Esperó 1 colectivo
				return 3;
			default: // Esperó 2 o más colectivos
				return 2;
		}
	}

	/**
	 * Reinicia el estado de viaje del pasajero a sus valores iniciales.
	 */
	public void resetearEstadoViaje() {
		this.colectivosEsperados = 0;
		// this.subioAlPrimerColectivoQuePaso = false;
		this.viajoSentado = false;
		this.pudoSubir = false;
		this.bajadaForzosa = false;
		this.satisfaccion = 0;
	}

	// =================================================================================
	// GETTERS Y SETTERS
	// =================================================================================

	public String getId() { return this.id; }
	public Parada getParadaOrigen() { return this.paradaOrigen; }
	public Parada getParadaDestino() { return this.paradaDestino; }
	public int getColectivosEsperados() { return this.colectivosEsperados; }
	public boolean isViajoSentado() { return this.viajoSentado; }
	public void setViajoSentado(boolean viajoSentado) { this.viajoSentado = viajoSentado; }
	public boolean isPudoSubir() { return this.pudoSubir; }
	public void setPudoSubir(boolean pudoSubir) { this.pudoSubir = pudoSubir; }
	public boolean isBajadaForzosa() { return this.bajadaForzosa; }
	public void setBajadaForzosa(boolean valor) { this.bajadaForzosa = valor; }
	public int getSatisfaccion() { return this.satisfaccion; }
	public void setSatisfaccion(int valor) { this.satisfaccion = valor; }

	// =================================================================================
	// MÉTODOS SOBREESCRITOS (Object)
	// =================================================================================
	
	@Override
	public String toString() {
		return "Pasajero '" + id + "' [Origen: " + paradaOrigen.getId() + ", Destino: " + paradaDestino.getId() + "]";
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Pasajero pasajero = (Pasajero) o;
		return Objects.equals(id, pasajero.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}