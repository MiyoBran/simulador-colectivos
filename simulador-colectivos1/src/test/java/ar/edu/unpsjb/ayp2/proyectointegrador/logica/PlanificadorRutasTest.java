package ar.edu.unpsjb.ayp2.proyectointegrador.logica;

import ar.edu.unpsjb.ayp2.proyectointegrador.modelo.Parada;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class PlanificadorRutasTest {
    private PlanificadorRutas planificador;
    private Parada p1, p2, p3, p4;

    @BeforeEach
    public void setUp() {
        planificador = new PlanificadorRutas();
        p1 = new Parada("P1", "Parada 1");
        p2 = new Parada("P2", "Parada 2");
        p3 = new Parada("P3", "Parada 3");
        p4 = new Parada("P4", "Parada 4");
    }

    @Test
    public void testAgregarParadaYConectar() {
        planificador.agregarParada(p1);
        planificador.agregarParada(p2);
        assertNotNull(planificador.getGrafo());
        assertEquals(2, planificador.getGrafo().numVertices());
        planificador.conectarParadas(p1, p2, 5);
        assertEquals(1, planificador.getGrafo().numEdges());
    }

    @Test
    public void testCalcularRutaOptimaDirecta() {
        planificador.conectarParadas(p1, p2, 10);
        List<Parada> ruta = planificador.calcularRutaOptima(p1, p2);
        assertEquals(2, ruta.size());
        assertEquals(p1, ruta.get(0));
        assertEquals(p2, ruta.get(1));
    }

    @Test
    public void testCalcularRutaOptimaConIntermedias() {
        planificador.conectarParadas(p1, p2, 2);
        planificador.conectarParadas(p2, p3, 2);
        planificador.conectarParadas(p1, p3, 10);
        List<Parada> ruta = planificador.calcularRutaOptima(p1, p3);
        assertEquals(3, ruta.size());
        assertEquals(p1, ruta.get(0));
        assertEquals(p2, ruta.get(1));
        assertEquals(p3, ruta.get(2));
    }

    @Test
    public void testRutaInexistente() {
        planificador.conectarParadas(p1, p2, 1);
        List<Parada> ruta = planificador.calcularRutaOptima(p1, p4);
        assertTrue(ruta.isEmpty());
    }

    @Test
    public void testRutaCiclo() {
        planificador.conectarParadas(p1, p2, 1);
        planificador.conectarParadas(p2, p3, 1);
        planificador.conectarParadas(p3, p1, 1);
        List<Parada> ruta = planificador.calcularRutaOptima(p1, p3);
        System.out.println("Ruta ciclo: " + ruta);
        assertEquals(3, ruta.size());
        assertEquals(p1, ruta.get(0));
        assertEquals(p2, ruta.get(1));
        assertEquals(p3, ruta.get(2));
    }
}