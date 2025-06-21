package ar.edu.unpsjb.ayp2.proyectointegrador.logica;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ar.edu.unpsjb.ayp2.proyectointegrador.modelo.Colectivo;
import ar.edu.unpsjb.ayp2.proyectointegrador.modelo.Linea;
import ar.edu.unpsjb.ayp2.proyectointegrador.modelo.Parada;
import ar.edu.unpsjb.ayp2.proyectointegrador.modelo.Pasajero;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@DisplayName("Tests para la clase Simulador")
class SimuladorTest {

    private Map<String, Linea> lineasDePrueba;
    private Map<String, Parada> paradasDePrueba;
    private List<Pasajero> pasajerosDePrueba;

    private Parada p1, p2, p3, p4, p5;
    private Linea l1, l2;

    @BeforeEach
    void setUp() {
        // Inicializar paradas de prueba
        p1 = new Parada("P1", "Calle A 100", 0.0, 0.0);
        p2 = new Parada("P2", "Calle B 200", 0.0, 0.0);
        p3 = new Parada("P3", "Calle C 300", 0.0, 0.0);
        p4 = new Parada("P4", "Calle D 400", 0.0, 0.0);
        p5 = new Parada("P5", "Calle E 500", 0.0, 0.0);

        paradasDePrueba = new HashMap<>();
        paradasDePrueba.put("P1", p1);
        paradasDePrueba.put("P2", p2);
        paradasDePrueba.put("P3", p3);
        paradasDePrueba.put("P4", p4);
        paradasDePrueba.put("P5", p5);

        // Inicializar líneas de prueba
        l1 = new Linea("L1", "Linea Uno");
        l1.agregarParadaAlRecorrido(p1);
        l1.agregarParadaAlRecorrido(p2);
        l1.agregarParadaAlRecorrido(p3); // P1 -> P2 -> P3

        l2 = new Linea("L2", "Linea Dos");
        l2.agregarParadaAlRecorrido(p3);
        l2.agregarParadaAlRecorrido(p4);
        l2.agregarParadaAlRecorrido(p5); // P3 -> P4 -> P5

        lineasDePrueba = new HashMap<>();
        lineasDePrueba.put("L1 - Linea Uno", l1);
        lineasDePrueba.put("L2 - Linea Dos", l2);

        // Reiniciar pasajeros de prueba para cada test
        pasajerosDePrueba = new ArrayList<>();
    }

    /**
     * Método auxiliar para correr la simulación paso a paso hasta que termine.
     * Reemplaza la necesidad de llamar a un método monolítico y permite probar el estado final.
     * @param simulador la instancia de Simulador a ejecutar.
     */
    private void ejecutarSimulacionCompleta(Simulador simulador) {
        while (!simulador.isSimulacionTerminada()) {
            simulador.ejecutarPasoDeSimulacion();
        }
    }

    // --- Tests para el constructor de Simulador ---
    @Test
    @DisplayName("Constructor: debería crearse exitosamente con datos válidos")
    void testSimuladorConstructorExitoso() {
        assertDoesNotThrow(() -> {
            new Simulador(lineasDePrueba, paradasDePrueba, pasajerosDePrueba);
        });
    }

    @Test
    @DisplayName("Constructor: debería lanzar excepción si las líneas son nulas")
    void testSimuladorConstructorLineasNulas() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Simulador(null, paradasDePrueba, pasajerosDePrueba);
        }, "El simulador requiere líneas cargadas.");
    }
    
    // --- Tests para inicializarColectivos ---
    @Test
    @DisplayName("Inicialización: debería crear un colectivo por línea con los datos correctos")
    void testInicializarColectivosExitoso() {
        Simulador simulador = new Simulador(lineasDePrueba, paradasDePrueba, pasajerosDePrueba);
        simulador.inicializarColectivos(10);

        List<Colectivo> colectivos = simulador.getColectivosEnSimulacion();
        assertNotNull(colectivos);
        assertEquals(2, colectivos.size(), "Debería haber un colectivo por cada línea.");

        Colectivo c1 = colectivos.stream().filter(c -> c.getLineaAsignada().equals(l1)).findFirst().orElseThrow();
        assertEquals(l1, c1.getLineaAsignada());
        assertEquals(10, c1.getCapacidadMaxima());
        assertEquals(p1, c1.getParadaActual());

        Colectivo c2 = colectivos.stream().filter(c -> c.getLineaAsignada().equals(l2)).findFirst().orElseThrow();
        assertEquals(l2, c2.getLineaAsignada());
        assertEquals(10, c2.getCapacidadMaxima());
        assertEquals(p3, c2.getParadaActual());
    }
    
    // --- Tests para la ejecución de la simulación ---
    @Test
    @DisplayName("Simulación: debería completarse sin errores cuando no hay pasajeros")
    void testEjecutarSimulacionSinPasajeros() {
        Simulador simulador = new Simulador(lineasDePrueba, paradasDePrueba, new ArrayList<>());
        simulador.inicializarColectivos(10);

        // Ejecuta la simulación completa
        ejecutarSimulacionCompleta(simulador);

        // Verifica el estado final
        for (Colectivo c : simulador.getColectivosEnSimulacion()) {
            assertTrue(c.estaEnTerminal(), "Colectivo " + c.getIdColectivo() + " debería haber terminado su recorrido.");
            assertEquals(0, c.getCantidadPasajerosABordo());
        }
    }

    @Test
    @DisplayName("Simulación: pasajeros deberían subir y bajar correctamente")
    void testEjecutarSimulacionConPasajerosSubenYBajan() {
        // Arrange: preparar el escenario
        Pasajero pA = new Pasajero(p1, p3); // Pasajero para la Línea 1
        Pasajero pB = new Pasajero(p3, p5); // Pasajero para la Línea 2

        p1.agregarPasajero(pA);
        p3.agregarPasajero(pB);
        pasajerosDePrueba.add(pA);
        pasajerosDePrueba.add(pB);

        Simulador simulador = new Simulador(lineasDePrueba, paradasDePrueba, pasajerosDePrueba);
        simulador.inicializarColectivos(2); // Capacidad suficiente para todos

        // Act: ejecutar la simulación
        ejecutarSimulacionCompleta(simulador);

        // Assert: verificar los resultados
        for (Colectivo c : simulador.getColectivosEnSimulacion()) {
            assertTrue(c.estaEnTerminal(), "Cada colectivo debería haber terminado su recorrido.");
            assertEquals(0, c.getCantidadPasajerosABordo(), "No deberían quedar pasajeros a bordo.");
        }

        assertTrue(pA.isPudoSubir(), "Pasajero pA (L1) debería haber podido subir.");
        assertFalse(p1.hayPasajerosEsperando(), "Parada P1 debería estar vacía.");

        assertTrue(pB.isPudoSubir(), "Pasajero pB (L2) debería haber podido subir.");
    }

    @Test
    @DisplayName("Simulación: con capacidad limitada, un pasajero sube y el otro espera")
    void testEjecutarSimulacionCapacidadLimitada() {
        // Arrange
        Pasajero pA = new Pasajero(p1, p3);
        Pasajero pB = new Pasajero(p1, p3);
        p1.agregarPasajero(pA);
        p1.agregarPasajero(pB);
        pasajerosDePrueba.add(pA);
        pasajerosDePrueba.add(pB);

        Simulador simulador = new Simulador(lineasDePrueba, paradasDePrueba, pasajerosDePrueba);
        simulador.inicializarColectivos(1); // Capacidad de solo 1 para el colectivo de L1

        // Act
        ejecutarSimulacionCompleta(simulador);

        // Assert
        assertTrue(pA.isPudoSubir() ^ pB.isPudoSubir(), "Solo uno de los dos pasajeros debería haber subido (XOR).");

        Pasajero pasajeroQueSubio = pA.isPudoSubir() ? pA : pB;
        Pasajero pasajeroQueNoSubio = pA.isPudoSubir() ? pB : pA;

        assertTrue(pasajeroQueSubio.isPudoSubir(), "El pasajero que subió debe tener su flag en true.");
        assertEquals(0, pasajeroQueSubio.getColectivosEsperados(), "El pasajero que subió no debería haber esperado colectivos llenos.");
        
        assertFalse(pasajeroQueNoSubio.isPudoSubir(), "El pasajero que no subió debe tener su flag en false.");
        assertTrue(pasajeroQueNoSubio.getColectivosEsperados() > 0, "El pasajero que no subió debería haber esperado al menos un colectivo.");
        
        assertTrue(p1.hayPasajerosEsperando(), "La parada P1 debería tener todavía al pasajero que no subió.");
        assertEquals(1, p1.cantidadPasajerosEsperando(), "La parada P1 debería tener exactamente un pasajero esperando.");
    }
}