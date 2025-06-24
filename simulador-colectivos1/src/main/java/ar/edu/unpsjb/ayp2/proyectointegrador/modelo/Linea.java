package ar.edu.unpsjb.ayp2.proyectointegrador.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Representa una línea de transporte público. Contiene un identificador único,
 * un nombre descriptivo y un recorrido como secuencia ordenada de paradas.
 *
 * @author Miyo
 * @version 1.2
 */
public class Linea {

	// =================================================================================
	// ATRIBUTOS
	// =================================================================================

	/** Identificador único de la línea (ej: "1"). */
	private final String id;
	/** Nombre descriptivo de la línea (ej: "Línea 1 - Ida"). */
	private final String nombre;
	/** Secuencia ordenada de paradas que componen el recorrido de la línea. */
	private final List<Parada> recorrido;

	// =================================================================================
	// CONSTRUCTOR
	// =================================================================================

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

	// =================================================================================
	// MÉTODOS DE GESTIÓN DEL RECORRIDO
	// =================================================================================

	/**
	 * Añade una parada al final del recorrido.
	 * Evita la adición de paradas nulas o duplicados consecutivos.
	 *
	 * @param parada La parada a añadir.
	 */
	public void agregarParadaAlRecorrido(Parada parada) {
		if (parada == null) {
			throw new IllegalArgumentException("No se puede agregar una parada nula al recorrido.");
		}
		// Evita agregar la misma parada dos veces seguidas.
		if (!this.recorrido.isEmpty() && this.recorrido.get(this.recorrido.size() - 1).equals(parada)) {
			return;
		}
		this.recorrido.add(parada);
	}

	// =================================================================================
	// MÉTODOS DE CONSULTA
	// =================================================================================
	
	/**
	 * Verifica si una parada específica forma parte del recorrido de esta línea.
	 *
	 * @param parada La parada a buscar en el recorrido.
	 * @return {@code true} si la parada está en el recorrido, {@code false} en caso contrario.
	 */
	public boolean tieneParadaEnRecorrido(Parada parada) {
		return parada != null && this.recorrido.contains(parada);
	}

	/**
	 * Determina si la parada dada es la terminal (última del recorrido).
	 *
	 * @param parada La parada a verificar.
	 * @return true si es la última parada, false en caso contrario.
	 */
	public boolean esTerminal(Parada parada) {
		return parada != null && !this.recorrido.isEmpty() && parada.equals(getUltimaParada());
	}

	/**
	 * [Sugerencia de Legibilidad]
	 * Genera un reporte multilínea y formateado del recorrido de la línea.
	 *
	 * @return Un String con el detalle de la línea y todas sus paradas.
	 */
	public String getReporteRecorrido() {
		StringBuilder sb = new StringBuilder();
		sb.append("--- Detalle de Línea: ").append(this.nombre).append(" (ID: ").append(this.id).append(") ---\n");
		if (recorrido.isEmpty()) {
			sb.append("  Recorrido vacío.\n");
		} else {
			sb.append("  Recorrido con ").append(recorrido.size()).append(" paradas:\n");
			for (int i = 0; i < recorrido.size(); i++) {
				Parada p = recorrido.get(i);
				sb.append(String.format("    %d. Parada %s - %s\n", i + 1, p.getId(), p.getDireccion()));
			}
		}
		sb.append("----------------------------------------------------");
		return sb.toString();
	}

	// =================================================================================
	// GETTERS
	// =================================================================================

	public String getId() { return this.id; }
	public String getNombre() { return this.nombre; }
	public List<Parada> getRecorrido() { return new ArrayList<>(this.recorrido); }
	public int getIndiceParada(Parada parada) { return parada == null ? -1 : this.recorrido.indexOf(parada); }
	public Parada getPrimeraParada() { return this.recorrido.isEmpty() ? null : this.recorrido.get(0); }
	public Parada getUltimaParada() { return this.recorrido.isEmpty() ? null : this.recorrido.get(this.recorrido.size() - 1); }

	// =================================================================================
	// MÉTODOS SOBREESCRITOS (Object)
	// =================================================================================

	@Override
	public String toString() {
		// Versión mejorada usando Streams y String.join para mayor claridad.
		String recorridoStr = this.recorrido.stream()
				.map(Parada::getId)
				.collect(Collectors.joining(" -> "));
		
		return String.format("Linea{id='%s', nombre='%s', recorrido=[%s]}", id, nombre, recorridoStr);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Linea linea = (Linea) o;
		return Objects.equals(id, linea.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}