package ar.edu.unpsjb.ayp2.proyectointegrador.modelo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

/**
 * Pruebas unitarias para la clase Linea.
 * Verifica la correcta creación, gestión del recorrido, métodos de consulta y
 * el contrato de la clase.
 */
@DisplayName("Pruebas de la Clase Linea")
class LineaTest {

    private Linea lineaSUT; // System Under Test
    private Parada p1, p2, p3;

    @BeforeEach
    void setUp() {
        lineaSUT = new Linea("L01", "Linea Principal");
        p1 = new Parada("P01", "Parada Uno");
        p2 = new Parada("P02", "Parada Dos");
        p3 = new Parada("P03", "Parada Tres");
    }

    @Nested
    @DisplayName("Pruebas de Constructor")
    class PruebasDeConstructor {
        
        @Test
        @DisplayName("Debería crear una línea con datos válidos y recorrido vacío")
        void crearLineaConDatosValidos() {
            assertEquals("L01", lineaSUT.getId());
            assertEquals("Linea Principal", lineaSUT.getNombre());
            // CORRECCIÓN: Usamos getRecorrido().size()
            assertEquals(0, lineaSUT.getRecorrido().size(), "El recorrido de una nueva línea debería tener tamaño 0.");
        }

        @Test
        @DisplayName("Debería lanzar excepción si el ID es nulo o vacío")
        void crearLineaConIdInvalidoLanzaExcepcion() {
            assertThrows(IllegalArgumentException.class, () -> new Linea(null, "Nombre Valido"));
            assertThrows(IllegalArgumentException.class, () -> new Linea("  ", "Nombre Valido"));
        }

        @Test
        @DisplayName("Debería lanzar excepción si el nombre es nulo o vacío")
        void crearLineaConNombreInvalidoLanzaExcepcion() {
            assertThrows(IllegalArgumentException.class, () -> new Linea("IDValido", null));
            assertThrows(IllegalArgumentException.class, () -> new Linea("IDValido", "  "));
        }
    }

    @Nested
    @DisplayName("Pruebas de Gestión del Recorrido")
    class PruebasDeRecorrido {

        @Test
        @DisplayName("agregarParadaAlRecorrido() debería añadir paradas y actualizar extremos")
        void agregarParadaAlRecorrido() {
            lineaSUT.agregarParadaAlRecorrido(p1);
            // CORRECCIÓN: Usamos getRecorrido().size()
            assertEquals(1, lineaSUT.getRecorrido().size());
            assertEquals(p1, lineaSUT.getPrimeraParada());
            assertEquals(p1, lineaSUT.getUltimaParada());

            lineaSUT.agregarParadaAlRecorrido(p2);
            assertEquals(2, lineaSUT.getRecorrido().size());
            assertEquals(p1, lineaSUT.getPrimeraParada());
            assertEquals(p2, lineaSUT.getUltimaParada());
        }

        @Test
        @DisplayName("agregarParadaAlRecorrido() nula debería lanzar excepción")
        void agregarParadaNulaLanzaExcepcion() {
            assertThrows(IllegalArgumentException.class, () -> lineaSUT.agregarParadaAlRecorrido(null));
        }

        @Test
        @DisplayName("agregarParadaAlRecorrido() no debería añadir duplicados consecutivos")
        void agregarMismaParadaConsecutiva() {
            lineaSUT.agregarParadaAlRecorrido(p1);
            // CORRECCIÓN: Usamos getRecorrido().size()
            int longitudOriginal = lineaSUT.getRecorrido().size();
            
            lineaSUT.agregarParadaAlRecorrido(p1); // Intento de agregar duplicado
            
            assertEquals(longitudOriginal, lineaSUT.getRecorrido().size(), "Agregar la misma parada consecutiva no debería aumentar el tamaño.");
        }

        @Test
        @DisplayName("getRecorrido() debería devolver una copia defensiva de la lista")
        void getRecorridoDevuelveCopiaDefensiva() {
            lineaSUT.agregarParadaAlRecorrido(p1);
            List<Parada> recorridoObtenido = lineaSUT.getRecorrido();

            // La copia no debe ser la misma instancia que la lista interna.
            assertNotSame(lineaSUT.getRecorrido(), lineaSUT.getRecorrido(), "Cada llamada a getRecorrido debe devolver una nueva lista.");
            
            // Modificar la copia no debe afectar a la lista original.
            recorridoObtenido.add(p3); 
            // CORRECCIÓN: Usamos getRecorrido().size()
            assertEquals(1, lineaSUT.getRecorrido().size(), "La lista original no debe cambiar si se modifica la copia.");
        }
    }

    @Nested
    @DisplayName("Pruebas de Búsqueda y Consulta")
    class PruebasDeConsulta {
        @BeforeEach
        void popularRecorrido(){
            lineaSUT.agregarParadaAlRecorrido(p1);
            lineaSUT.agregarParadaAlRecorrido(p2);
        }

        @Test
        @DisplayName("getParadaPorIndice() debería funcionar para índices válidos y lanzar excepción para inválidos")
        void getParadaPorIndice() {
            assertEquals(p1, lineaSUT.getParadaPorIndice(0));
            assertEquals(p2, lineaSUT.getParadaPorIndice(1));
            assertThrows(IndexOutOfBoundsException.class, () -> lineaSUT.getParadaPorIndice(-1));
            assertThrows(IndexOutOfBoundsException.class, () -> lineaSUT.getParadaPorIndice(2));
        }
        
        @Test
        @DisplayName("getIndiceParada() debería devolver el índice correcto o -1 si no la encuentra")
        void getIndiceParada() {
            assertEquals(0, lineaSUT.getIndiceParada(p1));
            assertEquals(1, lineaSUT.getIndiceParada(p2));
            assertEquals(-1, lineaSUT.getIndiceParada(p3));
            assertEquals(-1, lineaSUT.getIndiceParada(null));
        }

        @Test
        @DisplayName("tieneParadaEnRecorrido() debería devolver true si la contiene, false si no")
        void tieneParadaEnRecorrido() {
            assertTrue(lineaSUT.tieneParadaEnRecorrido(p1));
            assertFalse(lineaSUT.tieneParadaEnRecorrido(p3));
            assertFalse(lineaSUT.tieneParadaEnRecorrido(null));
        }

        @Test
        @DisplayName("esTerminal() debería identificar correctamente la última parada")
        void esTerminal() {
            assertTrue(lineaSUT.esTerminal(p2), "p2 debería ser la terminal.");
            assertFalse(lineaSUT.esTerminal(p1), "p1 no debería ser la terminal.");
            assertFalse(lineaSUT.esTerminal(p3), "Una parada que no está en la línea no puede ser terminal.");
            assertFalse(lineaSUT.esTerminal(null), "Una parada nula no puede ser terminal.");
        }
    }
	
    @Nested
    @DisplayName("Pruebas de Contrato (equals, hashCode, toString)")
    class PruebasDeContrato {
        
        @Test
        @DisplayName("equals y hashCode deberían basarse únicamente en el ID")
        void equalsYHashCode() {
            Linea lineaIgual = new Linea("L01", "Otro Nombre");
            Linea lineaDiferente = new Linea("L99", "Linea Principal");

            assertEquals(lineaSUT, lineaIgual, "Líneas con el mismo ID deben ser iguales.");
            assertEquals(lineaSUT.hashCode(), lineaIgual.hashCode(), "HashCodes de líneas iguales deben ser iguales.");
            assertNotEquals(lineaSUT, lineaDiferente, "Líneas con ID diferente no deben ser iguales.");
        }
        
        @Test
        @DisplayName("toString debería generar una representación correcta del recorrido")
        void testToString() {
            lineaSUT.agregarParadaAlRecorrido(p1);
            lineaSUT.agregarParadaAlRecorrido(p2);
            String str = lineaSUT.toString();

            assertTrue(str.contains("id='L01'"));
            assertTrue(str.contains("nombre='Linea Principal'"));
            assertTrue(str.contains("recorrido=[P01 -> P02]"));
        }

        @Test
        @DisplayName("getReporteRecorrido() debería generar un reporte formateado correctamente")
        void getReporteRecorrido() {
            lineaSUT.agregarParadaAlRecorrido(p1);
            lineaSUT.agregarParadaAlRecorrido(p2);
            String reporte = lineaSUT.getReporteRecorrido();

            assertTrue(reporte.contains("--- Detalle de Línea: Linea Principal (ID: L01) ---"));
            assertTrue(reporte.contains("1. Parada P01 - Parada Uno"));
            assertTrue(reporte.contains("2. Parada P02 - Parada Dos"));
        }
    }
}