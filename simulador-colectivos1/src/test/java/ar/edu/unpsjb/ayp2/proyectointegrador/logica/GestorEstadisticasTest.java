package ar.edu.unpsjb.ayp2.proyectointegrador.logica;

import ar.edu.unpsjb.ayp2.proyectointegrador.modelo.Pasajero;
import ar.edu.unpsjb.ayp2.proyectointegrador.modelo.Parada;
import ar.edu.unpsjb.ayp2.proyectointegrador.modelo.Colectivo;
import ar.edu.unpsjb.ayp2.proyectointegrador.modelo.Linea;
import org.junit.jupiter.api.BeforeEach;
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
        colectivo = new Colectivo("C1", linea, 30);
    }

    @Test
    public void testRegistrarPasajero() {
        gestor.registrarPasajero(pasajero);
        assertEquals(1, gestor.getPasajerosTotales());
    }

    @Test
    public void testRegistrarColectivo() {
        gestor.registrarColectivo(colectivo);
        assertEquals(1, gestor.getColectivosTotales());
        assertTrue(gestor.getOcupacionPorColectivo().containsKey("C1"));
    }

    @Test
    public void testRegistrarTransporteYSatisfaccion() {
        pasajero.agregarTiempoEspera(10);
        pasajero.agregarTiempoViaje(20);
        pasajero.setPudoSubir(true);
        pasajero.setViajoSentado(true);
        pasajero.setSubioAlPrimerColectivoQuePaso(true);
        gestor.registrarTransporte(pasajero);
        assertEquals(1, gestor.getPasajerosTransportados());
        assertTrue(gestor.getSatisfaccionPromedio() > 0);
        assertEquals(10, gestor.getTiempoEsperaPromedio());
        assertEquals(20, gestor.getTiempoViajePromedio());
    }

    @Test
    public void testRegistrarOcupacion() {
        gestor.registrarColectivo(colectivo);
        gestor.registrarOcupacion("C1", 15);
        assertEquals(15, gestor.getOcupacionPorColectivo().get("C1"));
    }

    @Test
    public void testReset() {
        gestor.registrarPasajero(pasajero);
        gestor.registrarColectivo(colectivo);
        gestor.reset();
        assertEquals(0, gestor.getPasajerosTotales());
        assertEquals(0, gestor.getColectivosTotales());
        assertEquals(0, gestor.getPasajerosTransportados());
        assertTrue(gestor.getOcupacionPorColectivo().isEmpty());
    }
}