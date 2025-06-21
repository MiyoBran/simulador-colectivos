package ar.edu.unpsjb.ayp2.proyectointegrador.modelo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

/**
 * Pruebas unitarias para la clase Linea.
 * Verifica la correcta creación, gestión del recorrido y comportamiento de los métodos.
 */
@DisplayName("Tests para la Clase Linea")
class LineaTest {

	private Linea lineaSUT; // Subject Under Test
	private Parada p1, p2, p3;

	@BeforeEach
	void setUp() {
		lineaSUT = new Linea("L01", "Linea Principal");
		p1 = new Parada("P01", "Parada Uno");
		p2 = new Parada("P02", "Parada Dos");
		p3 = new Parada("P03", "Parada Tres");
	}

	@Test
	@DisplayName("Constructor: debería crear una línea con datos válidos y recorrido vacío")
	void crearLineaConDatosValidos() {
		assertEquals("L01", lineaSUT.getId());
		assertEquals("Linea Principal", lineaSUT.getNombre());
		assertTrue(lineaSUT.getRecorrido().isEmpty(), "El recorrido de una nueva línea debería estar vacío.");
		assertEquals(0, lineaSUT.longitudRecorrido());
	}

	@Test
	@DisplayName("Constructor: debería lanzar excepción si el ID es nulo")
	void crearLineaConIdNuloLanzaExcepcion() {
		assertThrows(IllegalArgumentException.class, () -> new Linea(null, "Nombre Valido"));
	}

	@Test
	@DisplayName("Constructor: debería lanzar excepción si el nombre está vacío")
	void crearLineaConNombreVacioLanzaExcepcion() {
		assertThrows(IllegalArgumentException.class, () -> new Linea("IDValido", "  "));
	}

	@Test
	@DisplayName("Recorrido: agregar paradas debería aumentar la longitud y actualizar extremos")
	void agregarParadaAlRecorrido() {
		lineaSUT.agregarParadaAlRecorrido(p1);
		assertEquals(1, lineaSUT.longitudRecorrido());
		assertEquals(p1, lineaSUT.getPrimeraParada());
		assertEquals(p1, lineaSUT.getUltimaParada());

		lineaSUT.agregarParadaAlRecorrido(p2);
		assertEquals(2, lineaSUT.longitudRecorrido());
		assertEquals(p1, lineaSUT.getPrimeraParada());
		assertEquals(p2, lineaSUT.getUltimaParada());
	}

	@Test
	@DisplayName("Recorrido: agregar una parada nula debería lanzar excepción")
	void agregarParadaNulaAlRecorridoLanzaExcepcion() {
		assertThrows(IllegalArgumentException.class, () -> lineaSUT.agregarParadaAlRecorrido(null));
	}

	@Test
	@DisplayName("Recorrido: agregar la misma parada consecutivamente no debería tener efecto")
	void agregarMismaParadaConsecutivaNoLaAnade() {
		lineaSUT.agregarParadaAlRecorrido(p1);
		int longitudOriginal = lineaSUT.longitudRecorrido();
		
		lineaSUT.agregarParadaAlRecorrido(p1); // Intento de agregar duplicado consecutivo
		
		assertEquals(longitudOriginal, lineaSUT.longitudRecorrido(), "Agregar la misma parada consecutiva no debería aumentar la longitud.");
	}

	@Test
	@DisplayName("Encapsulamiento: getRecorrido() debería devolver una copia defensiva")
	void getRecorridoDevuelveCopia() {
		lineaSUT.agregarParadaAlRecorrido(p1);
		List<Parada> recorridoObtenido1 = lineaSUT.getRecorrido();
		List<Parada> recorridoObtenido2 = lineaSUT.getRecorrido();

		assertNotSame(recorridoObtenido1, recorridoObtenido2, "Cada llamada a getRecorrido debe devolver una nueva instancia de lista.");
		
		// Modificar la lista original no debe afectar la copia ya obtenida
		lineaSUT.agregarParadaAlRecorrido(p2);
		assertEquals(1, recorridoObtenido1.size(), "La copia obtenida no debe cambiar si la original se modifica después.");
		
		// Modificar la copia no debe afectar la lista original
		recorridoObtenido1.add(p3);
		assertEquals(2, lineaSUT.longitudRecorrido(), "La lista original no debe cambiar si se modifica la copia.");
	}

	@Test
	@DisplayName("Búsqueda: getParadaPorIndice() debería funcionar con índices válidos")
	void getParadaPorIndiceValido() {
		lineaSUT.agregarParadaAlRecorrido(p1);
		lineaSUT.agregarParadaAlRecorrido(p2);
		assertEquals(p1, lineaSUT.getParadaPorIndice(0));
		assertEquals(p2, lineaSUT.getParadaPorIndice(1));
	}

	@Test
	@DisplayName("Búsqueda: getParadaPorIndice() debería lanzar excepción para índices fuera de rango")
	void getParadaPorIndiceFueraDeRangoLanzaExcepcion() {
		lineaSUT.agregarParadaAlRecorrido(p1);
		assertThrows(IndexOutOfBoundsException.class, () -> lineaSUT.getParadaPorIndice(-1));
		assertThrows(IndexOutOfBoundsException.class, () -> lineaSUT.getParadaPorIndice(1));
	}
	
	@Test
	@DisplayName("Búsqueda: getParadaPorIndice() en recorrido vacío debería lanzar excepción")
	void getParadaPorIndiceEnRecorridoVacioLanzaExcepcion() {
		assertTrue(lineaSUT.getRecorrido().isEmpty());
		assertThrows(IndexOutOfBoundsException.class, () -> lineaSUT.getParadaPorIndice(0));
	}

	@Test
	@DisplayName("Extremos: getPrimeraParada() y getUltimaParada() deberían funcionar correctamente")
	void getPrimeraYUltimaParada() {
		assertNull(lineaSUT.getPrimeraParada(), "Primera parada de línea vacía debería ser null.");
		assertNull(lineaSUT.getUltimaParada(), "Última parada de línea vacía debería ser null.");

		lineaSUT.agregarParadaAlRecorrido(p1);
		assertEquals(p1, lineaSUT.getPrimeraParada());
		assertEquals(p1, lineaSUT.getUltimaParada());

		lineaSUT.agregarParadaAlRecorrido(p2);
		assertEquals(p1, lineaSUT.getPrimeraParada());
		assertEquals(p2, lineaSUT.getUltimaParada());
	}
	
	@Test
	@DisplayName("Búsqueda: tieneParadaEnRecorrido() debería funcionar correctamente")
	void testTieneParadaEnRecorrido() {
		assertFalse(lineaSUT.tieneParadaEnRecorrido(p1), "Recorrido vacío no debería contener p1.");
		assertFalse(lineaSUT.tieneParadaEnRecorrido(null), "Verificar por null debería devolver false.");

		lineaSUT.agregarParadaAlRecorrido(p1);
		assertTrue(lineaSUT.tieneParadaEnRecorrido(p1), "Recorrido debería contener p1.");
		assertFalse(lineaSUT.tieneParadaEnRecorrido(p2), "Recorrido no debería contener p2 todavía.");
	}

	@Test
	@DisplayName("Búsqueda: getIndiceParada() debería devolver el índice correcto o -1")
	void testGetIndiceParada() {
		assertEquals(-1, lineaSUT.getIndiceParada(p1));
		lineaSUT.agregarParadaAlRecorrido(p1);
		lineaSUT.agregarParadaAlRecorrido(p2);
		assertEquals(0, lineaSUT.getIndiceParada(p1));
		assertEquals(1, lineaSUT.getIndiceParada(p2));
		assertEquals(-1, lineaSUT.getIndiceParada(p3));
		assertEquals(-1, lineaSUT.getIndiceParada(null));
	}
	
	@Test
	@DisplayName("Equals/HashCode: deberían basarse únicamente en el ID de la línea")
	void testEqualsYHashCode() {
		Linea linea1 = new Linea("L99", "Linea Alfa");
		Linea linea2 = new Linea("L99", "Linea Beta"); // Mismo ID, diferente nombre
		Linea linea3 = new Linea("L100", "Linea Alfa"); // Diferente ID

		assertEquals(linea1, linea2, "Líneas con el mismo ID deben ser iguales.");
		assertEquals(linea1.hashCode(), linea2.hashCode(), "Líneas con el mismo ID deben tener el mismo hashCode.");
		assertNotEquals(linea1, linea3, "Líneas con diferente ID no deben ser iguales.");
	}
	
	@Test
	@DisplayName("toString: debería generar una representación en texto correcta")
	void testToString() {
		String strVacia = lineaSUT.toString();
		assertTrue(strVacia.contains("id='L01'"));
		assertTrue(strVacia.contains("nombre='Linea Principal'"));
		assertTrue(strVacia.contains("recorrido=[]"));

		lineaSUT.agregarParadaAlRecorrido(p1);
		lineaSUT.agregarParadaAlRecorrido(p2);
		String strConParadas = lineaSUT.toString();
		// Verifica el formato del recorrido en el toString
		assertTrue(strConParadas.contains("recorrido=[P01 -> P02]"));
	}
}