// Create this file at: /home/miyo/git/A-PII-UNPSJB/simulador-colectivos/src/main/java/ar/edu/unpsjb/ayp2/proyectointegrador/logica/SimulacionListener.java
package ar.edu.unpsjb.ayp2.proyectointegrador.logica;

/**
 * Interfaz para escuchar eventos de la simulación.
 * Permite separar la lógica de la presentación en la simulación.
 * 
 * @author MiyoBran
 */
public interface SimulacionListener {
    void onInicioSimulacion(int colectivos, int pasajeros);
    void onInicioCiclo(int numeroCiclo);
    void onMovimientoColectivo(String idColectivo, String lineaNombre, String paradaActual, String siguienteParada);
    void onBajaPasajero(String idPasajero);
    void onSubePasajero(String idPasajero, String destino);
    void onColectivoLleno(int pasajerosEsperando);
    void onEstadoParada(String nombreParada, int pasajerosEsperando);
    void onResumenCiclo(int ciclo, int esperando, int enColectivos, int transportados);
    void onMensajeSimulacion(String mensaje);
    void onFinSimulacion();
}
