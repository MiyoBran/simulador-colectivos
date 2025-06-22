package ar.edu.unpsjb.ayp2.proyectointegrador.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Representa a un usuario del sistema de transporte que desea viajar desde una
 * parada de origen a una de destino. Contiene atributos para gestionar su
 * estado durante la simulación, como los tiempos de espera y la satisfacción
 * del viaje.
 * 
 * @author Miyo
 * @version 1.1
 * 
 */
public class Pasajero {
	// Atributo para Incremento 1 (ID del pasajero)
	private String id;
	// Atributo para Incremento 1 (Parada de origen)
	private Parada paradaOrigen;
	// Atributo para Incremento 1 (Parada de destino)
	private Parada paradaDestino;

	// Atributos para Incremento 2 (Índice de Satisfacción)
	private int colectivosEsperados; // Cuántos colectivos llenos pasaron antes de poder subir
	private boolean subioAlPrimerColectivoQuePaso; // Si subió al primer colectivo que llegó (no necesariamente el
													// primero que esperaba si hubo llenos)
	private boolean viajoSentado; // Simplificación, se podría determinar de otra forma
	private boolean pudoSubir; // Si finalmente logró subir a algún colectivo

	// Indica si el pasajero fue bajado forzosamente en la terminal (no llegó a su destino)
	private boolean bajadaForzosa = false;

	// Incremento 2: atributos para simulación avanzada
	/** Tiempo total de espera en minutos antes de abordar un colectivo. */
	private int tiempoEspera;
	/** Lista de IDs de colectivos observados mientras esperaba. */
	private List<String> colectivosObservados;
	/** Tiempo total de viaje en minutos. */
	private int tiempoViaje;

	// --- GETTERS Y SETTERS ---
    // Satisfacción del pasajero (0-100)
    private int satisfaccion = 100;
    public void setSatisfaccion(int valor) {
        this.satisfaccion = valor;
    }
    public int getSatisfaccion() {
        return this.satisfaccion;
    }

	/**
	 * Constructor para crear un pasajero con un ID aleatorio.
	 * 
	 * @param paradaOrigen  La parada de origen del pasajero.
	 * @param paradaDestino La parada de destino del pasajero.
	 * 
	 */
	public Pasajero(Parada paradaOrigen, Parada paradaDestino) {
		this(UUID.randomUUID().toString(), paradaOrigen, paradaDestino);
	}

	/**
	 * Constructor para crear un pasajero con un ID específico. Este constructor es
	 * útil para pruebas o simulaciones donde se necesita un ID fijo.
	 * 
	 * @param id            El identificador único del pasajero.
	 * @param paradaOrigen  La parada de origen del pasajero.
	 * @param paradaDestino La parada de destino del pasajero.
	 */
	public Pasajero(String id, Parada paradaOrigen, Parada paradaDestino) {
		if (id == null || id.trim().isEmpty()) {
			throw new IllegalArgumentException("El ID del pasajero no puede ser nulo o vacío.");
		}
		if (paradaOrigen == null) {
			throw new IllegalArgumentException("La parada de origen no puede ser nula.");
		}
		if (paradaDestino == null) {
			throw new IllegalArgumentException("La parada de destino no puede ser nula.");
		}
		if (paradaOrigen.equals(paradaDestino)) {
			throw new IllegalArgumentException("La parada de origen y destino no pueden ser la misma.");
		}

		this.id = id;
		this.paradaOrigen = paradaOrigen;
		this.paradaDestino = paradaDestino;

		// Inicialización para Incremento 2
		this.colectivosEsperados = 0;
		this.subioAlPrimerColectivoQuePaso = false;
		this.viajoSentado = false;
		this.pudoSubir = false;

		// Inicialización para simulación avanzada
		this.tiempoEspera = 0;
		this.colectivosObservados = new ArrayList<>();
		this.tiempoViaje = 0;
	}

	public String getId() {
		return id;
	}

	public Parada getParadaOrigen() {
		return paradaOrigen;
	}

	public Parada getParadaDestino() {
		return paradaDestino;
	}

	public int getColectivosEsperados() {
		return colectivosEsperados;
	}

	public void incrementarColectivosEsperados() {
		this.colectivosEsperados++;
	}

	public boolean isSubioAlPrimerColectivoQuePaso() {
		return subioAlPrimerColectivoQuePaso;
	}

	public void setSubioAlPrimerColectivoQuePaso(boolean subioAlPrimerColectivoQuePaso) {
		this.subioAlPrimerColectivoQuePaso = subioAlPrimerColectivoQuePaso;
	}

	public boolean isViajoSentado() {
		return viajoSentado;
	}

	public void setViajoSentado(boolean viajoSentado) {
		this.viajoSentado = viajoSentado;
	}

	public boolean isPudoSubir() {
		return pudoSubir;
	}

	public void setPudoSubir(boolean pudoSubir) {
		this.pudoSubir = pudoSubir;
	}

	public boolean isBajadaForzosa() {
		return bajadaForzosa;
	}
	public void setBajadaForzosa(boolean valor) {
		this.bajadaForzosa = valor;
	}

	/**
	 * Devuelve el tiempo total de espera del pasajero.
	 * 
	 * @return tiempo de espera en minutos.
	 */
	public int getTiempoEspera() {
		return tiempoEspera;
	}

	/**
	 * Suma minutos al tiempo de espera del pasajero.
	 * 
	 * @param minutos minutos a sumar.
	 */
	public void agregarTiempoEspera(int minutos) {
		this.tiempoEspera += minutos;
	}

	/**
	 * Devuelve la lista de IDs de colectivos observados.
	 * 
	 * @return lista de IDs.
	 */
	public List<String> getColectivosObservados() {
		return new ArrayList<>(colectivosObservados);
	}

	/**
	 * Agrega un colectivo observado a la lista.
	 * 
	 * @param idColectivo ID del colectivo observado.
	 */
	public void agregarColectivoObservado(String idColectivo) {
		this.colectivosObservados.add(idColectivo);
	}

	/**
	 * Devuelve el tiempo total de viaje del pasajero.
	 * 
	 * @return tiempo de viaje en minutos.
	 */
	public int getTiempoViaje() {
		return tiempoViaje;
	}

	/**
	 * Suma minutos al tiempo de viaje del pasajero.
	 * 
	 * @param minutos minutos a sumar.
	 */
	public void agregarTiempoViaje(int minutos) {
		this.tiempoViaje += minutos;
	}

	/**
	 * Calcula el índice de satisfacción del pasajero según criterios de la simulación.
	 * 
	 * @return valor de satisfacción (0-100).
	 */
	public int calcularSatisfaccion() {
		int satisfaccion = 100;
		if (!pudoSubir) {
			satisfaccion = 0;
		} else {
			satisfaccion -= colectivosEsperados * 10;
			if (!viajoSentado)
				satisfaccion -= 15;
			if (!subioAlPrimerColectivoQuePaso)
				satisfaccion -= 10;
			satisfaccion -= tiempoEspera / 2;
			satisfaccion -= tiempoViaje / 3;
			if (satisfaccion < 0)
				satisfaccion = 0;
		}
		return satisfaccion;
	}

	/**
	 * Reinicia el estado del pasajero para una nueva simulación o intento de viaje.
	 * Útil si los mismos objetos Pasajero se reutilizan.
	 */
	public void resetearEstadoViaje() {
		this.colectivosEsperados = 0;
		this.subioAlPrimerColectivoQuePaso = false;
		this.viajoSentado = false;
		this.pudoSubir = false;

		this.tiempoEspera = 0;
		this.colectivosObservados.clear();
		this.tiempoViaje = 0;
	}

	// --- MÉTODOS DE OBJECT ---
	@Override
	public String toString() {
		// REFACTORING: Salida más limpia y legible.
		return "Pasajero '" + id + "' [Origen: " + paradaOrigen.getId() + ", Destino: " + paradaDestino.getId() + "]";
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Pasajero pasajero = (Pasajero) o;
		return Objects.equals(id, pasajero.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}