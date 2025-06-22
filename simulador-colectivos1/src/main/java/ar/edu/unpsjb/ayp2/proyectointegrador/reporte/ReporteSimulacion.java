package ar.edu.unpsjb.ayp2.proyectointegrador.reporte;

import ar.edu.unpsjb.ayp2.proyectointegrador.logica.Simulador;

public class ReporteSimulacion {
    public static void imprimirEstadisticasCompletas(Simulador simulador) {
        System.out.println("\n--- Estadísticas de Satisfacción (Anexo I) ---");
        System.out.printf("Índice de satisfacción: %.2f\n", simulador.getGestorEstadisticas().getIndiceSatisfaccion());
        System.out.println("Desglose de calificaciones:");
        for (int cal = 5; cal >= 1; cal--) {
            int count = simulador.getGestorEstadisticas().getDesgloseCalificaciones().getOrDefault(cal, 0);
            System.out.println("  Calificación " + cal + ": " + count + " pasajeros");
        }
        System.out.println("\n--- Estadísticas de la Simulación ---");
        System.out.println("Pasajeros transportados: " + simulador.getGestorEstadisticas().getPasajerosTransportados());
        System.out.println("Tiempo promedio de espera: " + String.format("%.2f", simulador.getGestorEstadisticas().getTiempoEsperaPromedio()) + " minutos");
        System.out.println("Tiempo promedio de viaje: " + String.format("%.2f", simulador.getGestorEstadisticas().getTiempoViajePromedio()) + " minutos");
        System.out.println("Satisfacción promedio: " + String.format("%.1f", simulador.getGestorEstadisticas().getSatisfaccionPromedio()));
        System.out.println("% Satisfechos: " + String.format("%.1f", simulador.getGestorEstadisticas().getPorcentajeSatisfechos()) + "%");
        System.out.println("% Insatisfechos: " + String.format("%.1f", simulador.getGestorEstadisticas().getPorcentajeInsatisfechos()) + "%");
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
            System.out.printf("\nOcupación promedio general de colectivos: %.2f%%\n", promedioGeneral);
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
            System.err.println("[ADVERTENCIA] El desglose de calificaciones no coincide con la cantidad de pasajeros generados ni transportados.");
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
        int suma = desglose.getOrDefault("transportados", 0)
                + desglose.getOrDefault("bajadosForzosamente", 0)
                + desglose.getOrDefault("nuncaSubieron", 0);
        System.out.println("\n--- Reporte de Pasajeros ---");
        System.out.println("Total de pasajeros generados: " + totalGenerados);
        System.out.println("Pasajeros transportados: " + desglose.getOrDefault("transportados", 0));
        System.out.println("Pasajeros bajados forzosamente en terminal: " + desglose.getOrDefault("bajadosForzosamente", 0));
        System.out.println("Pasajeros que nunca subieron a un colectivo: " + desglose.getOrDefault("nuncaSubieron", 0));
        if (suma != totalGenerados) {
            System.out.println("[ADVERTENCIA] La suma de pasajeros reportados no coincide con el total generado. Suma: " + suma + ", Total generados: " + totalGenerados);
        }
    }
}