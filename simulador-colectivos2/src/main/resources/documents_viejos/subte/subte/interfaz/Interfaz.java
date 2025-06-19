package subte.interfaz;

import java.util.List;

import net.datastructures.TreeMap;
import subte.aplicacion.Constante;
import subte.modelo.Estacion;
import subte.modelo.Tramo;

public class Interfaz {

	// Usuario ingresa opción
	public static int opcion() {
		return Constante.MAS_RAPIDO;
	}

	// Usuario ingresa estación origen
	public static Estacion ingresarEstacionOrigen(
			TreeMap<String, Estacion> estaciones) {
		return estaciones.get("a2");
	}

	// Usuario ingresa estación destino
	public static Estacion ingresarEstacionDestino(
			TreeMap<String, Estacion> estaciones) {
		return estaciones.get("c6");
	}

	public static void resultado(List<Tramo> recorrido) {
		int tiempoTotal = 0;
		int tiempoSubte = 0;
		int tiempoCaminando = 0;
		for (Tramo t : recorrido) {
			System.out.println(t.getEstacion1().getLinea().getCodigo() + "-"
					+ t.getEstacion1().getNombre() + " > "
					+ t.getEstacion2().getLinea().getCodigo() + "-"
					+ t.getEstacion2().getNombre() + " :" + t.getTiempo());
			tiempoTotal += t.getTiempo();
			if (t.getEstacion1().getLinea().equals(t.getEstacion2().getLinea()))
				tiempoSubte += t.getTiempo();
			else
				tiempoCaminando += t.getTiempo();
		}
		System.out.println("Tiempo Total: " + tiempoTotal);
		System.out.println("Tiempo Subte: " + tiempoSubte);
		System.out.println("Tiempo Caminando: " + tiempoCaminando);
	}

}
