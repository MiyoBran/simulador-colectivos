package ar.edu.unpsjb.ayp2.proyectointegrador.interfaz;

import ar.edu.unpsjb.ayp2.proyectointegrador.logica.Simulador;
import ar.edu.unpsjb.ayp2.proyectointegrador.modelo.Linea;
import ar.edu.unpsjb.ayp2.proyectointegrador.modelo.Parada;
import ar.edu.unpsjb.ayp2.proyectointegrador.modelo.Pasajero;
import ar.edu.unpsjb.ayp2.proyectointegrador.datos.LectorArchivos;
import ar.edu.unpsjb.ayp2.proyectointegrador.logica.GeneradorPasajeros;
import ar.edu.unpsjb.ayp2.proyectointegrador.reporte.ReporteSimulacion;

import java.util.*;

/**
 * Clase principal de la aplicación del Simulador de Colectivos Urbanos. Se
 * encarga de la carga de datos, configuración de la simulación y la interacción
 * con el usuario a través de un menú por consola.
 * 
 * @author Miyo
 * @author Enzo
 * @version 1.0
 */
public class SimuladorColectivosApp {
	public static void main(String[] args) {
		SimuladorController controller = new SimuladorController();
		SimuladorUI ui = new SimuladorUI(controller);
		ui.start();
	}

	/**
	 * Imprime la bienvenida al usuario.
	 */
	private static void imprimirBienvenida() {
		System.out.println("===================================");
		System.out.println("  Simulador de Colectivos Urbanos  ");
		System.out.println("===================================\n");
	}
}