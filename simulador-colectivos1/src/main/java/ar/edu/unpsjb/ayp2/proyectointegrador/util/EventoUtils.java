package ar.edu.unpsjb.ayp2.proyectointegrador.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

public class EventoUtils {
	/**
	 * Agrupa una lista de eventos por colectivo. Cada evento debe tener el formato
	 * "[colectivo] - [evento]".
	 * 
	 * @param eventos Lista de eventos generados durante la simulación.
	 * @return Un mapa donde la clave es el ID del colectivo (String) y el valor es
	 *         una lista de eventos (List<String>) asociados a ese colectivo. Los
	 *         eventos generales o de sistema se agrupan bajo la clave "GENERAL".
	 *         Este mapa permite acceder fácilmente a todos los eventos relacionados
	 *         con cada colectivo para su presentación o análisis.
	 */
	public static Map<String, List<String>> mostrarEventosAgrupadosPorColectivo(List<String> eventos) {
		// Mapa: Colectivo ID -> Lista de eventos
		LinkedHashMap<String, List<String>> eventosPorColectivo = new LinkedHashMap<>();
		String colectivoActual = null;

		for (String evento : eventos) {
			String idDetectado = detectarColectivoEnLinea(evento);
			if (idDetectado != null) {
				colectivoActual = idDetectado;
				eventosPorColectivo.putIfAbsent(colectivoActual, new ArrayList<>());
				eventosPorColectivo.get(colectivoActual).add(evento.trim());
			} else if (colectivoActual != null) {
				eventosPorColectivo.get(colectivoActual).add("  " + evento.trim());
			} else {
				// Eventos generales o de sistema (poco común, pero se muestran igual)
				eventosPorColectivo.putIfAbsent("GENERAL", new ArrayList<>());
				eventosPorColectivo.get("GENERAL").add(evento.trim());
			}
		}

		return eventosPorColectivo;
	}
    /**
     * Detecta el ID del colectivo en la línea de evento. Asume que los eventos
     * relevantes de colectivo arrancan con "Colectivo <ID>" o " Colectivo <ID>"
     * Devuelve el ID (ej: "C2-1"), o null si no detecta un colectivo.
     */
    public static String detectarColectivoEnLinea(String linea) {
        // Ejemplo: "Colectivo C2-1 de la línea ...", " Colectivo C2-1 ha llegado a la
        // terminal."
        String pattern = "Colectivo (C\\d+-\\d+)";
        java.util.regex.Matcher matcher = java.util.regex.Pattern.compile(pattern).matcher(linea);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

}