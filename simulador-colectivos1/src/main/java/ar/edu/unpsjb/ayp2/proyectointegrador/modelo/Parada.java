package ar.edu.unpsjb.ayp2.proyectointegrador.modelo;

import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;
import java.util.stream.Collectors;

/**
 * Representa una parada de colectivo en el sistema de transporte público.
 * <p>
 * Cada parada tiene un identificador, dirección, coordenadas y gestiona
 * una cola de pasajeros en espera.
 *
 * @author Miyo
 * @version 1.2
 */
public class Parada {

	// =================================================================================
	// ATRIBUTOS
	// =================================================================================

	/** Identificador único de la parada (ej: "42"). */
	private final String id;
	/** Dirección descriptiva de la parada. */
	private final String direccion;
	/** Coordenada geográfica de latitud. */
	private final double latitud;
	/** Coordenada geográfica de longitud. */
	private final double longitud;
	/** Cola de pasajeros que se encuentran esperando en esta parada. */
	private final Queue<Pasajero> pasajerosEsperando;

	// =================================================================================
	// CONSTRUCTORES
	// =================================================================================

	/**
	 * Constructor principal.
	 *
	 * @param id        El identificador único de la parada.
	 * @param direccion La dirección descriptiva de la parada.
	 * @param latitud   La latitud geográfica de la parada.
	 * @param longitud  La longitud geográfica de la parada.
	 */
	public Parada(String id, String direccion, double latitud, double longitud) {
		if (id == null || id.trim().isEmpty())
			throw new IllegalArgumentException("El ID de la parada no puede ser nulo o vacío.");
		if (direccion == null || direccion.trim().isEmpty())
			throw new IllegalArgumentException("La dirección de la parada no puede ser nula o vacía.");

		this.id = id;
		this.direccion = direccion;
		this.latitud = latitud;
		this.longitud = longitud;
		this.pasajerosEsperando = new LinkedList<>();
	}

	/**
	 * Constructor alternativo sin coordenadas (asume 0.0).
	 *
	 * @param id        El identificador único de la parada.
	 * @param direccion La dirección descriptiva de la parada.
	 */
	public Parada(String id, String direccion) {
		this(id, direccion, 0.0, 0.0);
	}

	// =================================================================================
	// MÉTODOS DE GESTIÓN DE LA COLA DE PASAJEROS
	// =================================================================================

	/**
	 * Añade un pasajero a la cola de espera de esta parada.
	 * @param pasajero El pasajero a añadir (no puede ser nulo).
	 */
	public void agregarPasajero(Pasajero pasajero) {
		if (pasajero == null) {
			throw new IllegalArgumentException("No se puede agregar un pasajero nulo a la parada.");
		}
		this.pasajerosEsperando.offer(pasajero);
	}

	/**
	 * Remueve y devuelve el siguiente pasajero de la cola de espera.
	 * @return El siguiente pasajero, o null si la cola está vacía.
	 */
	public Pasajero removerSiguientePasajero() {
		return this.pasajerosEsperando.poll();
	}
	
	// =================================================================================
	// MÉTODOS DE CONSULTA
	// =================================================================================

	/**
	 * Devuelve la cantidad de pasajeros esperando en la parada.
	 * @return El número de pasajeros en la cola.
	 */
	public int cantidadPasajerosEsperando() {
		return this.pasajerosEsperando.size();
	}

	/**
	 * [Sugerencia de Legibilidad]
	 * Genera un reporte multilínea del estado actual de la parada.
	 * @return Un String formateado con los detalles de la parada y los pasajeros esperando.
	 */
	public String getReporteDeEstado() {
		StringBuilder sb = new StringBuilder();
		sb.append("--- Estado de Parada: ").append(this.id).append(" (").append(this.direccion).append(") ---\n");
		sb.append("  Pasajeros esperando: ").append(this.cantidadPasajerosEsperando()).append("\n");

		if (!pasajerosEsperando.isEmpty()) {
			String pasajerosStr = pasajerosEsperando.stream()
					.map(p -> p.getId() + " (Destino: " + p.getParadaDestino().getId() + ")")
					.collect(Collectors.joining(", "));
			sb.append("  En cola: [").append(pasajerosStr).append("]\n");
		}
		sb.append("-----------------------------------------------------");
		return sb.toString();
	}

	// =================================================================================
	// GETTERS
	// =================================================================================

	public String getId() { return this.id; }
	public String getDireccion() { return this.direccion; }
	public double getLatitud() { return this.latitud; }
	public double getLongitud() { return this.longitud; }

	/**
	 * Devuelve una copia defensiva de la cola de pasajeros.
	 * Esto evita que el código externo modifique la cola interna de la parada.
	 *
	 * @return Una nueva cola (LinkedList) con los pasajeros que están esperando.
	 */
	public Queue<Pasajero> getPasajerosEsperando() {
		return new LinkedList<>(this.pasajerosEsperando);
	}
	
	// =================================================================================
	// MÉTODOS SOBREESCRITOS (Object)
	// =================================================================================

	@Override
	public String toString() {
		return "Parada{" + "id='" + id + '\'' + ", direccion='" + direccion + '\'' + ", esperando=" + cantidadPasajerosEsperando() + '}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Parada parada = (Parada) o;
		return id.equals(parada.id); // La igualdad se basa únicamente en el ID.
	}

	@Override
	public int hashCode() {
		return Objects.hash(id); // El hashCode también debe basarse en el ID.
	}
}