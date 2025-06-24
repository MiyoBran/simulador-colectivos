package ar.edu.unpsjb.ayp2.proyectointegrador.interfaz;

import ar.edu.unpsjb.ayp2.proyectointegrador.modelo.Parada;
import ar.edu.unpsjb.ayp2.proyectointegrador.reporte.ReporteSimulacion;
import ar.edu.unpsjb.ayp2.proyectointegrador.util.EventoUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Gestiona la Interfaz de Usuario (UI) por consola para el simulador.
 * <p>
 * Presenta un menú de opciones al usuario y delega las acciones al
 * SimuladorController. Es responsable de iniciar la simulación y mostrar
 * los resultados y menús correspondientes.
 *
 * @author Miyo
 * @author Enzo
 * @version 1.2
 */
public class SimuladorUI {

	// =================================================================================
	// ATRIBUTOS
	// =================================================================================

	private final SimuladorController controller;
	private final Scanner scanner;

	// =================================================================================
	// CONSTRUCTOR
	// =================================================================================

	public SimuladorUI(SimuladorController controller) {
		this.controller = controller;
		this.scanner = new Scanner(System.in);
	}

	// =================================================================================
	// LÓGICA PRINCIPAL DE LA UI
	// =================================================================================

	/**
	 * Inicia la interfaz de usuario, muestra el menú principal y gestiona el
	 * bucle de interacción con el usuario.
	 */
	public void start() {
		imprimirBienvenida();
		controller.inicializar();

		System.out.println("Simulación lista para comenzar con " + controller.getPasajerosGenerados().size() + " pasajeros.");
		System.out.println("Se han inicializado " + controller.getSimulador().getColectivosEnSimulacion().size() + " colectivos.");

		boolean salir = false;
		while (!salir) {
			imprimirMenuPrincipal();
			String opcion = scanner.nextLine();

			switch (opcion) {
				case "1":
					ejecutarSimulacionCompleta();
					break;
				case "2":
					mostrarEstadisticas();
					break;

				/*
				// =====================================================================
				// FUNCIONALIDAD DESHABILITADA PARA ESTE INCREMENTO
				// =====================================================================
				case "3":
					ejecutarPasoDeSimulacion();
					break;
				case "4":
					calcularRutaOptima();
					break;
				*/

				case "0":
					salir = true;
					break;
				default:
					System.out.println("Opción no válida. Intente nuevamente.");
			}
		}
		System.out.println("Saliendo del simulador. ¡Hasta pronto!");
	}
	
	// =================================================================================
	// MÉTODOS PRIVADOS DE AYUDA (HELPERS PARA EL MENÚ)
	// =================================================================================

	/**
	 * Imprime el banner de bienvenida de la aplicación.
	 */
	private void imprimirBienvenida() {
		System.out.println("===================================");
		System.out.println("  Simulador de Colectivos Urbanos  ");
		System.out.println("===================================\n");
	}

	/**
	 * Imprime las opciones del menú principal en la consola.
	 */
	private void imprimirMenuPrincipal() {
		System.out.println("\n--- MENÚ PRINCIPAL ---");
		System.out.println("1. Ejecutar simulación completa");
		System.out.println("2. Ver estadísticas de la simulación");
		// System.out.println("3. Ejecutar paso de simulación"); // Opción deshabilitada
		// System.out.println("4. Calcular ruta óptima entre paradas"); // Opción deshabilitada
		System.out.println("0. Salir");
		System.out.print("Seleccione una opción: ");
	}

	/**
	 * Lógica para la opción 1: Ejecuta la simulación de principio a fin y muestra resultados.
	 */
	private void ejecutarSimulacionCompleta() {
		var simulador = controller.getSimulador();
		if (simulador.isSimulacionTerminada()) {
			System.out.println("La simulación ya ha finalizado. Mostrando estadísticas.");
			mostrarEstadisticas();
			return;
		}

		Map<String, List<String>> simulacionColectivo = new LinkedHashMap<>();
		while (!simulador.isSimulacionTerminada()) {
			List<String> eventosDelPaso = simulador.ejecutarPasoDeSimulacion();
			Map<String, List<String>> eventosPorColectivo = EventoUtils.mostrarEventosAgrupadosPorColectivo(eventosDelPaso);
			// Acumula los eventos para mostrarlos al final
			for (Map.Entry<String, List<String>> entry : eventosPorColectivo.entrySet()) {
				simulacionColectivo.putIfAbsent(entry.getKey(), new ArrayList<>());
				simulacionColectivo.get(entry.getKey()).addAll(entry.getValue());
			}
		}
		
		// Imprime todos los eventos acumulados
		for (Map.Entry<String, List<String>> entry : simulacionColectivo.entrySet()) {
			System.out.println("\nEventos del " + entry.getKey() + ":");
			for (String evento : entry.getValue()) {
				System.out.println("  " + evento);
			}
		}
		
		System.out.println("\n--- SIMULACIÓN FINALIZADA ---");
		mostrarEstadisticas();
	}

	/**
	 * Lógica para la opción 2: Muestra los reportes y estadísticas de la simulación.
	 */
	private void mostrarEstadisticas() {
		var simulador = controller.getSimulador();
		if (simulador.getGestorEstadisticas() == null) {
			System.out.println("Funcionalidad de estadísticas no disponible en este simulador.");
		} else {
			System.out.println("\n" + String.join("\n", simulador.getReporteFinal()));
			ReporteSimulacion.imprimirEstadisticasCompletas(simulador);
			ReporteSimulacion.verificarConsistenciaEstadisticas(simulador);
			ReporteSimulacion.imprimirReportePasajeros(simulador);
			ReporteSimulacion.imprimirOcupacionPromedioColectivos(simulador);
		}
	}
	
	/*
	// =====================================================================
	// MÉTODOS PARA FUNCIONALIDADES DESHABILITADAS
	// =====================================================================

	private void ejecutarPasoDeSimulacion() {
		var simulador = controller.getSimulador();
		if (!simulador.isSimulacionTerminada()) {
			List<String> eventosDelPaso = simulador.ejecutarPasoDeSimulacion();
			Map<String, List<String>> eventosPorColectivo = EventoUtils.mostrarEventosAgrupadosPorColectivo(eventosDelPaso);
			for (Map.Entry<String, List<String>> entry : eventosPorColectivo.entrySet()) {
				System.out.println("Colectivo " + entry.getKey() + ":");
				for (String evento : entry.getValue()) {
					System.out.println("  " + evento);
				}
			}
		} else {
			System.out.println("La simulación ya ha finalizado.");
		}
	}

	private void calcularRutaOptima() {
		System.out.print("Ingrese ID de parada origen: ");
		String idOrigen = scanner.nextLine();
		System.out.print("Ingrese ID de parada destino: ");
		String idDestino = scanner.nextLine();
		
		Parada origen = controller.getParadasCargadas().get(idOrigen);
		Parada destino = controller.getParadasCargadas().get(idDestino);
		var simulador = controller.getSimulador();

		if (origen == null || destino == null) {
			System.out.println("Parada origen o destino no encontrada.");
		} else if (simulador.getPlanificadorRutas() == null) {
			System.out.println("Funcionalidad de rutas no disponible en este simulador.");
		} else {
			List<Parada> ruta = simulador.getPlanificadorRutas().calcularRutaOptima(origen, destino);
			if (ruta.isEmpty()) {
				System.out.println("No existe ruta entre las paradas seleccionadas.");
			} else {
				System.out.println("Ruta óptima: ");
				for (Parada p : ruta) {
					System.out.println("- " + p.getId() + " | " + p.getDireccion());
				}
			}
		}
	}
	*/
}