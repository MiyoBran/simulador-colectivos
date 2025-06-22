package ar.edu.unpsjb.ayp2.proyectointegrador.reporte;

import ar.edu.unpsjb.ayp2.proyectointegrador.logica.Simulador;

public class ReporteSimulacion {
	public static void imprimirEstadisticasCompletas(Simulador simulador) {
		System.out.println("\n--- Estadísticas de Satisfacción (Anexo I) ---");
		System.out.println(String.format("Índice de satisfacción: %.2f", simulador.getGestorEstadisticas().getIndiceSatisfaccion()));
		System.out.println("Desglose de calificaciones:");
		for (int cal = 5; cal >= 1; cal--) {
			int count = simulador.getGestorEstadisticas().getDesgloseCalificaciones().getOrDefault(cal, 0);
			System.out.println(String.format("  Calificación %d: %d pasajeros", cal, count));
		}
		System.out.println("\n--- Estadísticas de la Simulación ---");
		System.out.println(String.format("Pasajeros transportados: %d", simulador.getGestorEstadisticas().getPasajerosTransportados()));
		System.out.println(String.format("Tiempo promedio de espera: %.2f minutos", simulador.getGestorEstadisticas().getTiempoEsperaPromedio()));
		System.out.println(String.format("Tiempo promedio de viaje: %.2f minutos", simulador.getGestorEstadisticas().getTiempoViajePromedio()));
		System.out.println(String.format("Satisfacción promedio: %.1f", simulador.getGestorEstadisticas().getSatisfaccionPromedio()));
		System.out.println(String.format("%% Satisfechos: %.1f%%", simulador.getGestorEstadisticas().getPorcentajeSatisfechos()));
		System.out.println(String.format("%% Insatisfechos: %.1f%%", simulador.getGestorEstadisticas().getPorcentajeInsatisfechos()));
		// Agregar ocupación promedio general de colectivos
		var ocupaciones = simulador.getGestorEstadisticas().getOcupacionPromedioPorColectivo();
		if (!ocupaciones.isEmpty()) {
			double suma = 0.0;
			int cantidad = 0;
			for (var entry : ocupaciones.entrySet()) {
				suma += entry.getValue();
				cantidad++;
			}
			double promedioGeneral = cantidad > 0 ? (suma / cantidad) * 100 : 0.0;
			System.out.println(String.format("\nOcupación promedio general de colectivos: %.2f%%", promedioGeneral));
		}
	}

	public static void verificarConsistenciaEstadisticas(Simulador simulador) {
		var gestor = simulador.getGestorEstadisticas();
		int totalPasajerosGenerados = gestor.getPasajerosTotales();
		int pasajerosTransportados = gestor.getPasajerosTransportados();
		var desglose = gestor.getDesgloseCalificaciones();
		int sumaCalificaciones = desglose.values().stream().mapToInt(Integer::intValue).sum();
		double indiceSatisfaccion = gestor.getIndiceSatisfaccion();
		double satisfaccionPromedio = gestor.getSatisfaccionPromedio();
		double porcentajeSatisfechos = gestor.getPorcentajeSatisfechos();
		double porcentajeInsatisfechos = gestor.getPorcentajeInsatisfechos();

		// Check 1: Breakdown matches total generated or transported
		if (sumaCalificaciones != totalPasajerosGenerados && sumaCalificaciones != pasajerosTransportados) {
			System.err.println(
					"[ADVERTENCIA] El desglose de calificaciones no coincide con la cantidad de pasajeros generados ni transportados.");
		}

		// Check 2: Percentages add up to 100%
		double totalPorcentaje = porcentajeSatisfechos + porcentajeInsatisfechos;
		if (Math.abs(totalPorcentaje - 100.0) > 0.1) {
			System.err.println("[ADVERTENCIA] Los porcentajes de satisfechos e insatisfechos no suman 100%.");
		}

		// Check 3: Satisfaction index and average are compatible
		double calculoPromedio = 0.0;
		int totalCalificaciones = 0;
		for (int i = 1; i <= 5; i++) {
			int count = desglose.getOrDefault(i, 0);
			calculoPromedio += i * count;
			totalCalificaciones += count;
		}
		if (totalCalificaciones > 0) {
			double promedioEscala100 = calculoPromedio / totalCalificaciones * 20; // 1-5 mapped to 20-100
			if (Math.abs(promedioEscala100 - satisfaccionPromedio) > 1.0) {
				System.err.println("[ADVERTENCIA] La satisfacción promedio calculada no coincide con la reportada.");
			}
		}
	}

	public static void imprimirReportePasajeros(Simulador simulador) {
        var gestor = simulador.getGestorEstadisticas();
        var desglose = gestor.getDesglosePasajeros();
        int totalGenerados = gestor.getPasajerosTotales();
        int suma = desglose.getOrDefault("transportados", 0) + desglose.getOrDefault("bajadosForzosamente", 0)
                + desglose.getOrDefault("nuncaSubieron", 0);
        System.out.println("\n--- Reporte de Pasajeros ---");
        System.out.println(String.format("Total de pasajeros generados: %d", totalGenerados));
        System.out.println(String.format("Pasajeros transportados: %d", desglose.getOrDefault("transportados", 0)));
        System.out.println(String.format("Pasajeros bajados forzosamente en terminal: %d", desglose.getOrDefault("bajadosForzosamente", 0)));
        System.out.println(String.format("Pasajeros que nunca subieron a un colectivo: %d", desglose.getOrDefault("nuncaSubieron", 0)));
        if (suma != totalGenerados) {
            System.out.println(String.format("[ADVERTENCIA] La suma de pasajeros reportados no coincide con el total generado. Suma: %d, Total generados: %d", suma, totalGenerados));
        }
    }

	/**
	 * Imprime la ocupación promedio de cada colectivo y el promedio general.
	 */
	public static void imprimirOcupacionPromedioColectivos(Simulador simulador) {
        var ocupaciones = simulador.getGestorEstadisticas().getOcupacionPromedioPorColectivo();
        System.out.println("\n--- OCUPACIÓN PROMEDIO DE COLECTIVOS (Anexo II) ---");
        if (ocupaciones.isEmpty()) {
            System.out.println("No hay datos de ocupación registrados.");
        } else {
            var colectivosOrdenados = new java.util.ArrayList<>(simulador.getColectivosEnSimulacion());
            colectivosOrdenados.sort(COMPARATOR_POR_NUMERO_ID_COLECTIVO);
            double suma = 0.0;
            int cantidad = 0;
            for (var colectivo : colectivosOrdenados) {
                Double ocup = ocupaciones.get(colectivo.getIdColectivo());
                if (ocup != null) {
                    System.out.println(String.format("%s: %.2f%%", colectivo.getEtiqueta(), ocup * 100));
                    suma += ocup;
                    cantidad++;
                }
            }
            double promedioGeneral = cantidad > 0 ? (suma / cantidad) * 100 : 0.0;
            System.out.println(String.format("\nOcupación promedio general de colectivos: %.2f%%", promedioGeneral));
        }
    }

	/**
	 * Comparator para ordenar colectivos por el número extraído de su id
	 * (C#-linea).
	 */
	private static final java.util.Comparator<ar.edu.unpsjb.ayp2.proyectointegrador.modelo.Colectivo> COMPARATOR_POR_NUMERO_ID_COLECTIVO = (
			a, b) -> {
		try {
			int numA = extraerNumeroIdColectivo(a.getIdColectivo());
			int numB = extraerNumeroIdColectivo(b.getIdColectivo());
			return Integer.compare(numA, numB);
		} catch (Exception e) {
			return a.getIdColectivo().compareTo(b.getIdColectivo());
		}
	};

	/**
	 * Extrae el número del id del colectivo (formato C#-linea).
	 */
	private static int extraerNumeroIdColectivo(String id) {
		int start = id.indexOf('C') + 1;
		int end = id.indexOf('-');
		if (start > 0 && end > start) {
			return Integer.parseInt(id.substring(start, end));
		}
		throw new IllegalArgumentException("Formato de id de colectivo inválido: " + id);
	}
}