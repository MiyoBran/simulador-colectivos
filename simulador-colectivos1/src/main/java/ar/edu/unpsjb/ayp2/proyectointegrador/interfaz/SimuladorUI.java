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
 * SimuladorController. Es responsable de iniciar la simulación y mostrar los
 * resultados y menús correspondientes.
 *
 * @author Miyo
 * @author Enzo
 * @version 1.3
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
	 * Inicia la interfaz de usuario, muestra el menú principal y gestiona el bucle
	 * de interacción con el usuario.
	 */
	public void start() {
		imprimirBienvenida();
		try {
			controller.inicializar();
		} catch (Exception e) {
			System.err.println("Error crítico durante la inicialización. La aplicación no puede continuar.");
			e.printStackTrace();
			return;
		}


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
					// FUNCIONALIDAD DESHABILITADA TEMPORALMENTE
					// =====================================================================
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
		System.out.println("\nSaliendo del simulador. ¡Hasta pronto!");
	}

	// =================================================================================
	// MÉTODOS PRIVADOS DE AYUDA (HELPERS PARA EL MENÚ)
	// =================================================================================

	private void imprimirBienvenida() {
		System.out.println("===================================");
		System.out.println("  Simulador de Colectivos Urbanos  ");
		System.out.println("===================================\n");
	}

	private void imprimirMenuPrincipal() {
		System.out.println("\n--- MENÚ PRINCIPAL ---");
		System.out.println("1. Ejecutar simulación completa");
		System.out.println("2. Ver estadísticas de la simulación");
		// System.out.println("4. Calcular ruta óptima entre paradas"); // Opción HABILITADA
		System.out.println("0. Salir");
		System.out.print("Seleccione una opción: ");
	}

	private void ejecutarSimulacionCompleta() {
		var simulador = controller.getSimulador();
		if (simulador.isSimulacionTerminada()) {
			System.out.println("La simulación ya ha finalizado. Mostrando estadísticas.");
			mostrarEstadisticas();
			return;
		}

		System.out.println("Ejecutando simulación... por favor espere.");
		Map<String, List<String>> simulacionColectivo = new LinkedHashMap<>();
		while (!simulador.isSimulacionTerminada()) {
			List<String> eventosDelPaso = simulador.ejecutarPasoDeSimulacion();
			Map<String, List<String>> eventosPorColectivo = EventoUtils.mostrarEventosAgrupadosPorColectivo(eventosDelPaso);
			for (Map.Entry<String, List<String>> entry : eventosPorColectivo.entrySet()) {
				simulacionColectivo.computeIfAbsent(entry.getKey(), k -> new ArrayList<>()).addAll(entry.getValue());
			}
		}

		for (Map.Entry<String, List<String>> entry : simulacionColectivo.entrySet()) {
			System.out.println("\nEventos del " + entry.getKey() + ":");
			for (String evento : entry.getValue()) {
				if (evento.startsWith("\n")) {
					System.out.print(evento);
				} else {
					System.out.println("  " + evento);
				}
			}
		}

		System.out.println("\n--- SIMULACIÓN FINALIZADA ---");
		// Se muestran las estadísticas automáticamente al finalizar.
		// mostrarEstadisticas();
	}

	private void mostrarEstadisticas() {
		var simulador = controller.getSimulador();
		if (simulador.getGestorEstadisticas() == null) {
			System.out.println("Funcionalidad de estadísticas no disponible en este simulador.");
		} else {
			System.out.println("\n=====================================================");
			System.out.println("      INFORME COMPLETO DE LA SIMULACIÓN");
			System.out.println("=====================================================");

			ReporteSimulacion.imprimirReportePasajeros(simulador);
			ReporteSimulacion.imprimirEstadisticasDeSatisfaccion(simulador);
			ReporteSimulacion.imprimirOcupacionPromedioColectivos(simulador);

			System.out.println("\n--- Mensajes Finales del Simulador ---");
			List<String> reporteFinal = simulador.getReporteFinal();
			if (reporteFinal.isEmpty() || reporteFinal.size() <= 1) { // Size 1 para el mensaje por defecto
				System.out.println("  No hay mensajes o advertencias finales.");
			} else {
				for (String mensaje : reporteFinal) {
					System.out.println("  " + mensaje);
				}
			}

			ReporteSimulacion.verificarConsistenciaEstadisticas(simulador);
		}
	}
	/*
	// =================================================================================
	// MÉTODO PARA FUNCIONALIDAD FUTURA (DESHABILITADA)
	// =================================================================================
	
	/**
	 * Lógica para la opción 4: Permite al usuario ingresar dos paradas y calcula la
	 * ruta más corta entre ellas usando el PlanificadorRutas.
	 *
	private void calcularRutaOptima() {
		// 1. Solicitar datos al usuario
		System.out.print("Ingrese ID de parada origen: ");
		String idOrigen = scanner.nextLine();
		System.out.print("Ingrese ID de parada destino: ");
		String idDestino = scanner.nextLine();

		// 2. Obtener y validar las paradas
		Parada origen = controller.getParadasCargadas().get(idOrigen);
		Parada destino = controller.getParadasCargadas().get(idDestino);

		if (origen == null || destino == null) {
			System.out.println("\nError: Parada origen o destino no encontrada.");
			return;
		}

		// 3. Calcular y mostrar la ruta
		List<Parada> ruta = controller.getPlanificadorRutas().calcularRutaOptima(origen, destino);

		if (ruta.isEmpty() || ruta.size() < 2) {
			System.out.println("\nNo se encontró una ruta de colectivo entre las paradas seleccionadas.");
		} else {
			System.out.println("\n--- RUTA ÓPTIMA ENCONTRADA ---");
			System.out.printf("  1. Diríjase a la parada de origen: %s (ID: %s)\n", origen.getDireccion(), origen.getId());

			for (int i = 0; i < ruta.size() - 1; i++) {
				Parada paradaActual = ruta.get(i);
				Parada paradaSiguiente = ruta.get(i + 1);
				
				Linea lineaDelTramo = encontrarLineaParaSegmento(paradaActual, paradaSiguiente);
				
				if (lineaDelTramo != null) {
					System.out.printf("  -> Tome el colectivo de la [%s]\n", lineaDelTramo.getNombre());
					System.out.printf("  %d. Baje en la parada: %s (ID: %s)\n", i + 2, paradaSiguiente.getDireccion(), paradaSiguiente.getId());
				} else {
					System.out.printf("  (No se encontró línea directa para el tramo %s -> %s)\n", paradaActual.getId(), paradaSiguiente.getId());
				}
			}
			System.out.println("---------------------------------");
			System.out.println("Ha llegado a su destino.");
		}
	}

	/**
	 * Busca en todas las líneas disponibles para encontrar una que conecte un
	 * segmento de ruta específico (dos paradas consecutivas).
	 *
	 * @param origen  La parada de inicio del segmento.
	 * @param destino La parada de fin del segmento.
	 * @return La primera {@link Linea} encontrada que cubre el segmento, o {@code null} si no se encuentra ninguna.
	 *
	private Linea encontrarLineaParaSegmento(Parada origen, Parada destino) {
		for (Linea linea : controller.getLineasCargadas().values()) {
			List<Parada> recorrido = linea.getRecorrido();
			int indiceOrigen = recorrido.indexOf(origen);
			
			if (indiceOrigen != -1 && indiceOrigen < recorrido.size() - 1) {
				if (recorrido.get(indiceOrigen + 1).equals(destino)) {
					return linea;
				}
			}
		}
		return null;
	}
	*/
}