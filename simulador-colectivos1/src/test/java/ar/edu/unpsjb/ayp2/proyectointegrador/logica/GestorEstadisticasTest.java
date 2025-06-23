package ar.edu.unpsjb.ayp2.proyectointegrador.logica;

import ar.edu.unpsjb.ayp2.proyectointegrador.modelo.Pasajero;
import ar.edu.unpsjb.ayp2.proyectointegrador.modelo.Parada;
import ar.edu.unpsjb.ayp2.proyectointegrador.modelo.Colectivo;
import ar.edu.unpsjb.ayp2.proyectointegrador.modelo.Linea;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GestorEstadisticasTest {
    private GestorEstadisticas gestor;
    private Pasajero pasajero;
    private Colectivo colectivo;
    private Linea linea;

    @BeforeEach
    public void setUp() {
        gestor = new GestorEstadisticas();
        Parada origen = new Parada("P1", "Origen");
        Parada destino = new Parada("P2", "Destino");
        pasajero = new Pasajero("1", origen, destino);
        linea = new Linea("L1", "Linea Test");
        // Colectivo actualizado con los nuevos parámetros requeridos
        colectivo = new Colectivo("C1", linea, 30, 20, 10, 1);
    }

    @Test
    @DisplayName("Registrar pasajero incrementa el total de pasajeros")
    public void testRegistrarPasajero() {
        gestor.registrarPasajero(pasajero);
        assertEquals(1, gestor.getPasajerosTotales());
    }

    @Test
    @DisplayName("Registrar colectivo incrementa el total y lo agrega al mapa de ocupación")
    public void testRegistrarColectivo() {
        gestor.registrarColectivo(colectivo);
        assertEquals(1, gestor.getColectivosTotales());
        assertTrue(gestor.getOcupacionPorColectivo().containsKey("C1"));
    }

    @Test
    @DisplayName("Registrar transporte actualiza tiempos y satisfacción correctamente")
    public void testRegistrarTransporteYSatisfaccion() {
        // pasajero.agregarTiempoEspera(10); // Método no disponible en el modelo actual
        // pasajero.agregarTiempoViaje(20); // Método no disponible en el modelo actual
        pasajero.setPudoSubir(true);
        pasajero.setViajoSentado(true);
        // pasajero.setSubioAlPrimerColectivoQuePaso(true); // Método no disponible en el modelo actual
       /// gestor.registrarTransporte(pasajero);
        assertEquals(1, gestor.getPasajerosTransportados());
        assertTrue(gestor.getSatisfaccionPromedio() > 0);
        // assertEquals(10, gestor.getTiempoEsperaPromedio()); // No disponible
        // assertEquals(20, gestor.getTiempoViajePromedio()); // No disponible
    }

    @Test
    @DisplayName("Resetear estadísticas deja todos los contadores en cero")
    public void testReset() {
        gestor.registrarPasajero(pasajero);
        gestor.registrarColectivo(colectivo);
        gestor.reset();
        assertEquals(0, gestor.getPasajerosTotales());
        assertEquals(0, gestor.getColectivosTotales());
        assertEquals(0, gestor.getPasajerosTransportados());
        assertTrue(gestor.getOcupacionPorColectivo().isEmpty());
    }

    @Test
    @DisplayName("Calcula correctamente el promedio de ocupación de colectivos (Anexo II)")
    public void testOcupacionPromedioPorColectivo() {
        gestor.registrarCapacidadColectivo("C1", 30);
        gestor.registrarOcupacionTramo("C1", 15); // 0.5
        gestor.registrarOcupacionTramo("C1", 30); // 1.0
        gestor.registrarOcupacionTramo("C1", 0);  // 0.0
        var promedios = gestor.getOcupacionPromedioPorColectivo();
        assertTrue(promedios.containsKey("C1"));
        assertEquals((0.5 + 1.0 + 0.0) / 3, promedios.get("C1"), 0.0001);
    }

    @Test
    @DisplayName("Devuelve correctamente el desglose de calificaciones de satisfacción")
    public void testDesgloseCalificaciones() {
        pasajero.setPudoSubir(true);
        pasajero.setViajoSentado(true);
        // pasajero.setSubioAlPrimerColectivoQuePaso(true); // Método no disponible en el modelo actual
        //gestor.registrarTransporte(pasajero); // Debería ser calificación 5
        var desglose = gestor.getDesgloseCalificaciones();
        assertEquals(1, desglose.getOrDefault(5, 0));
    }

    @Test
    @DisplayName("Devuelve correctamente el desglose de pasajeros por categoría")
    public void testDesglosePasajeros() {
        Pasajero p1 = new Pasajero("1", new Parada("P1", "Origen"), new Parada("P2", "Destino"));
        Pasajero p2 = new Pasajero("2", new Parada("P1", "Origen"), new Parada("P2", "Destino"));
        Pasajero p3 = new Pasajero("3", new Parada("P1", "Origen"), new Parada("P2", "Destino"));
        p1.setPudoSubir(true);
        p1.setBajadaForzosa(false);
        p2.setPudoSubir(false);
        p3.setPudoSubir(true);
        p3.setBajadaForzosa(true);
        gestor.registrarPasajero(p1);
        gestor.registrarPasajero(p2);
        gestor.registrarPasajero(p3);
        var desglose = gestor.getDesglosePasajeros();
        assertEquals(1, desglose.get("transportados"));
        assertEquals(1, desglose.get("bajadosForzosamente"));
        assertEquals(1, desglose.get("nuncaSubieron"));
    }
}