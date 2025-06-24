package proyectointegrador.interfaz;

import proyectointegrador.interfaz.SimuladorController;
import proyectointegrador.logica.Simulador;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas (de integración) para la clase SimuladorController.
 * Se verifica que el proceso de inicialización completo se ejecute correctamente
 * y que el estado del controlador sea consistente.
 */
@DisplayName("Pruebas de la Clase SimuladorController")
class SimuladorControllerTest {
    private SimuladorController controller;

    @BeforeEach
    void setUp() {
        controller = new SimuladorController();
    }

    @Nested
    @DisplayName("Pruebas del Proceso de Inicialización")
    class PruebasDeInicializacion {

        @Test
        @DisplayName("Debería inicializar por completo sin lanzar excepciones")
        void inicializarNoLanzaExcepciones() {
            // Este test verifica que toda la cadena de inicialización (carga de archivos,
            // generación de pasajeros, etc.) funcione correctamente con los datos reales del proyecto.
            assertDoesNotThrow(() -> controller.inicializar());
        }

        @Test
        @DisplayName("Debería lanzar una excepción si se intenta usar el simulador antes de inicializar")
        void usarSimuladorAntesDeInicializarLanzaExcepcion() {
            // Se espera NullPointerException porque controller.getSimulador() devuelve null
            assertThrows(NullPointerException.class, () -> controller.getSimulador().getColectivosEnSimulacion());
        }

        @Test
        @DisplayName("Debería permitir múltiples inicializaciones sin error (ser idempotente)")
        void inicializarMultiplesVeces() {
            assertDoesNotThrow(() -> controller.inicializar(), "La primera inicialización debe ser exitosa.");
            assertDoesNotThrow(() -> controller.inicializar(), "La segunda inicialización también debe ser exitosa.");
        }
    }

    @Nested
    @DisplayName("Pruebas de Estado Post-Inicialización")
    class PruebasDeEstado {

        @BeforeEach
        void inicializarControlador() {
            // Nos aseguramos de que el controlador esté inicializado para todos los tests de este grupo.
            controller.inicializar();
        }

        @Test
        @DisplayName("Debería haber cargado datos en todas sus entidades")
        void gettersDevuelvenDatosCargados() {
            assertNotNull(controller.getParadasCargadas(), "Las paradas no deben ser nulas.");
            assertNotNull(controller.getLineasCargadas(), "Las líneas no deben ser nulas.");
            assertNotNull(controller.getPasajerosGenerados(), "Los pasajeros no deben ser nulos.");
            assertNotNull(controller.getSimulador(), "El simulador no debe ser nulo.");
            
            assertFalse(controller.getParadasCargadas().isEmpty(), "Debe haber paradas cargadas.");
            assertFalse(controller.getLineasCargadas().isEmpty(), "Debe haber líneas cargadas.");
            assertFalse(controller.getPasajerosGenerados().isEmpty(), "Debe haber pasajeros generados.");
        }

        @Test
        @DisplayName("Debería devolver siempre la misma instancia de sus componentes")
        void gettersSonConsistentes() {
            // Verificamos que no se creen nuevas instancias en cada llamada a los getters.
            Simulador sim1 = controller.getSimulador();
            Simulador sim2 = controller.getSimulador();
            assertSame(sim1, sim2, "El controlador debe devolver siempre la misma instancia del simulador.");

            var paradas1 = controller.getParadasCargadas();
            var paradas2 = controller.getParadasCargadas();
            assertSame(paradas1, paradas2, "El controlador debe devolver siempre la misma instancia del mapa de paradas.");
        }
    }
}