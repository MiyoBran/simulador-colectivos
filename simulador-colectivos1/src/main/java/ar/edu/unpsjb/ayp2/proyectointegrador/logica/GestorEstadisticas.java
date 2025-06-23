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

    // Para índice de satisfacción (Anexo I)
   
    private Map<Integer, Integer> conteoCalificaciones; // calificación (1-5) -> cantidad
  
    private int sumaCalificaciones;
    private double indiceSatisfaccion;
    private int totalPasajerosCalificados;

    // Para ocupación promedio (Anexo II)
    private final Map<String, List<Double>> ocupacionesPorColectivo; // idColectivo -> lista de ocupaciones por tramo
    private final Map<String, Integer> capacidadPorColectivo;

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

        this.conteoCalificaciones = new HashMap<>();
        this.sumaCalificaciones = 0;
        this.totalPasajerosCalificados = 0;
        this.ocupacionesPorColectivo = new HashMap<>();
        this.capacidadPorColectivo = new HashMap<>();
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
       // sumaTiemposEspera += p.getTiempoEspera();
       //sumaTiemposViaje += p.getTiempoViaje();
        int satisfaccion = p.calcularSatisfaccion();
        sumaSatisfaccion += satisfaccion;
        if (satisfaccion >= 3) {
            pasajerosSatisfechos++;
        } else {
            pasajerosInsatisfechos++;
        }
        // --- NUEVO: Actualizar desglose de calificaciones y stats de satisfacción ---
        //int calificacion = mapSatisfaccionToCalificacion(satisfaccion);
        conteoCalificaciones.put(satisfaccion, conteoCalificaciones.getOrDefault(satisfaccion, 0)+ 1);
        sumaCalificaciones += satisfaccion * 20; // Mapeo directo de 1-5 a 20-100
        totalPasajerosCalificados++;
    }

    /** Mapea la satisfacción (0-100) a calificación 1-5. */
    private int mapSatisfaccionToCalificacion(int satisfaccion) {
        if (satisfaccion >= 80) return 5;
        if (satisfaccion >= 60) return 4;
        if (satisfaccion >= 40) return 3;
        if (satisfaccion >= 20) return 2;
        return 1;
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
        // Ahora se calcula como el promedio de calificaciones (1-5) 
        if (pasajerosTransportados == 0 || totalPasajerosCalificados == 0) return 0;
        return ((double) sumaCalificaciones / totalPasajerosCalificados);
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

    /** Registrar la calificación de satisfacción de un pasajero (Anexo I). */
    @Deprecated
    public void registrarCalificacionSatisfaccion(int calificacion) {
        // Este método ya no debe usarse directamente. El desglose se actualiza en registrarTransporte.
    }

    /** Devuelve el índice de satisfacción según Anexo I. */
    public double getIndiceSatisfaccion() {
        if (totalPasajerosCalificados == 0) return 0.0;
        for(Map.Entry<Integer, Integer> entry : conteoCalificaciones.entrySet()) {
			int calificacion = entry.getKey();
			int cantidad = entry.getValue();
			// Calcular la suma ponderada de calificaciones
			indiceSatisfaccion += calificacion * cantidad;
        	
        	
        	
        }
        return (double) indiceSatisfaccion / (totalPasajerosCalificados * 5);//
    }

    /** Devuelve el desglose de calificaciones. */
    public Map<Integer, Integer> getDesgloseCalificaciones() {
        return new HashMap<>(conteoCalificaciones);
    }

    /** Registrar la capacidad máxima de un colectivo (para ocupación promedio). */
    public void registrarCapacidadColectivo(String idColectivo, int capacidadMaxima) {
        capacidadPorColectivo.put(idColectivo, capacidadMaxima);
    }

    /** Registrar ocupación de un colectivo en un tramo (Anexo II). */
    public void registrarOcupacionTramo(String idColectivo, int pasajerosEnTramo) {
        Integer capacidad = capacidadPorColectivo.get(idColectivo);
        if (capacidad == null || capacidad == 0) return;
        double ocupacion = (double) pasajerosEnTramo / capacidad;
        ocupacionesPorColectivo.computeIfAbsent(idColectivo, k -> new ArrayList<>()).add(ocupacion);
    }

    /** Devuelve el promedio de ocupación por colectivo (Anexo II). */
    public Map<String, Double> getOcupacionPromedioPorColectivo() {
        Map<String, Double> promedios = new HashMap<>();
        for (Map.Entry<String, List<Double>> entry : ocupacionesPorColectivo.entrySet()) {
            List<Double> lista = entry.getValue();
            if (!lista.isEmpty()) {
                double suma = 0.0;
                for (double o : lista) suma += o;
                promedios.put(entry.getKey(), suma / lista.size());
            }
        }
        return promedios;
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
        conteoCalificaciones.clear();
        sumaCalificaciones = 0;
        totalPasajerosCalificados = 0;
        ocupacionesPorColectivo.clear();
        capacidadPorColectivo.clear();
    }

    /**
     * Devuelve un desglose de los pasajeros por categoría:
     * - "transportados": llegaron a destino
     * - "bajadosForzosamente": bajados en terminal sin llegar a destino
     * - "nuncaSubieron": nunca subieron a un colectivo (siguen esperando)
     */
    public Map<String, Integer> getDesglosePasajeros() {
        int transportados = 0;
        int bajadosForzosamente = 0;
        int nuncaSubieron = 0;
        for (Pasajero p : pasajeros) {
            if (p.isBajadaForzosa()) {
                bajadosForzosamente++;
            } else if (!p.isPudoSubir()) {
                nuncaSubieron++;
            } else if (p.isPudoSubir() && !p.isBajadaForzosa()) {
                transportados++;
            }
        }
        Map<String, Integer> desglose = new HashMap<>();
        desglose.put("transportados", transportados);
        desglose.put("bajadosForzosamente", bajadosForzosamente);
        desglose.put("nuncaSubieron", nuncaSubieron);
        return desglose;
    }
}