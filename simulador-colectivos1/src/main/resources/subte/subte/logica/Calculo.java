package subte.logica;

import java.util.ArrayList;
import java.util.List;

import net.datastructures.AdjacencyMapGraph;
import net.datastructures.Edge;
import net.datastructures.Entry;
import net.datastructures.Graph;
import net.datastructures.GraphAlgorithms;
import net.datastructures.Map;
import net.datastructures.Position;
import net.datastructures.PositionalList;
import net.datastructures.ProbeHashMap;
import net.datastructures.TreeMap;
import net.datastructures.Vertex;
import subte.modelo.Estacion;
import subte.modelo.Tramo;

public class Calculo {

	private Graph<Estacion, Tramo> subte;
	private TreeMap<String, Vertex<Estacion>> vertices;

	public Calculo(TreeMap<String, Estacion> estaciones, List<Tramo> tramos) {

		subte = new AdjacencyMapGraph<>(false);

		// Cargar estaciones
		vertices = new TreeMap<String, Vertex<Estacion>>();
		for (Entry<String, Estacion> estacion : estaciones.entrySet())
			vertices.put(estacion.getKey(),
					subte.insertVertex(estacion.getValue()));

		// Cargar tramos
		for (Tramo tramo : tramos)
			subte.insertEdge(vertices.get(tramo.getEstacion1().getCodigo()),
					vertices.get(tramo.getEstacion2().getCodigo()), tramo);
	}

	public List<Tramo> rapido(Estacion estacion1, Estacion estacion2) {
		// copia grafo
		Graph<Estacion, Integer> rapido = new AdjacencyMapGraph<>(false);
		Map<Estacion, Vertex<Estacion>> res = new ProbeHashMap<>();

		for (Vertex<Estacion> result : subte.vertices())
			res.put(result.getElement(),
					rapido.insertVertex(result.getElement()));

		Vertex<Estacion>[] vert;

		for (Edge<Tramo> result : subte.edges()) {
			vert = subte.endVertices(result);
			rapido.insertEdge(res.get(vert[0].getElement()), res.get(vert[1]
					.getElement()), result.getElement().getTiempo());
		}
		PositionalList<Vertex<Estacion>> lista = GraphAlgorithms
				.shortestPathList(rapido, res.get(estacion1),
						res.get(estacion2));

		List<Tramo> tramos = new ArrayList<Tramo>();

		Vertex<Estacion> v1, v2;
		Position<Vertex<Estacion>> aux = lista.first();
		while (lista.after(aux) != null) {
			v1 = aux.getElement();			
			aux = lista.after(aux);
			v2 = aux.getElement();
			tramos.add(subte.getEdge(vertices.get(v1.getElement().getCodigo()),
					vertices.get(v2.getElement().getCodigo())).getElement());
		}
		return tramos;
	}

}
