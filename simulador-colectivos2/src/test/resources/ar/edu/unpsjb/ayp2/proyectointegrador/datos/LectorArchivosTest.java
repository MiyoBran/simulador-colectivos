package ar.edu.unpsjb.ayp2.proyectointegrador.datos;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import ar.edu.unpsjb.ayp2.proyectointegrador.modelo.Linea;
import ar.edu.unpsjb.ayp2.proyectointegrador.modelo.Parada;

/**
 * Tests para la clase LectorArchivos, verificando la correcta carga de datos
 * y el manejo de errores de configuración y de archivos.
 */
@DisplayName("Tests para LectorArchivos")
class LectorArchivosTest {

    private LectorArchivos lector;

    // Archivos de prueba ubicados en src/test/resources/
    private final String PARADAS_VALIDAS = "datos_test/paradas_test.txt";
    private final String LINEAS_VALIDAS = "datos_test/lineas_test.txt";
    private final String ARCHIVO_INEXISTENTE = "archivo_que_no_existe.txt";

    /**
     * Crea una instancia de LectorArchivos con propiedades de prueba.
     */
    private LectorArchivos createLectorWithTestProperties(String paradaProp, String lineaProp) {
        Properties testProps = new Properties();
        if (paradaProp != null) {
            testProps.setProperty("parada", paradaProp);
        }
        if (lineaProp != null) {
            testProps.setProperty("linea", lineaProp);
        }
        return new LectorArchivos(testProps);
    }

    // --- Pruebas para cargarParadas ---
    @Test
    @DisplayName("Carga de Paradas: debería cargar exitosamente un archivo válido")
    void testCargarParadasExitoso() throws IOException {
        lector = createLectorWithTestProperties(PARADAS_VALIDAS, null);
        lector.cargarParadas();
        Map<String, Parada> paradas = lector.getParadasCargadas();

        assertEquals(4, paradas.size(), "Deberían cargarse 4 paradas válidas del archivo de prueba.");
        assertTrue(paradas.containsKey("P01"));
        assertEquals("Parada Valida 1", paradas.get("P01").getDireccion());
        
        // Verifica que las paradas inválidas del archivo de prueba no se cargaron.
        assertFalse(paradas.containsKey("P05"), "P05 (formato incorrecto) no debería cargarse.");
        assertFalse(paradas.containsKey("P06"), "P06 (lat no numérica) no debería cargarse.");
    }

    @Test
    @DisplayName("Carga de Paradas: debería lanzar FileNotFoundException si el archivo no existe")
    void testCargarParadasArchivoNoExiste() {
        lector = createLectorWithTestProperties(ARCHIVO_INEXISTENTE, null);
        assertThrows(FileNotFoundException.class, () -> {
            lector.cargarParadas();
        });
    }

    @Test
    @DisplayName("Carga de Paradas: debería lanzar IOException si la propiedad 'parada' no está definida")
    void testCargarParadasPropiedadNoDefinida() {
        lector = createLectorWithTestProperties(null, null); // Propiedad "parada" es null
        assertThrows(IOException.class, () -> {
            lector.cargarParadas();
        });
    }

    // --- Pruebas para cargarLineas ---
    @Test
    @DisplayName("Carga de Líneas: debería cargar exitosamente un archivo válido si las paradas ya existen")
    void testCargarLineasExitoso() throws IOException {
        lector = createLectorWithTestProperties(PARADAS_VALIDAS, LINEAS_VALIDAS);
        lector.cargarParadas(); // Pre-condición: cargar las paradas necesarias.
        lector.cargarLineas();
        Map<String, Linea> lineas = lector.getLineasCargadas();

        assertEquals(4, lineas.size(), "Deberían cargarse 4 líneas válidas.");
        assertTrue(lineas.containsKey("L01 - Linea Test Valida 1"));
        Linea l01 = lineas.get("L01 - Linea Test Valida 1");
        assertEquals(3, l01.longitudRecorrido());
        assertEquals("P01", l01.getParadaPorIndice(0).getId());

        // Verifica que una línea con recorrido vacío se cargue correctamente.
        assertTrue(lineas.containsKey("L05 - Linea Recorrido Vacio"));
        assertEquals(0, lineas.get("L05 - Linea Recorrido Vacio").longitudRecorrido());

        // Verifica que las líneas inválidas no se cargaron.
        assertFalse(lineas.containsKey("L04 - Linea Parada Inexistente"), "L04 (parada inexistente) no debería cargarse.");
    }
    
    @Test
    @DisplayName("Carga de Líneas: no debería cargar ninguna línea si las paradas no fueron cargadas previamente")
    void testCargarLineasSinHaberCargadoParadasAntes() throws IOException {
        lector = createLectorWithTestProperties(PARADAS_VALIDAS, LINEAS_VALIDAS);
        lector.cargarLineas();
        assertTrue(lector.getLineasCargadas().isEmpty(), "No debería cargarse ninguna línea si no hay paradas.");
    }

    @Test
    @DisplayName("Carga de Líneas: debería lanzar IOException si la propiedad 'linea' no está definida")
    void testCargarLineasPropiedadNoDefinida() {
        // CORRECCIÓN: El test ahora espera una IOException.
        lector = createLectorWithTestProperties(PARADAS_VALIDAS, null); // No se define la propiedad "linea"
        assertThrows(IOException.class, () -> {
            lector.cargarLineas();
        });
    }

    // --- Pruebas para cargarDatosCompletos ---
    @Test
    @DisplayName("Carga Completa: debería cargar paradas y líneas correctamente")
    void testCargarDatosCompletosExitoso() throws IOException {
        lector = createLectorWithTestProperties(PARADAS_VALIDAS, LINEAS_VALIDAS);
        lector.cargarDatosCompletos();
        
        assertEquals(4, lector.getParadasCargadas().size());
        assertEquals(4, lector.getLineasCargadas().size());
    }

    @Test
    @DisplayName("Carga Completa: debería ser idempotente y recargar correctamente")
    void testCargarDatosCompletosEsIdempotente() throws IOException {
        lector = createLectorWithTestProperties(PARADAS_VALIDAS, LINEAS_VALIDAS);
        
        lector.cargarDatosCompletos();
        int numParadasInicial = lector.getParadasCargadas().size();
        int numLineasInicial = lector.getLineasCargadas().size();

        // Volver a llamar no debería causar errores ni acumular datos
        lector.cargarDatosCompletos();
        assertEquals(numParadasInicial, lector.getParadasCargadas().size(), "cargarDatosCompletos no limpió/recargó paradas correctamente.");
        assertEquals(numLineasInicial, lector.getLineasCargadas().size(), "cargarDatosCompletos no limpió/recargó líneas correctamente.");
    }
}