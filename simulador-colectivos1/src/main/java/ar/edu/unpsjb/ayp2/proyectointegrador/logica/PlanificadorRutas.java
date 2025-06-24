package ar.edu.unpsjb.ayp2.proyectointegrador.logica;

import ar.edu.unpsjb.ayp2.proyectointegrador.modelo.Linea;
import ar.edu.unpsjb.ayp2.proyectointegrador.modelo.Parada;

import net.datastructures.AdjacencyMapGraph;
import net.datastructures.Edge;
import net.datastructures.Vertex;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * Modela la red de transporte como un grafo y calcula rutas óptimas entre paradas.
 * Utiliza un grafo dirigido y el algoritmo de Dijkstra para encontrar el camino más corto.
 *
 * @author Miyen
 * @version 2.2
 */
public class PlanificadorRutas {

	// =================================================================================
	// ATRIBUTOS
	// =================================================================================

	private final AdjacencyMapGraph<Parada, Integer> grafo;
	private final Map<String, Vertex<Parada>> verticesPorId;

	// =================================================================================
	// CONSTRUCTOR
	// =================================================================================

	public PlanificadorRutas() {
		this.grafo = new AdjacencyMapGraph<>(true);
		this.verticesPorId = new HashMap<>();
	}

	// =================================================================================
	// MÉTODOS PÚBLICOS
	// =================================================================================

	/**
	 * Construye la topología completa del grafo a partir de todas las líneas de colectivo.
	 * @param lineasDisponibles Un mapa con todas las líneas cargadas en la simulación.
	 */
	public void construirGrafoDesdeLineas(final Map<String, Linea> lineasDisponibles) {
		if (lineasDisponibles == null) return;
		
		for (final Linea linea : lineasDisponibles.values()) {
			final List<Parada> recorrido = linea.getRecorrido();
			for (int i = 0; i < recorrido.size() - 1; i++) {
				final Parada origen = recorrido.get(i);
				final Parada destino = recorrido.get(i + 1);
				conectarParadas(origen, destino, 1);
			}
		}
	}
	
	/**
	 * Calcula la ruta óptima (menor peso) entre dos paradas usando el algoritmo de Dijkstra.
	 * @param origen Parada de inicio del viaje.
	 * @param destino Parada final del viaje.
	 * @return Una lista ordenada de Paradas que representa la ruta óptima, o una lista vacía si no se encuentra ruta.
	 */
	public List<Parada> calcularRutaOptima(final Parada origen, final Parada destino) {
		final Vertex<Parada> vOrigen = verticesPorId.get(origen.getId());
		final Vertex<Parada> vDestino = verticesPorId.get(destino.getId());

		if (vOrigen == null || vDestino == null) {
			return Collections.emptyList();
		}

		final Map<Vertex<Parada>, Integer> dist = new HashMap<>();
		final Map<Vertex<Parada>, Vertex<Parada>> prev = new HashMap<>();
		final PriorityQueue<Vertex<Parada>> pq = new PriorityQueue<>(Comparator.comparingInt(dist::get));

		for (Vertex<Parada> v : grafo.vertices()) {
			dist.put(v, Integer.MAX_VALUE);
			prev.put(v, null);
		}
		dist.put(vOrigen, 0);
		pq.add(vOrigen);

		while (!pq.isEmpty()) {
			Vertex<Parada> u = pq.poll();
			if (u.equals(vDestino)) break;

			for (Edge<Integer> e : grafo.outgoingEdges(u)) {
				Vertex<Parada> v = grafo.opposite(u, e);
				int nuevoPeso = dist.get(u) + e.getElement();
				
				if (nuevoPeso < dist.get(v)) {
					dist.put(v, nuevoPeso);
					prev.put(v, u);
					pq.add(v);
				}
			}
		}

		return reconstruirRuta(vOrigen, vDestino, prev);
	}
	
	// =================================================================================
	// MÉTODOS PRIVADOS DE AYUDA (HELPERS)
	// =================================================================================

	/** Agrega una parada como vértice al grafo si no existe previamente. */
	private void agregarParada(final Parada parada) {
		if (parada != null && !verticesPorId.containsKey(parada.getId())) {
			Vertex<Parada> v = grafo.insertVertex(parada);
			verticesPorId.put(parada.getId(), v);
		}
	}

	/** Conecta dos paradas con una arista dirigida y un peso. */
	private void conectarParadas(final Parada origen, final Parada destino, final int peso) {
		agregarParada(origen);
		agregarParada(destino);
		
		Vertex<Parada> vOrigen = verticesPorId.get(origen.getId());
		Vertex<Parada> vDestino = verticesPorId.get(destino.getId());
		
		// =====================================================================
		// INICIO DE LA CORRECCIÓN
		// =====================================================================
		// Se verifica si la arista ya existe antes de insertarla para evitar el error.
		if (grafo.getEdge(vOrigen, vDestino) == null) {
			grafo.insertEdge(vOrigen, vDestino, peso);
		}
		// =====================================================================
		// FIN DE LA CORRECCIÓN
		// =====================================================================
	}
	
	/** Reconstruye la lista de paradas de la ruta óptima a partir del mapa de predecesores. */
	private List<Parada> reconstruirRuta(final Vertex<Parada> origen, final Vertex<Parada> destino, final Map<Vertex<Parada>, Vertex<Parada>> prev) {
		final List<Parada> ruta = new LinkedList<>();
		Vertex<Parada> actual = destino;

		if (prev.get(actual) == null && !actual.equals(origen)) {
			return Collections.emptyList();
		}

		while (actual != null) {
			ruta.add(0, actual.getElement());
			actual = prev.get(actual);
		}
		
		return (ruta.get(0).equals(origen.getElement())) ? ruta : Collections.emptyList();
	}
}