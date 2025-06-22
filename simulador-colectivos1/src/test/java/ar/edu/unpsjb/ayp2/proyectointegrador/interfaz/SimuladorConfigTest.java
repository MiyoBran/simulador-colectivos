package ar.edu.unpsjb.ayp2.proyectointegrador.interfaz;

import org.junit.jupiter.api.*;
import java.util.Properties;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la clase SimuladorConfig.
 * Se verifica la correcta lectura de parámetros desde Properties y el uso de valores por defecto.
 */
@DisplayName("Tests para la clase SimuladorConfig")
class SimuladorConfigTest {
    private Properties props;

    @BeforeEach
    void setUp() {
        props = new Properties();
    }

    @Test
    @DisplayName("Debería obtener los nombres de archivo por defecto")
    void testArchivosPorDefecto() {
        assertEquals("lineas_pm_mapeadas.txt", SimuladorConfig.obtenerNombreArchivoLineas(props));
        assertEquals("paradas_pm_mapeadas.txt", SimuladorConfig.obtenerNombreArchivoParadas(props));
    }

    @Test
    @DisplayName("Debería obtener los nombres de archivo configurados")
    void testArchivosConfigurados() {
        props.setProperty("linea", "lineas_test.txt");
        props.setProperty("parada", "paradas_test.txt");
        assertEquals("lineas_test.txt", SimuladorConfig.obtenerNombreArchivoLineas(props));
        assertEquals("paradas_test.txt", SimuladorConfig.obtenerNombreArchivoParadas(props));
    }

    @Test
    @DisplayName("Debería obtener valores numéricos configurados y por defecto")
    void testValoresNumericos() {
        // Por defecto
        assertEquals(150, SimuladorConfig.obtenerCantidadPasajeros(props));
        assertEquals(30, SimuladorConfig.obtenerCapacidadColectivo(props));
        assertEquals(20, SimuladorConfig.obtenerCapacidadSentadosColectivo(props));
        assertEquals(1, SimuladorConfig.obtenerCantidadColectivosSimultaneosPorLinea(props));
        assertEquals(1, SimuladorConfig.obtenerRecorridosPorColectivo(props));
        assertEquals(10, SimuladorConfig.obtenerFrecuenciaSalidaColectivosMinutos(props));
        // Configurados
        props.setProperty("cantidadPasajeros", "99");
        props.setProperty("capacidadColectivo", "40");
        props.setProperty("capacidadSentadosColectivo", "25");
        props.setProperty("cantidad_de_colectivos_simultaneos_por_linea", "3");
        props.setProperty("recorridos_por_colectivo", "2");
        props.setProperty("frecuencia_salida_colectivos_minutos", "15");
        assertEquals(99, SimuladorConfig.obtenerCantidadPasajeros(props));
        assertEquals(40, SimuladorConfig.obtenerCapacidadColectivo(props));
        assertEquals(25, SimuladorConfig.obtenerCapacidadSentadosColectivo(props));
        assertEquals(3, SimuladorConfig.obtenerCantidadColectivosSimultaneosPorLinea(props));
        assertEquals(2, SimuladorConfig.obtenerRecorridosPorColectivo(props));
        assertEquals(15, SimuladorConfig.obtenerFrecuenciaSalidaColectivosMinutos(props));
    }

    @Test
    @DisplayName("Debería usar valores por defecto ante valores inválidos")
    void testValoresInvalidos() {
        props.setProperty("cantidadPasajeros", "abc");
        props.setProperty("capacidadColectivo", "-1");
        props.setProperty("capacidadSentadosColectivo", "xyz");
        props.setProperty("cantidad_de_colectivos_simultaneos_por_linea", "0");
        props.setProperty("recorridos_por_colectivo", "-5");
        props.setProperty("frecuencia_salida_colectivos_minutos", "no_num");
        assertEquals(150, SimuladorConfig.obtenerCantidadPasajeros(props));
        assertEquals(30, SimuladorConfig.obtenerCapacidadColectivo(props));
        assertEquals(20, SimuladorConfig.obtenerCapacidadSentadosColectivo(props));
        assertEquals(1, SimuladorConfig.obtenerCantidadColectivosSimultaneosPorLinea(props));
        assertEquals(1, SimuladorConfig.obtenerRecorridosPorColectivo(props));
        assertEquals(10, SimuladorConfig.obtenerFrecuenciaSalidaColectivosMinutos(props));
    }
}
