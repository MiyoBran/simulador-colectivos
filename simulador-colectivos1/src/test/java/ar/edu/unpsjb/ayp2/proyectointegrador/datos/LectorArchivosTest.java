package ar.edu.unpsjb.ayp2.proyectointegrador.datos;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import ar.edu.unpsjb.ayp2.proyectointegrador.modelo.Linea;
import ar.edu.unpsjb.ayp2.proyectointegrador.modelo.Parada;

/**
 * Tests para la clase LectorArchivos, verificando la correcta carga de datos y
 * el manejo de errores de configuración y de archivos.
 */
@DisplayName("Pruebas de la Clase LectorArchivos")
class LectorArchivosTest {

	private LectorArchivos lectorSUT;

	// Archivos de prueba ubicados en src/test/resources/
	private static final String PARADAS_VALIDAS = "datos_test/paradas_test.txt";
	private static final String LINEAS_VALIDAS = "datos_test/lineas_test.txt";
	private static final String ARCHIVO_INEXISTENTE = "archivo_que_no_existe.txt";

	private LectorArchivos createLectorWithTestProperties(String paradaProp, String lineaProp) {
		Properties testProps = new Properties();
		if (paradaProp != null) testProps.setProperty("parada", paradaProp);
		if (lineaProp != null) testProps.setProperty("linea", lineaProp);
		return new LectorArchivos(testProps);
	}

	@Nested
	@DisplayName("Pruebas de Carga de Paradas")
	class PruebasCargaParadas {

		@Test
		@DisplayName("Debería cargar exitosamente un archivo de paradas válido")
		void cargarParadasExitoso() throws IOException {
			lectorSUT = createLectorWithTestProperties(PARADAS_VALIDAS, null);
			lectorSUT.cargarParadas();
			Map<String, Parada> paradas = lectorSUT.getParadasCargadas();

			assertEquals(4, paradas.size(), "Deberían cargarse 4 paradas válidas del archivo.");
			assertTrue(paradas.containsKey("P01"));
			assertEquals("Parada Valida 1", paradas.get("P01").getDireccion());
			assertFalse(paradas.containsKey("P05"), "La parada con formato incorrecto no debería cargarse.");
		}

		@Test
		@DisplayName("Debería lanzar IOException si el archivo de paradas no existe")
		void cargarParadasArchivoNoExiste() {
			lectorSUT = createLectorWithTestProperties(ARCHIVO_INEXISTENTE, null);
			// CORRECCIÓN: Se espera IOException, que es lo que lanza el método si el recurso no se encuentra.
			assertThrows(IOException.class, () -> lectorSUT.cargarParadas());
		}

		@Test
		@DisplayName("Debería lanzar IOException si la propiedad 'parada' no está definida")
		void cargarParadasPropiedadNoDefinida() {
			lectorSUT = createLectorWithTestProperties(null, null);
			assertThrows(IOException.class, () -> lectorSUT.cargarParadas());
		}
	}

	@Nested
	@DisplayName("Pruebas de Carga de Líneas")
	class PruebasCargaLineas {

		@BeforeEach
		void setUp() throws IOException {
			// Pre-condición para los tests de líneas: cargar las paradas primero.
			lectorSUT = createLectorWithTestProperties(PARADAS_VALIDAS, LINEAS_VALIDAS);
			lectorSUT.cargarParadas();
		}

		@Test
		@DisplayName("Debería cargar exitosamente un archivo de líneas válido")
		void cargarLineasExitoso() throws IOException {
			lectorSUT.cargarLineas();
			Map<String, Linea> lineas = lectorSUT.getLineasCargadas();

			assertEquals(4, lineas.size(), "Deberían cargarse 4 líneas válidas.");
			
			String claveLinea1 = "L01 - Linea Test Valida 1";
			assertTrue(lineas.containsKey(claveLinea1));
			Linea l01 = lineas.get(claveLinea1);
			// CORRECCIÓN: Usamos getRecorrido().size()
			assertEquals(3, l01.getRecorrido().size());
			assertEquals("P01", l01.getParadaPorIndice(0).getId());

			assertFalse(lineas.containsKey("L04 - Linea Parada Inexistente"), "Línea con parada inexistente no debería cargarse.");
		}

		@Test
		@DisplayName("No debería cargar líneas si las paradas no fueron cargadas previamente")
		void cargarLineasSinParadasPrevias() throws IOException {
			LectorArchivos lectorSinParadas = createLectorWithTestProperties(PARADAS_VALIDAS, LINEAS_VALIDAS);
			// No llamamos a lectorSinParadas.cargarParadas()
			lectorSinParadas.cargarLineas();
			assertTrue(lectorSinParadas.getLineasCargadas().isEmpty(), "No deberían cargarse líneas sin paradas.");
		}

		@Test
		@DisplayName("Debería lanzar IOException si la propiedad 'linea' no está definida")
		void cargarLineasPropiedadNoDefinida() {
			// Creamos un lector que tiene la propiedad de paradas pero no la de líneas
			LectorArchivos lectorSinPropLinea = createLectorWithTestProperties(PARADAS_VALIDAS, null);
			
			// === MEJORA AÑADIDA AQUÍ ===
			// Para probar la carga de líneas, primero debemos cumplir la pre-condición de que las paradas existan.
			// Lo cargamos dentro de un assertDoesNotThrow para asegurarnos de que este paso funciona como se espera.
			assertDoesNotThrow(lectorSinPropLinea::cargarParadas, "La carga de paradas (pre-condición) no debería fallar.");

			// Ahora que las paradas están cargadas, el método SÍ intentará leer el archivo de líneas.
			// Como la propiedad 'linea' es nula, AHORA SÍ se lanzará la excepción esperada.
			assertThrows(IOException.class, lectorSinPropLinea::cargarLineas);
		}
	}
	
	@Nested
	@DisplayName("Pruebas de Carga Completa")
	class PruebasCargaCompleta {
		
		@Test
		@DisplayName("cargarDatosCompletos() debería invocar la carga de paradas y líneas")
		void cargarDatosCompletos() throws IOException {
			lectorSUT = createLectorWithTestProperties(PARADAS_VALIDAS, LINEAS_VALIDAS);
			lectorSUT.cargarDatosCompletos();

			assertEquals(4, lectorSUT.getParadasCargadas().size());
			assertEquals(4, lectorSUT.getLineasCargadas().size());
		}

		@Test
		@DisplayName("cargarDatosCompletos() debería ser idempotente y recargar los datos")
		void cargarDatosCompletosEsIdempotente() throws IOException {
			lectorSUT = createLectorWithTestProperties(PARADAS_VALIDAS, LINEAS_VALIDAS);
			lectorSUT.cargarDatosCompletos();

			// Modificamos los datos cargados para simular un estado "sucio"
			lectorSUT.getParadasCargadas().put("P99", new Parada("P99", "Parada Falsa"));
			
			// Volvemos a llamar al método
			lectorSUT.cargarDatosCompletos();

			assertEquals(4, lectorSUT.getParadasCargadas().size(), "La recarga debería limpiar los datos antiguos.");
			assertFalse(lectorSUT.getParadasCargadas().containsKey("P99"), "La parada falsa no debería existir después de recargar.");
		}
	}
}