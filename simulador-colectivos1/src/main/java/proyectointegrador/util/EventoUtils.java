package proyectointegrador.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern; // Se importa Pattern

/**
 * Clase de utilidades para manejar eventos del simulador. Proporciona métodos
 * para agrupar eventos por colectivo y detectar colectivos en líneas de
 * eventos.
 *
 * @author Miyo
 * @author Enzo
 * @version 1.1 // Versión actualizada para reflejar las mejoras
 */
public final class EventoUtils { // La clase ahora es FINAL

	// MEJORA: El patrón de Regex se compila una sola vez como una constante estática.
	private static final Pattern COLECTIVO_PATTERN = Pattern.compile("Colectivo (C\\d+-\\d+)");

	/**
	 * MEJORA: Constructor privado para evitar que la clase sea instanciada.
	 */
	private EventoUtils() {
	}

	// Método para mostrar eventos agrupados por colectivo
	public static Map<String, List<String>> mostrarEventosAgrupadosPorColectivo(List<String> eventos) {
		// DECLARACIÓN DE VARIABLES
		LinkedHashMap<String, List<String>> eventosPorColectivo = new LinkedHashMap<>(); // Mapa para almacenar eventos agrupados por colectivo
		String colectivoActual = null; // ID del colectivo procesado actualmente
		String idDetectado = null; // ID detectado en la línea de evento

		// LÓGICA DEL MÉTODO
		for (String evento : eventos) {
			idDetectado = detectarColectivoEnLinea(evento);
			if (idDetectado != null) {
				colectivoActual = idDetectado;
				eventosPorColectivo.putIfAbsent(colectivoActual, new ArrayList<>());
				eventosPorColectivo.get(colectivoActual).add(evento.trim());
			} else if (colectivoActual != null) {
				eventosPorColectivo.get(colectivoActual).add("  " + evento.trim());
			} else {
				// Eventos generales o de sistema
				eventosPorColectivo.putIfAbsent("GENERAL", new ArrayList<>());
				eventosPorColectivo.get("GENERAL").add(evento.trim());
			}
		}

		return eventosPorColectivo;
	}

	// Método para detectar el ID del colectivo en una línea de evento
	public static String detectarColectivoEnLinea(String linea) {
		// Se utiliza la constante pre-compilada para mayor eficiencia.
		java.util.regex.Matcher matcher = COLECTIVO_PATTERN.matcher(linea);

		if (matcher.find()) {
			return matcher.group(1);
		}
		return null;
	}

}