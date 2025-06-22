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
        System.out.println("\n--- Ocupación promedio de colectivos (Anexo II) ---");
        for (var entry : simulador.getGestorEstadisticas().getOcupacionPromedioPorColectivo().entrySet()) {
            System.out.printf("Colectivo %s: %.2f\n", entry.getKey(), entry.getValue());
        }
        System.out.println("\n--- Estadísticas de la Simulación ---");
        System.out.println("Pasajeros transportados: " + simulador.getGestorEstadisticas().getPasajerosTransportados());
        System.out.println("Tiempo promedio de espera: " + simulador.getGestorEstadisticas().getTiempoEsperaPromedio());
        System.out.println("Tiempo promedio de viaje: " + simulador.getGestorEstadisticas().getTiempoViajePromedio());
        System.out.println("Satisfacción promedio: " + simulador.getGestorEstadisticas().getSatisfaccionPromedio());
        System.out.println("% Satisfechos: " + simulador.getGestorEstadisticas().getPorcentajeSatisfechos());
        System.out.println("% Insatisfechos: " + simulador.getGestorEstadisticas().getPorcentajeInsatisfechos());
    }
}
