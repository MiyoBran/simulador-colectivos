package ar.edu.unpsjb.ayp2.proyectointegrador.interfaz;

import ar.edu.unpsjb.ayp2.proyectointegrador.logica.Simulador;
import ar.edu.unpsjb.ayp2.proyectointegrador.modelo.Parada;
import ar.edu.unpsjb.ayp2.proyectointegrador.modelo.Linea;
import ar.edu.unpsjb.ayp2.proyectointegrador.modelo.Pasajero;
import org.junit.jupiter.api.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la clase SimuladorController.
 * Se verifica la inicialización, getters y el manejo de datos cargados.
 */
@DisplayName("Tests para la clase SimuladorController")
class SimuladorControllerTest {
    private SimuladorController controller;

    @BeforeEach
    void setUp() {
        controller = new SimuladorController();
    }

    @Test
    @DisplayName("Debería inicializar correctamente y cargar datos")
    void testInicializarYCargarDatos() {
        assertDoesNotThrow(() -> controller.inicializar());
        assertNotNull(controller.getParadasCargadas(), "Las paradas deben estar cargadas");
        assertNotNull(controller.getLineasCargadas(), "Las líneas deben estar cargadas");
        assertNotNull(controller.getConfigProperties(), "Las propiedades deben estar cargadas");
        assertNotNull(controller.getPasajerosGenerados(), "Los pasajeros deben estar generados");
        assertNotNull(controller.getSimulador(), "El simulador debe estar inicializado");
        assertTrue(controller.getParadasCargadas().size() > 0, "Debe haber al menos una parada cargada");
        assertTrue(controller.getLineasCargadas().size() > 0, "Debe haber al menos una línea cargada");
        assertTrue(controller.getPasajerosGenerados().size() > 0, "Debe haber al menos un pasajero generado");
    }

    @Test
    @DisplayName("Debería devolver el mismo simulador tras inicializar")
    void testGetSimulador() {
        controller.inicializar();
        Simulador sim1 = controller.getSimulador();
        Simulador sim2 = controller.getSimulador();
        assertSame(sim1, sim2, "Debe devolver la misma instancia de simulador");
    }

    @Test
    @DisplayName("Debería devolver los mismos datos cargados tras inicializar")
    void testGettersConsistentes() {
        controller.inicializar();
        Map<String, Parada> paradas = controller.getParadasCargadas();
        Map<String, Linea> lineas = controller.getLineasCargadas();
        List<Pasajero> pasajeros = controller.getPasajerosGenerados();
        assertEquals(paradas, controller.getParadasCargadas());
        assertEquals(lineas, controller.getLineasCargadas());
        assertEquals(pasajeros, controller.getPasajerosGenerados());
    }

    @Test
    @DisplayName("Debería lanzar excepción si se accede al simulador antes de inicializar")
    void testGetSimuladorAntesDeInicializar() {
        Exception ex = assertThrows(NullPointerException.class, () -> controller.getSimulador().getColectivosEnSimulacion());
        assertNotNull(ex, "Debe lanzar excepción si se accede al simulador sin inicializar");
    }

    @Test
    @DisplayName("Debería permitir múltiples inicializaciones sin error")
    void testInicializarMultiplesVeces() {
        assertDoesNotThrow(() -> controller.inicializar());
        assertDoesNotThrow(() -> controller.inicializar());
        assertNotNull(controller.getSimulador(), "El simulador debe seguir disponible tras reinicializar");
    }
}