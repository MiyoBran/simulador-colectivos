package proyectointegrador.interfaz;

import org.junit.jupiter.api.*;

import java.util.Properties;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la clase SimuladorConfig.
 * Se verifica la correcta lectura de parámetros desde Properties y el uso de valores por defecto.
 * Cada propiedad de configuración se prueba en su propia clase anidada para máxima claridad.
 */
@DisplayName("Pruebas de la Clase SimuladorConfig")
class SimuladorConfigTest {
    
    private Properties props;

    @BeforeEach
    void setUp() {
        props = new Properties();
    }

    @Nested
    @DisplayName("Para Nombres de Archivo")
    class PruebasDeArchivos {
        @Test
        @DisplayName("Debería obtener los nombres por defecto si no están configurados")
        void archivosPorDefecto() {
            assertEquals("lineas_pm_mapeadas.txt", SimuladorConfig.obtenerNombreArchivoLineas(props));
            assertEquals("paradas_pm_mapeadas.txt", SimuladorConfig.obtenerNombreArchivoParadas(props));
        }

        @Test
        @DisplayName("Debería obtener los nombres configurados si existen")
        void archivosConfigurados() {
            props.setProperty("linea", "lineas_test.txt");
            props.setProperty("parada", "paradas_test.txt");
            assertEquals("lineas_test.txt", SimuladorConfig.obtenerNombreArchivoLineas(props));
            assertEquals("paradas_test.txt", SimuladorConfig.obtenerNombreArchivoParadas(props));
        }
    }

    @Nested
    @DisplayName("Para Cantidad de Pasajeros")
    class PruebasCantidadPasajeros {
        @Test
        @DisplayName("Debería usar el valor por defecto")
        void valorPorDefecto() {
            assertEquals(150, SimuladorConfig.obtenerCantidadPasajeros(props));
        }

        @Test
        @DisplayName("Debería usar el valor configurado")
        void valorConfigurado() {
            props.setProperty("cantidadPasajeros", "99");
            assertEquals(99, SimuladorConfig.obtenerCantidadPasajeros(props));
        }

        @Test
        @DisplayName("Debería usar el valor por defecto para entradas inválidas (texto, negativo)")
        void valorInvalido() {
            props.setProperty("cantidadPasajeros", "abc");
            assertEquals(150, SimuladorConfig.obtenerCantidadPasajeros(props), "Debe usar default para texto.");
            
            props.setProperty("cantidadPasajeros", "0");
            assertEquals(150, SimuladorConfig.obtenerCantidadPasajeros(props), "Debe usar default para valor menor a 1.");
        }
    }

    @Nested
    @DisplayName("Para Capacidad del Colectivo")
    class PruebasCapacidadColectivo {
        @Test
        @DisplayName("Debería usar el valor por defecto")
        void valorPorDefecto() {
            assertEquals(30, SimuladorConfig.obtenerCapacidadColectivo(props));
        }

        @Test
        @DisplayName("Debería usar el valor configurado")
        void valorConfigurado() {
            props.setProperty("capacidadColectivo", "45");
            assertEquals(45, SimuladorConfig.obtenerCapacidadColectivo(props));
        }

        @Test
        @DisplayName("Debería usar el valor por defecto para una entrada inválida")
        void valorInvalido() {
            props.setProperty("capacidadColectivo", "-10");
            assertEquals(30, SimuladorConfig.obtenerCapacidadColectivo(props));
        }
    }

    @Nested
    @DisplayName("Para Capacidad de Sentados")
    class PruebasCapacidadSentados {
        @Test
        @DisplayName("Debería usar el valor por defecto")
        void valorPorDefecto() {
            assertEquals(20, SimuladorConfig.obtenerCapacidadSentadosColectivo(props));
        }

        @Test
        @DisplayName("Debería usar el valor configurado (incluyendo 0)")
        void valorConfigurado() {
            props.setProperty("capacidadSentadosColectivo", "15");
            assertEquals(15, SimuladorConfig.obtenerCapacidadSentadosColectivo(props));

            props.setProperty("capacidadSentadosColectivo", "0");
            assertEquals(0, SimuladorConfig.obtenerCapacidadSentadosColectivo(props), "0 es un valor válido para asientos.");
        }

        @Test
        @DisplayName("Debería usar el valor por defecto para una entrada inválida")
        void valorInvalido() {
            props.setProperty("capacidadSentadosColectivo", "texto");
            assertEquals(20, SimuladorConfig.obtenerCapacidadSentadosColectivo(props));
        }
    }

    // Se podrían añadir @Nested classes para el resto de propiedades
    // (obtenerCantidadColectivosSimultaneosPorLinea, etc.) siguiendo el mismo patrón.
}