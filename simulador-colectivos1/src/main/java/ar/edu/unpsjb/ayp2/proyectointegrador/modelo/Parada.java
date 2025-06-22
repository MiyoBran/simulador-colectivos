package ar.edu.unpsjb.ayp2.proyectointegrador.modelo;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Representa una parada de colectivo en el sistema de transporte público. Cada
 * parada tiene un identificador único, una dirección descriptiva, coordenadas
 * geográficas (latitud y longitud) y una cola de pasajeros esperando para
 * abordar.
 * 
 * @author Miyo
 * @version 1.1
 * 
 */
public class Parada {
	private String id; // Manteniendo como String para consistencia con tu LectorArchivos actual
	private String direccion; // Renombrado de 'nombre' a 'direccion' para claridad
	private Queue<Pasajero> pasajerosEsperando;
	private double latitud;
	private double longitud;

	/**
	 * Tiempo promedio de espera de los pasajeros en esta parada (para estadísticas).
	 */
	private double tiempoEsperaPromedio;
	/**
	 * Cantidad de pasajeros que han abordado en esta parada (para estadísticas).
	 */
	private int pasajerosAbordados;
	/**
	 * Cantidad de colectivos que han pasado por esta parada (para estadísticas).
	 */
	private int colectivosPasados;

	/**
	 * Constructor principal.
	 * 
	 * @param id        El identificador único de la parada.
	 * @param direccion La dirección descriptiva de la parada.
	 * @param latitud   La latitud geográfica de la parada.
	 * @param longitud  La longitud geográfica de la parada.
	 * @throws IllegalArgumentException si id o direccion son nulos o vacíos.
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
	 * Constructor alternativo sin latitud/longitud (asume 0.0). Útil para tests o
	 * casos donde no se disponga de coordenadas.
	 * 
	 * @param id        El identificador único de la parada.
	 * @param direccion La dirección descriptiva de la parada.
	 */
	public Parada(String id, String direccion) {
		this(id, direccion, 0.0, 0.0);
	}

	public String getId() {
		return id;
	}

	public String getDireccion() {
		return direccion;
	}

	public double getLatitud() {
		return latitud;
	}

	public double getLongitud() {
		return longitud;
	}

	/**
	 * Añade un pasajero a la cola de espera de esta parada.
	 * 
	 * @param pasajero El pasajero a añadir.
	 * @throws IllegalArgumentException si el pasajero es nulo.
	 */
	public void agregarPasajero(Pasajero pasajero) {
		if (pasajero == null) {
			throw new IllegalArgumentException("No se puede agregar un pasajero nulo a la parada.");
		}
		this.pasajerosEsperando.offer(pasajero);
	}

	/**
	 * Remueve y devuelve el siguiente pasajero de la cola de espera.
	 * 
	 * @return El siguiente pasajero, o null si la cola está vacía.
	 */
	public Pasajero removerSiguientePasajero() {
		return this.pasajerosEsperando.poll();
	}

	/**
	 * Verifica si hay pasajeros esperando en la parada.
	 * 
	 * @return true si hay pasajeros esperando, false en caso contrario.
	 */
	public boolean hayPasajerosEsperando() {
		return !this.pasajerosEsperando.isEmpty();
	}

	/**
	 * Devuelve la cantidad de pasajeros esperando en la parada.
	 * 
	 * @return El número de pasajeros en la cola.
	 */
	public int cantidadPasajerosEsperando() {
		return this.pasajerosEsperando.size();
	}

	/**
	 * Verifica si un pasajero específico se encuentra en la cola de espera de esta
	 * parada.
	 * 
	 * @param pasajero El pasajero a buscar.
	 * @return true si el pasajero está en la cola, false en caso contrario.
	 */
	public boolean tienePasajeroEnCola(Pasajero pasajero) {
		return this.pasajerosEsperando.contains(pasajero);
	}

	/**
	 * Incrementa la cantidad de pasajeros que han abordado en esta parada.
	 */
	public void incrementarPasajerosAbordados() {
		this.pasajerosAbordados++;
	}

	/**
	 * Incrementa la cantidad de colectivos que han pasado por esta parada.
	 */
	public void incrementarColectivosPasados() {
		this.colectivosPasados++;
	}

	/**
	 * Actualiza el tiempo promedio de espera de los pasajeros en esta parada.
	 * 
	 * @param nuevoTiempoEspera tiempo de espera del pasajero que abordó.
	 */
	public void actualizarTiempoEsperaPromedio(double nuevoTiempoEspera) {
		if (pasajerosAbordados == 0) {
			this.tiempoEsperaPromedio = nuevoTiempoEspera;
		} else {
			this.tiempoEsperaPromedio = ((this.tiempoEsperaPromedio * (pasajerosAbordados - 1)) + nuevoTiempoEspera)
					/ pasajerosAbordados;
		}
	}

	/**
	 * Devuelve el tiempo promedio de espera en la parada.
	 * 
	 * @return tiempo promedio de espera.
	 */
	public double getTiempoEsperaPromedio() {
		return tiempoEsperaPromedio;
	}

	/**
	 * Devuelve la cantidad de pasajeros que han abordado en esta parada.
	 * 
	 * @return cantidad de pasajeros abordados.
	 */
	public int getPasajerosAbordados() {
		return pasajerosAbordados;
	}

	/**
	 * Devuelve la cantidad de colectivos que han pasado por esta parada.
	 * 
	 * @return cantidad de colectivos pasados.
	 */
	public int getColectivosPasados() {
		return colectivosPasados;
	}

	/**
	 * Devuelve la cola de pasajeros esperando en la parada.
	 * 
	 * @return cola de pasajeros.
	 */
	public Queue<Pasajero> getPasajerosEsperando() {
		return pasajerosEsperando;
	}

	@Override
	public String toString() {
		return "Parada{" + "id='" + id + '\'' + ", direccion='" + direccion + '\'' + ", latitud=" + latitud
				+ ", longitud=" + longitud + ", pasajerosEsperando=" + pasajerosEsperando.size() + '}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Parada parada = (Parada) o;
		// La igualdad se basa únicamente en el ID, que es único.
		return id.equals(parada.id);
	}

	@Override
	public int hashCode() {
		// El hashCode también debe basarse en el ID.
		return id.hashCode();
	}
}