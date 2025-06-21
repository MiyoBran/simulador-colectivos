package ar.edu.unpsjb.ayp2.proyectointegrador.logica;

import ar.edu.unpsjb.ayp2.proyectointegrador.modelo.Parada;
import net.datastructures.AdjacencyMapGraph;
import net.datastructures.Vertex;
import net.datastructures.Edge;
import java.util.*;

/**
 * PlanificadorRutas modela la red de transporte como un grafo y permite calcular rutas óptimas entre paradas.
 * Utiliza AdjacencyMapGraph para representar la red y Dijkstra para rutas más cortas.
 *
 * @author Miyen
 * @version 2.0
 */
public class PlanificadorRutas {
    private AdjacencyMapGraph<Parada, Integer> grafo;
    private Map<String, Vertex<Parada>> verticesPorId;

    public PlanificadorRutas() {
        this.grafo = new AdjacencyMapGraph<>(true); // Cambiado a dirigido
        this.verticesPorId = new HashMap<>();
    }

    /**
     * Agrega una parada al grafo si no existe.
     */
    public void agregarParada(Parada parada) {
        if (!verticesPorId.containsKey(parada.getId())) {
            Vertex<Parada> v = grafo.insertVertex(parada);
            verticesPorId.put(parada.getId(), v);
        }
    }

    /**
     * Conecta dos paradas con un peso (por ejemplo, tiempo o distancia).
     */
    public void conectarParadas(Parada origen, Parada destino, int peso) {
        agregarParada(origen);
        agregarParada(destino);
        grafo.insertEdge(verticesPorId.get(origen.getId()), verticesPorId.get(destino.getId()), peso);
    }

    /**
     * Calcula la ruta óptima (menor peso) entre dos paradas usando Dijkstra.
     * Devuelve la lista de paradas en el orden del recorrido óptimo.
     */
    public List<Parada> calcularRutaOptima(Parada origen, Parada destino) {
        if (!verticesPorId.containsKey(origen.getId()) || !verticesPorId.containsKey(destino.getId())) {
            return Collections.emptyList();
        }
        Map<Vertex<Parada>, Integer> dist = new HashMap<>();
        Map<Vertex<Parada>, Vertex<Parada>> prev = new HashMap<>();
        Set<Vertex<Parada>> visitados = new HashSet<>();
        PriorityQueue<Vertex<Parada>> pq = new PriorityQueue<>(Comparator.comparingInt(dist::get));
        for (Vertex<Parada> v : grafo.vertices()) {
            dist.put(v, v.equals(verticesPorId.get(origen.getId())) ? 0 : Integer.MAX_VALUE);
            prev.put(v, null);
        }
        pq.add(verticesPorId.get(origen.getId()));
        while (!pq.isEmpty()) {
            Vertex<Parada> u = pq.poll();
            if (!visitados.add(u)) continue;
            for (Edge<Integer> e : grafo.outgoingEdges(u)) {
                Vertex<Parada> v = grafo.opposite(u, e);
                int peso = e.getElement();
                int alt = dist.get(u) + peso;
                if (alt < dist.get(v)) {
                    dist.put(v, alt);
                    prev.put(v, u);
                    pq.add(v);
                }
            }
        }
        // Reconstruir el camino
        List<Parada> ruta = new LinkedList<>();
        Vertex<Parada> actual = verticesPorId.get(destino.getId());
        while (actual != null) {
            ruta.add(0, actual.getElement());
            actual = prev.get(actual);
        }
        if (!ruta.isEmpty() && ruta.get(0).equals(origen)) {
            return ruta;
        } else {
            return Collections.emptyList();
        }
    }

    /**
     * Devuelve el grafo interno (solo para fines de test o visualización).
     */
    public AdjacencyMapGraph<Parada, Integer> getGrafo() {
        return grafo;
    }
}