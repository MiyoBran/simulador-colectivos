package proyectointegrador.modelo;

import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

/**
 * Representa una parada de colectivo. Cada parada tiene un identificador único,
 * una dirección, coordenadas geográficas y gestiona una cola de pasajeros en
 * espera y sus propias estadísticas básicas.
 *
 * @author Miyo
 * @author Enzo
 * 
 * @version 1.1
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

	// --- Atributos de Estadísticas ---
	/** Tiempo promedio de espera de los pasajeros que abordaron en esta parada. */
	private double tiempoEsperaPromedio;
	/** Cantidad total de pasajeros que han abordado en esta parada. */
	private int pasajerosAbordados;
	/** Cantidad total de colectivos que han pasado por esta parada. */
	private int colectivosPasados;

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
		if (id == null || id.trim().isEmpty()) {
			throw new IllegalArgumentException("El ID de la parada no puede ser nulo o vacío.");
		}
		if (direccion == null || direccion.trim().isEmpty()) {
			throw new IllegalArgumentException("La dirección de la parada no puede ser nula o vacía.");
		}
		this.id = id;
		this.direccion = direccion;
		this.latitud = latitud;
		this.longitud = longitud;
		this.pasajerosEsperando = new LinkedList<>();
		this.tiempoEsperaPromedio = 0.0;
		this.pasajerosAbordados = 0;
		this.colectivosPasados = 0;
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
	// MÉTODOS DE GESTIÓN DE LA COLA
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

	/**
	 * Devuelve el siguiente pasajero de la cola sin removerlo.
	 * @return El siguiente pasajero, o null si la cola está vacía.
	 */
	public Pasajero peekSiguientePasajero() {
		return this.pasajerosEsperando.peek();
	}
	
	/**
	 * Verifica si hay pasajeros esperando en la parada.
	 * @return true si hay pasajeros esperando, false en caso contrario.
	 */
	public boolean hayPasajerosEsperando() {
		return !this.pasajerosEsperando.isEmpty();
	}

	/**
	 * Devuelve la cantidad de pasajeros esperando en la parada.
	 * @return El número de pasajeros en la cola.
	 */
	public int cantidadPasajerosEsperando() {
		return this.pasajerosEsperando.size();
	}

	// =================================================================================
	// MÉTODOS DE GESTIÓN DE ESTADÍSTICAS
	// =================================================================================

	/**
	 * Incrementa en uno la cantidad de pasajeros que han abordado en esta parada.
	 */
	public void incrementarPasajerosAbordados() {
		this.pasajerosAbordados++;
	}

	/**
	 * Incrementa en uno la cantidad de colectivos que han pasado por esta parada.
	 */
	public void incrementarColectivosPasados() {
		this.colectivosPasados++;
	}


	// =================================================================================
	// GETTERS
	// =================================================================================

	public String getId() { return this.id; }
	public String getDireccion() { return this.direccion; }
	public double getLatitud() { return this.latitud; }
	public double getLongitud() { return this.longitud; }
	public int getPasajerosAbordados() { return this.pasajerosAbordados; }
	public int getColectivosPasados() { return this.colectivosPasados; }
	public double getTiempoEsperaPromedio() { return this.tiempoEsperaPromedio; }
	
	/**
	 * Devuelve la cola de pasajeros esperando en la parada.
	 * ADVERTENCIA: Devuelve la referencia original. Modificarla externamente afectará
	 * el estado de la parada. Para una versión segura, ver getCopiaPasajerosEsperando().
	 * @return la cola de pasajeros.
	 */
	public Queue<Pasajero> getPasajerosEsperando() {
		return this.pasajerosEsperando;
	}

	// =================================================================================
	// MÉTODOS SOBREESCRITOS (Object)
	// =================================================================================

	@Override
	public String toString() {
		return "Parada{" + "id='" + id + '\'' + ", direccion='" + direccion + '\'' + ", esperando=" + pasajerosEsperando.size() + '}';
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