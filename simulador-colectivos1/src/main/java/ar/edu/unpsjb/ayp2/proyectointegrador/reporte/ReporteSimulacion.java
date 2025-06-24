package ar.edu.unpsjb.ayp2.proyectointegrador.reporte;

import ar.edu.unpsjb.ayp2.proyectointegrador.logica.GestorEstadisticas;
import ar.edu.unpsjb.ayp2.proyectointegrador.logica.Simulador;
import ar.edu.unpsjb.ayp2.proyectointegrador.modelo.Colectivo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;

/**
 * Clase de utilidad para generar y mostrar reportes de la simulación.
 * Proporciona métodos estáticos para imprimir diversas estadísticas en la consola.
 *
 * @author Miyo
 * @author Enzo
 * @version 1.1
 */
public final class ReporteSimulacion {

	/**
	 * Constructor privado para prevenir la instanciación de esta clase de utilidad.
	 */
	private ReporteSimulacion() {
	}

	// =================================================================================
	// MÉTODOS PÚBLICOS DE REPORTE
	// =================================================================================

	/**
	 * Imprime las estadísticas de satisfacción de los pasajeros (Anexo I).
	 * @param simulador El simulador del cual se obtienen los datos.
	 */
	public static void imprimirEstadisticasDeSatisfaccion(Simulador simulador) {
		GestorEstadisticas gestor = simulador.getGestorEstadisticas();
		
		imprimirTitulo("Estadísticas de Satisfacción (Anexo I)");
		imprimirLineaDato("Índice de satisfacción general", String.format("%.2f%%", gestor.getIndiceSatisfaccion() * 100), "");
		
		System.out.println("  - Desglose de calificaciones:");
		gestor.getDesgloseCalificaciones().entrySet().stream()
			.sorted(Map.Entry.<Integer, Integer>comparingByKey().reversed()) // Ordenar de 5 a 1
			.forEach(entry -> 
				imprimirLineaDato(String.format("    Calificación %d estrellas", entry.getKey()), entry.getValue(), "pasajeros")
			);
	}
	
	/**
	 * Imprime un reporte detallado sobre el estado final de todos los pasajeros.
	 * @param simulador El simulador del cual se obtienen los datos.
	 */
	public static void imprimirReportePasajeros(Simulador simulador) {
		GestorEstadisticas gestor = simulador.getGestorEstadisticas();
		Map<String, Integer> desglose = gestor.getDesglosePasajeros();
		
		imprimirTitulo("Reporte de Pasajeros");
		imprimirLineaDato("Total generados", gestor.getPasajerosTotales(), "pasajeros");
		imprimirLineaDato("Transportados con éxito", desglose.getOrDefault("transportadosConExito", 0), "pasajeros");
		imprimirLineaDato("Bajados forzosamente", desglose.getOrDefault("bajadosForzosamente", 0), "pasajeros");
		imprimirLineaDato("Nunca subieron", desglose.getOrDefault("nuncaSubieron", 0), "pasajeros");
		
		verificarConsistenciaPasajeros(gestor, desglose);
	}

	/**
	 * Imprime la ocupación promedio de cada colectivo y el promedio general (Anexo II).
	 * @param simulador El simulador del cual se obtienen los datos.
	 */
	public static void imprimirOcupacionPromedioColectivos(Simulador simulador) {
		Map<String, Double> ocupaciones = simulador.getGestorEstadisticas().getOcupacionPromedioPorColectivo();
		
		imprimirTitulo("Ocupación Promedio de Colectivos (Anexo II)");
		if (ocupaciones.isEmpty()) {
			System.out.println("  No hay datos de ocupación registrados.");
			return;
		}

		// Ordena los colectivos por ID para una presentación consistente
		ArrayList<Colectivo> colectivosOrdenados = new ArrayList<>(simulador.getColectivosEnSimulacion());
		colectivosOrdenados.sort(COMPARATOR_POR_NUMERO_ID_COLECTIVO);

		for (Colectivo colectivo : colectivosOrdenados) {
			Double ocupacion = ocupaciones.get(colectivo.getIdColectivo());
			if (ocupacion != null) {
				imprimirLineaDato(colectivo.getEtiqueta(), String.format("%.2f%%", ocupacion * 100), "");
			}
		}
		
		double promedioGeneral = calcularOcupacionPromedioGeneral(ocupaciones);
		System.out.println(); // Salto de línea
		imprimirLineaDato("Ocupación promedio general", String.format("%.2f%%", promedioGeneral * 100), "");
	}
	
	// =================================================================================
	// MÉTODOS PÚBLICOS DE VERIFICACIÓN
	// =================================================================================

	/**
	 * Realiza una serie de verificaciones para asegurar la consistencia de las estadísticas.
	 * Imprime advertencias en la consola de error si encuentra inconsistencias.
	 * @param simulador El simulador cuyos datos se verificarán.
	 */
	public static void verificarConsistenciaEstadisticas(Simulador simulador) {
		GestorEstadisticas gestor = simulador.getGestorEstadisticas();
		
		imprimirTitulo("Verificación de Consistencia de Estadísticas");
		
		// Verificación 1: Que el desglose de pasajeros coincida con el total generado.
		verificarConsistenciaPasajeros(gestor, gestor.getDesglosePasajeros());
		
		// Verificación 2: Que el total de pasajeros calificados coincida con los transportados (o generados).
		int totalCalificaciones = gestor.getDesgloseCalificaciones().values().stream().mapToInt(Integer::intValue).sum();
		if (totalCalificaciones != gestor.getPasajerosTotales()) {
			System.err.printf("[ADVERTENCIA] El total de pasajeros calificados (%d) no coincide con el total de pasajeros generados (%d).\n",
				totalCalificaciones, gestor.getPasajerosTotales());
		} else {
			System.out.println("  Consistencia de conteo de pasajeros: OK");
		}
	}
	
	// =================================================================================
	// MÉTODOS PRIVADOS DE AYUDA (HELPERS)
	// =================================================================================

	/** Imprime un título de sección formateado. */
	private static void imprimirTitulo(String titulo) {
		System.out.printf("\n--- %S ---\n", titulo); // %S para mayúsculas
	}

	/** Imprime una línea de dato con etiqueta, valor y sufijo, con formato e indentación. */
	private static void imprimirLineaDato(String etiqueta, Object valor, String sufijo) {
		System.out.printf("  - %-40s %s %s\n", etiqueta + ":", valor, sufijo).flush();
	}

	/** Calcula el promedio general de ocupación a partir de un mapa de promedios individuales. */
	private static double calcularOcupacionPromedioGeneral(Map<String, Double> ocupaciones) {
		if (ocupaciones == null || ocupaciones.isEmpty()) {
			return 0.0;
		}
		double sumaPromedios = ocupaciones.values().stream().mapToDouble(Double::doubleValue).sum();
		return sumaPromedios / ocupaciones.size();
	}

	/** Verifica si el desglose de pasajeros suma el total de generados. */
	private static void verificarConsistenciaPasajeros(GestorEstadisticas gestor, Map<String, Integer> desglose) {
		int totalGenerados = gestor.getPasajerosTotales();
		int sumaDesglose = desglose.values().stream().mapToInt(Integer::intValue).sum();

		if (sumaDesglose != totalGenerados) {
			System.err.printf("[ADVERTENCIA] La suma del desglose de pasajeros (%d) no coincide con el total generado (%d).\n",
				sumaDesglose, totalGenerados);
		}
	}

	// =================================================================================
	// COMPARATOR
	// =================================================================================

	/**
	 * Compara dos colectivos basándose en el número numérico de su ID (ej: C<num>-linea).
	 */
	private static final Comparator<Colectivo> COMPARATOR_POR_NUMERO_ID_COLECTIVO = (a, b) -> {
		try {
			int numA = extraerNumeroIdColectivo(a.getIdColectivo());
			int numB = extraerNumeroIdColectivo(b.getIdColectivo());
			return Integer.compare(numA, numB);
		} catch (Exception e) {
			// Fallback a comparación de String si el formato falla
			return a.getIdColectivo().compareTo(b.getIdColectivo());
		}
	};

	/** Extrae la parte numérica del ID de un colectivo. */
	private static int extraerNumeroIdColectivo(String id) {
		int start = id.indexOf('C') + 1;
		int end = id.indexOf('-');
		if (start > 0 && end > start) {
			return Integer.parseInt(id.substring(start, end));
		}
		throw new NumberFormatException("Formato de id de colectivo inválido: " + id);
	}
}