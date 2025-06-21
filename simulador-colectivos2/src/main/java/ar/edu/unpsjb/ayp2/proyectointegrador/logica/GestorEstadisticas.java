package ar.edu.unpsjb.ayp2.proyectointegrador.logica;

import ar.edu.unpsjb.ayp2.proyectointegrador.modelo.Pasajero;
import ar.edu.unpsjb.ayp2.proyectointegrador.modelo.Parada;
import ar.edu.unpsjb.ayp2.proyectointegrador.modelo.Colectivo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * GestorEstadisticas centraliza la recolección y cálculo de métricas de la simulación.
 * Permite registrar eventos relevantes y consultar estadísticas agregadas.
 *
 * @author Miyen
 * @version 2.0
 */
public class GestorEstadisticas {
    private int pasajerosTransportados;
    private int pasajerosTotales;
    private int colectivosTotales;
    private int colectivosEnServicio;
    private int pasajerosSatisfechos;
    private int pasajerosInsatisfechos;
    private int sumaTiemposEspera;
    private int sumaTiemposViaje;
    private int sumaSatisfaccion;
    private List<Pasajero> pasajeros;
    private Map<String, Integer> ocupacionPorColectivo;

    public GestorEstadisticas() {
        this.pasajerosTransportados = 0;
        this.pasajerosTotales = 0;
        this.colectivosTotales = 0;
        this.colectivosEnServicio = 0;
        this.pasajerosSatisfechos = 0;
        this.pasajerosInsatisfechos = 0;
        this.sumaTiemposEspera = 0;
        this.sumaTiemposViaje = 0;
        this.sumaSatisfaccion = 0;
        this.pasajeros = new ArrayList<>();
        this.ocupacionPorColectivo = new HashMap<>();
    }

    /** Registrar un pasajero en la simulación. */
    public void registrarPasajero(Pasajero p) {
        pasajeros.add(p);
        pasajerosTotales++;
    }

    /** Registrar un colectivo en la simulación. */
    public void registrarColectivo(Colectivo c) {
        colectivosTotales++;
        ocupacionPorColectivo.put(c.getIdColectivo(), 0);
    }

    /** Registrar que un pasajero fue transportado. */
    public void registrarTransporte(Pasajero p) {
        pasajerosTransportados++;
        sumaTiemposEspera += p.getTiempoEspera();
        sumaTiemposViaje += p.getTiempoViaje();
        int satisfaccion = p.calcularSatisfaccion();
        sumaSatisfaccion += satisfaccion;
        if (satisfaccion >= 60) {
            pasajerosSatisfechos++;
        } else {
            pasajerosInsatisfechos++;
        }
    }

    /** Registrar ocupación de un colectivo en un momento dado. */
    public void registrarOcupacion(String idColectivo, int ocupacion) {
        ocupacionPorColectivo.put(idColectivo, ocupacion);
    }

    /** Calcular el tiempo promedio de espera de los pasajeros transportados. */
    public double getTiempoEsperaPromedio() {
        return pasajerosTransportados == 0 ? 0 : (double) sumaTiemposEspera / pasajerosTransportados;
    }

    /** Calcular el tiempo promedio de viaje de los pasajeros transportados. */
    public double getTiempoViajePromedio() {
        return pasajerosTransportados == 0 ? 0 : (double) sumaTiemposViaje / pasajerosTransportados;
    }

    /** Calcular la satisfacción promedio de los pasajeros transportados. */
    public double getSatisfaccionPromedio() {
        return pasajerosTransportados == 0 ? 0 : (double) sumaSatisfaccion / pasajerosTransportados;
    }

    /** Obtener el porcentaje de pasajeros satisfechos. */
    public double getPorcentajeSatisfechos() {
        return pasajerosTransportados == 0 ? 0 : 100.0 * pasajerosSatisfechos / pasajerosTransportados;
    }

    /** Obtener el porcentaje de pasajeros insatisfechos. */
    public double getPorcentajeInsatisfechos() {
        return pasajerosTransportados == 0 ? 0 : 100.0 * pasajerosInsatisfechos / pasajerosTransportados;
    }

    /** Obtener la ocupación máxima registrada por colectivo. */
    public Map<String, Integer> getOcupacionPorColectivo() {
        return new HashMap<>(ocupacionPorColectivo);
    }

    /** Obtener la cantidad total de pasajeros transportados. */
    public int getPasajerosTransportados() {
        return pasajerosTransportados;
    }

    /** Obtener la cantidad total de pasajeros simulados. */
    public int getPasajerosTotales() {
        return pasajerosTotales;
    }

    /** Obtener la cantidad total de colectivos simulados. */
    public int getColectivosTotales() {
        return colectivosTotales;
    }

    /** Resetear todas las estadísticas (útil para nuevas simulaciones). */
    public void reset() {
        pasajerosTransportados = 0;
        pasajerosTotales = 0;
        colectivosTotales = 0;
        colectivosEnServicio = 0;
        pasajerosSatisfechos = 0;
        pasajerosInsatisfechos = 0;
        sumaTiemposEspera = 0;
        sumaTiemposViaje = 0;
        sumaSatisfaccion = 0;
        pasajeros.clear();
        ocupacionPorColectivo.clear();
    }
}
