package proyectointegrador.logica;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import proyectointegrador.modelo.Colectivo;
import proyectointegrador.modelo.Pasajero;

/**
 * GestorEstadisticas centraliza la recolección y cálculo de métricas de la simulación.
 * Permite registrar eventos relevantes y consultar estadísticas agregadas.
 *
 * @author Miyen
 * @author Enzo
 * 
 * @version 2.0
 */
public class GestorEstadisticas {

	// =================================================================================
	// ATRIBUTOS
	// =================================================================================

	// --- Estadísticas Generales ---
	private final List<Pasajero> pasajerosRegistrados;
	private int pasajerosTransportados;
	private int pasajerosSatisfechos;
	private int pasajerosInsatisfechos;
	private final Map<String, Integer> ocupacionPorColectivo;

	// --- Estadísticas de Satisfacción (Anexo I) ---
	private final Map<Integer, Integer> conteoCalificaciones; // Calificación (1-5) -> Cantidad
	private int sumaCalificaciones;
	private int totalPasajerosCalificados;

	// --- Estadísticas de Ocupación de Colectivos (Anexo II) ---
	private final Map<String, List<Double>> ocupacionesPorColectivo; // idColectivo -> lista de % ocupación
	private final Map<String, Integer> capacidadPorColectivo;

	// =================================================================================
	// CONSTRUCTOR Y RESET
	// =================================================================================

	public GestorEstadisticas() {
		this.pasajerosRegistrados = new ArrayList<>();
		this.ocupacionPorColectivo = new HashMap<>();
		this.conteoCalificaciones = new HashMap<>();
		this.ocupacionesPorColectivo = new HashMap<>();
		this.capacidadPorColectivo = new HashMap<>();
		this.reset();
	}

	/** Resetear todas las estadísticas a su estado inicial. */
	public void reset() {
		this.pasajerosRegistrados.clear();
		this.ocupacionPorColectivo.clear();
		this.conteoCalificaciones.clear();
		this.ocupacionesPorColectivo.clear();
		this.capacidadPorColectivo.clear();
		this.pasajerosTransportados = 0;
		this.pasajerosSatisfechos = 0;
		this.pasajerosInsatisfechos = 0;
		this.sumaCalificaciones = 0;
		this.totalPasajerosCalificados = 0;
	}

	// =================================================================================
	// MÉTODOS DE REGISTRO
	// =================================================================================

	/** Registra un nuevo pasajero al inicio de la simulación. */
	public void registrarPasajero(Pasajero p) {
		this.pasajerosRegistrados.add(p);
	}

	/** Registra que un pasajero fue transportado y procesa su satisfacción. */
	public void registrarTransporte(Pasajero p) {
		this.pasajerosTransportados++;
		
		int satisfaccion = p.calcularSatisfaccion(); // Asumimos que devuelve 1-5
		
		if (satisfaccion >= 3) {
			this.pasajerosSatisfechos++;
		} else {
			this.pasajerosInsatisfechos++;
		}
		
		this.conteoCalificaciones.put(satisfaccion, this.conteoCalificaciones.getOrDefault(satisfaccion, 0) + 1);
		this.sumaCalificaciones += satisfaccion;
		this.totalPasajerosCalificados++;
	}

	/** Registrar la capacidad máxima de un colectivo para el cálculo de ocupación. */
	public void registrarCapacidadColectivo(String idColectivo, int capacidadMaxima) {
		this.capacidadPorColectivo.put(idColectivo, capacidadMaxima);
	}

	/** Registrar la ocupación de un colectivo en un tramo del recorrido. */
	public void registrarOcupacionTramo(String idColectivo, int pasajerosEnTramo) {
		Integer capacidad = capacidadPorColectivo.get(idColectivo);
		if (capacidad == null || capacidad == 0) return;
		
		double ocupacion = (double) pasajerosEnTramo / capacidad;
		this.ocupacionesPorColectivo.computeIfAbsent(idColectivo, k -> new ArrayList<>()).add(ocupacion);
	}

	// =================================================================================
	// MÉTODOS DE CÁLCULO Y CONSULTA
	// =================================================================================

	/** Obtener el porcentaje de pasajeros satisfechos. */
	public double getPorcentajeSatisfechos() {
		return pasajerosTransportados == 0 ? 0 : 100.0 * pasajerosSatisfechos / pasajerosTransportados;
	}

	/** Obtener el porcentaje de pasajeros insatisfechos. */
	public double getPorcentajeInsatisfechos() {
		return pasajerosTransportados == 0 ? 0 : 100.0 * pasajerosInsatisfechos / pasajerosTransportados;
	}

	/** Devuelve el índice de satisfacción general (normalizado a 1). */
	public double getIndiceSatisfaccion() {
		if (totalPasajerosCalificados == 0) return 0.0;
		// La suma de calificaciones (1-5) dividido por el máximo posible (pasajeros * 5)
		return (double) sumaCalificaciones / (totalPasajerosCalificados * 5.0);
	}

	/** Devuelve el promedio de ocupación por colectivo como un mapa. */
	public Map<String, Double> getOcupacionPromedioPorColectivo() {
		Map<String, Double> promedios = new HashMap<>();
		for (Map.Entry<String, List<Double>> entry : ocupacionesPorColectivo.entrySet()) {
			List<Double> lista = entry.getValue();
			if (lista != null && !lista.isEmpty()) {
				double suma = lista.stream().mapToDouble(Double::doubleValue).sum();
				promedios.put(entry.getKey(), suma / lista.size());
			}
		}
		return promedios;
	}

	/** Devuelve un desglose de los pasajeros por su estado final. */
	public Map<String, Integer> getDesglosePasajeros() {
		int transportados = 0;
		int bajadosForzosamente = 0;
		int nuncaSubieron = 0;
		for (Pasajero p : pasajerosRegistrados) {
			if (p.isBajadaForzosa()) {
				bajadosForzosamente++;
			} else if (!p.isPudoSubir()) {
				nuncaSubieron++;
			} else {
				transportados++;
			}
		}
		Map<String, Integer> desglose = new HashMap<>();
		desglose.put("transportadosConExito", transportados);
		desglose.put("bajadosForzosamente", bajadosForzosamente);
		desglose.put("nuncaSubieron", nuncaSubieron);
		return desglose;
	}

	// =================================================================================
	// GETTERS (Defensivos)
	// =================================================================================

	public int getPasajerosTransportados() { return this.pasajerosTransportados; }
	public int getPasajerosTotales() { return this.pasajerosRegistrados.size(); }
	public Map<Integer, Integer> getDesgloseCalificaciones() { return Collections.unmodifiableMap(this.conteoCalificaciones); }
}