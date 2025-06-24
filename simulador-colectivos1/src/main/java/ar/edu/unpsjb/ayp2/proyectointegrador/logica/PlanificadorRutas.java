package ar.edu.unpsjb.ayp2.proyectointegrador.logica;

import ar.edu.unpsjb.ayp2.proyectointegrador.modelo.Linea;
import ar.edu.unpsjb.ayp2.proyectointegrador.modelo.Parada;

import net.datastructures.AdjacencyMapGraph;
import net.datastructures.Edge;
import net.datastructures.Vertex;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

/**
 * Modela la red de transporte como un grafo y calcula rutas óptimas entre paradas.
 * Utiliza un grafo dirigido y el algoritmo de Dijkstra para encontrar el camino más corto.
 *
 * @author Miyen
 * @version 2.1
 */
public class PlanificadorRutas {

	// =================================================================================
	// ATRIBUTOS
	// =================================================================================

	/** Grafo que representa la red de transporte. Las Paradas son vértices y los tramos, aristas. */
	private final AdjacencyMapGraph<Parada, Integer> grafo;
	/** Mapa para acceder rápidamente a un vértice del grafo a través del ID de la Parada. */
	private final Map<String, Vertex<Parada>> verticesPorId;

	// =================================================================================
	// CONSTRUCTOR
	// =================================================================================

	public PlanificadorRutas() {
		// Se usa un grafo dirigido (true) porque los recorridos de colectivo tienen un sentido.
		this.grafo = new AdjacencyMapGraph<>(true);
		this.verticesPorId = new HashMap<>();
	}

	// =================================================================================
	// MÉTODOS PÚBLICOS DE CONSTRUCCIÓN DEL GRAFO
	// =================================================================================

	/**
	 * [MÉTODO CLAVE AÑADIDO]
	 * Construye la topología completa del grafo a partir de todas las líneas de colectivo.
	 * Este método debe ser llamado una vez después de cargar los datos de las líneas.
	 *
	 * @param lineasDisponibles Un mapa con todas las líneas cargadas en la simulación.
	 */
	public void construirGrafoDesdeLineas(final Map<String, Linea> lineasDisponibles) {
		if (lineasDisponibles == null) return;
		
		for (final Linea linea : lineasDisponibles.values()) {
			final List<Parada> recorrido = linea.getRecorrido();
			// Itera sobre el recorrido para conectar paradas consecutivas.
			for (int i = 0; i < recorrido.size() - 1; i++) {
				final Parada origen = recorrido.get(i);
				final Parada destino = recorrido.get(i + 1);
				// El peso "1" significa que cuesta "1 paso" ir de una parada a la siguiente.
				conectarParadas(origen, destino, 1);
			}
		}
	}

	// =================================================================================
	// MÉTODOS PÚBLICOS DE CÁLCULO
	// =================================================================================

	/**
	 * Calcula la ruta óptima (menor peso) entre dos paradas usando el algoritmo de Dijkstra.
	 *
	 * @param origen Parada de inicio del viaje.
	 * @param destino Parada final del viaje.
	 * @return Una lista ordenada de Paradas que representa la ruta óptima, o una lista vacía si no se encuentra ruta.
	 */
	public List<Parada> calcularRutaOptima(final Parada origen, final Parada destino) {
		final Vertex<Parada> vOrigen = verticesPorId.get(origen.getId());
		final Vertex<Parada> vDestino = verticesPorId.get(destino.getId());

		if (vOrigen == null || vDestino == null) {
			return Collections.emptyList(); // Origen o destino no están en el grafo.
		}

		// Implementación del algoritmo de Dijkstra
		final Map<Vertex<Parada>, Integer> dist = new HashMap<>();
		final Map<Vertex<Parada>, Vertex<Parada>> prev = new HashMap<>();
		final PriorityQueue<Vertex<Parada>> pq = new PriorityQueue<>(Comparator.comparingInt(dist::get));

		// Inicialización
		for (Vertex<Parada> v : grafo.vertices()) {
			dist.put(v, Integer.MAX_VALUE);
			prev.put(v, null);
		}
		dist.put(vOrigen, 0);
		pq.add(vOrigen);

		// Bucle principal
		while (!pq.isEmpty()) {
			Vertex<Parada> u = pq.poll();

			if (u.equals(vDestino)) break; // Optimización: parar si ya llegamos al destino.

			for (Edge<Integer> e : grafo.outgoingEdges(u)) {
				Vertex<Parada> v = grafo.opposite(u, e);
				int nuevoPeso = dist.get(u) + e.getElement();
				
				if (nuevoPeso < dist.get(v)) {
					dist.put(v, nuevoPeso);
					prev.put(v, u);
					// Se vuelve a añadir a la cola con la nueva prioridad (menor distancia)
					pq.add(v);
				}
			}
		}

		// Reconstrucción del camino
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
		grafo.insertEdge(verticesPorId.get(origen.getId()), verticesPorId.get(destino.getId()), peso);
	}
	
	/** Reconstruye la lista de paradas de la ruta óptima a partir del mapa de predecesores. */
	private List<Parada> reconstruirRuta(final Vertex<Parada> origen, final Vertex<Parada> destino, final Map<Vertex<Parada>, Vertex<Parada>> prev) {
		final List<Parada> ruta = new LinkedList<>();
		Vertex<Parada> actual = destino;

		// Si el destino no es alcanzable desde el origen, su predecesor será null (excepto si es el origen mismo)
		if (prev.get(actual) == null && !actual.equals(origen)) {
			return Collections.emptyList();
		}

		while (actual != null) {
			ruta.add(0, actual.getElement());
			actual = prev.get(actual);
		}
		
		// Verificación final: la ruta debe empezar en el origen.
		return (ruta.get(0).equals(origen.getElement())) ? ruta : Collections.emptyList();
	}

}