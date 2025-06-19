package ar.edu.unpsjb.ayp2.proyectointegrador.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Representa una línea de transporte público. Contiene un identificador único,
 * un nombre descriptivo y un recorrido que es una secuencia ordenada de
 * paradas.
 * 
 * @author Miyo
 * @version 1.1
 */
public class Linea {
	private String id;
	private String nombre;
	private List<Parada> recorrido;

	/**
	 * Constructor principal.
	 * 
	 * @param id     El identificador único de la línea.
	 * @param nombre El nombre descriptivo de la línea.
	 * @throws IllegalArgumentException si id o nombre son nulos o vacíos.
	 */
	public Linea(String id, String nombre) {
		if (id == null || id.trim().isEmpty()) {
			throw new IllegalArgumentException("El ID de la línea no puede ser nulo o vacío.");
		}
		if (nombre == null || nombre.trim().isEmpty()) {
			throw new IllegalArgumentException("El nombre de la línea no puede ser nulo o vacío.");
		}
		this.id = id;
		this.nombre = nombre;
		this.recorrido = new ArrayList<>();
	}

	// --- Getters ---
	public String getId() {
		return id;
	}

	public String getNombre() {
		return nombre;
	}

	/**
	 * Devuelve una copia defensiva de la lista de paradas del recorrido.
	 * 
	 * @return Una nueva lista (ArrayList) con las paradas del recorrido.
	 */
	public List<Parada> getRecorrido() {
		return new ArrayList<>(this.recorrido);
	}

	// --- Métodos de Gestión del Recorrido ---

	/**
	 * Añade una parada al final del recorrido de la línea. Evita silenciosamente la
	 * adición de paradas duplicadas consecutivas.
	 * 
	 * @param parada La parada a añadir.
	 * @throws IllegalArgumentException si la parada es nula.
	 */
	public void agregarParadaAlRecorrido(Parada parada) {
		if (parada == null) {
			throw new IllegalArgumentException("No se puede agregar una parada nula al recorrido.");
		}
		// REFACTORING: Se elimina la impresión en consola. La lógica simplemente ignora
		// el duplicado.
		if (!this.recorrido.isEmpty() && this.recorrido.get(this.recorrido.size() - 1).equals(parada)) {
			return;
		}
		this.recorrido.add(parada);
	}

	/**
	 * Obtiene una parada del recorrido por su índice.
	 * 
	 * @param indice El índice de la parada (0-based).
	 * @return La parada en el índice especificado.
	 * @throws IndexOutOfBoundsException si el índice está fuera de rango.
	 */
	public Parada getParadaPorIndice(int indice) {
		if (indice < 0 || indice >= this.recorrido.size()) {
			throw new IndexOutOfBoundsException(
					"Índice fuera de rango: " + indice + ". Tamaño del recorrido: " + this.recorrido.size());
		}
		return this.recorrido.get(indice);
	}

	/**
	 * Devuelve la cantidad de paradas en el recorrido.
	 * 
	 * @return El número de paradas.
	 */
	public int longitudRecorrido() {
		return this.recorrido.size();
	}

	/**
	 * Verifica si una parada específica forma parte del recorrido de esta línea.
	 * 
	 * @param parada La parada a buscar en el recorrido.
	 * @return {@code true} si la parada está en el recorrido, {@code false} en caso
	 *         contrario.
	 */
	public boolean tieneParadaEnRecorrido(Parada parada) {
		if (parada == null) {
			return false;
		}
		return this.recorrido.contains(parada);
	}

	/**
	 * Encuentra el índice de una parada en el recorrido.
	 * 
	 * @param parada La parada cuyo índice se busca.
	 * @return El índice de la parada (0-based) o -1 si no está en el recorrido.
	 */
	public int getIndiceParada(Parada parada) {
		if (parada == null) {
			return -1;
		}
		return this.recorrido.indexOf(parada);
	}

	// --- Métodos de Lógica de Negocio sobre el Recorrido ---

	/**
	 * Devuelve la primera parada del recorrido.
	 * 
	 * @return La primera parada, o null si el recorrido está vacío.
	 */
	public Parada getPrimeraParada() {
		return this.recorrido.isEmpty() ? null : this.recorrido.get(0);
	}

	/**
	 * Devuelve la última parada del recorrido.
	 * 
	 * @return La última parada, o null si el recorrido está vacío.
	 */
	public Parada getUltimaParada() {
		return this.recorrido.isEmpty() ? null : this.recorrido.get(this.recorrido.size() - 1);
	}

	/**
	 * Determina si la parada dada es la terminal (última del recorrido).
	 * 
	 * @param parada La parada a verificar.
	 * @return true si es la última parada, false en caso contrario.
	 */
	public boolean esTerminal(Parada parada) {
		if (!this.recorrido.isEmpty() && parada != null) {
			return parada.equals(getUltimaParada());
		}
		return false;
	}

	// --- Sobrescritura de Métodos de Object ---

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Linea{id='").append(id).append('\'');
		sb.append(", nombre='").append(nombre).append('\'');
		sb.append(", recorrido=[");
		if (!this.recorrido.isEmpty()) {
			for (int i = 0; i < this.recorrido.size(); i++) {
				sb.append(this.recorrido.get(i).getId());
				if (i < this.recorrido.size() - 1) {
					sb.append(" -> ");
				}
			}
		}
		sb.append("]}");
		return sb.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Linea linea = (Linea) o;
		return Objects.equals(id, linea.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}