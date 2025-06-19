package ar.edu.unpsjb.ayp2.proyectointegrador.modelo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List; // Importar List de java.util

class LineaColectivoTest {

    private LineaColectivo lineaSUT;
    private Parada p1, p2, p3;

    @BeforeEach
    void setUp() {
        lineaSUT = new LineaColectivo("L01", "Linea Principal");
        p1 = new Parada("P01", "Parada Uno");
        p2 = new Parada("P02", "Parada Dos");
        p3 = new Parada("P03", "Parada Tres");
    }

    @Test
    void crearLineaConDatosValidos() {
        assertEquals("L01", lineaSUT.getId());
        assertEquals("Linea Principal", lineaSUT.getNombre());
        assertTrue(lineaSUT.getRecorrido().isEmpty(), "El recorrido de una nueva línea debería estar vacío.");
        assertEquals(0, lineaSUT.longitudRecorrido());
    }

    @Test
    void crearLineaConIdNuloLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> new LineaColectivo(null, "Nombre"));
    }

    @Test
    void crearLineaConNombreVacioLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> new LineaColectivo("ID", "  "));
    }

    @Test
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
    void agregarParadaNulaAlRecorridoLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> lineaSUT.agregarParadaAlRecorrido(null));
    }
    
    @Test
    void agregarMismaParadaConsecutivaNoAfectaOMuestraAdvertencia() {
        lineaSUT.agregarParadaAlRecorrido(p1);
        int longitudOriginal = lineaSUT.longitudRecorrido();
        lineaSUT.agregarParadaAlRecorrido(p1); 
        assertEquals(longitudOriginal, lineaSUT.longitudRecorrido(), "Agregar la misma parada consecutiva no debería aumentar la longitud.");
    }


    @Test
    void getRecorridoDevuelveCopia() {
        lineaSUT.agregarParadaAlRecorrido(p1);
        List<Parada> recorridoObtenido = lineaSUT.getRecorrido(); // Esto ahora devuelve un ArrayList
        assertEquals(1, recorridoObtenido.size());
        assertEquals(p1, recorridoObtenido.get(0)); // Usar get(0) para ArrayList

        // No podemos hacer assertNotSame directamente con la lista interna porque no la exponemos.
        // Pero podemos verificar que modificar la copia no afecta al original y viceversa.
        // Y que obtener el recorrido de nuevo no devuelve la misma instancia de la copia anterior.

        List<Parada> recorridoObtenido2 = lineaSUT.getRecorrido();
        assertNotSame(recorridoObtenido, recorridoObtenido2, "Cada llamada a getRecorrido debe devolver una nueva copia.");


        // Verificar que la copia no se afecta por cambios en la original
        lineaSUT.agregarParadaAlRecorrido(p2); // Modifica la línea original
        assertEquals(1, recorridoObtenido.size(), "La copia del recorrido (recorridoObtenido) no debería verse afectada por cambios posteriores en la línea original.");
        
        // Verificar que modificar la copia no afecta al original (más difícil de probar sin exponer la interna)
        // Si la copia es un ArrayList, podemos intentar añadirle algo y ver que la original no cambia.
        if (recorridoObtenido instanceof java.util.ArrayList) {
            try {
                recorridoObtenido.add(new Parada("PX", "Test")); // Esto fallaría si fuera inmodificable
            } catch (UnsupportedOperationException e) {
                // Si la copia es inmodificable (e.g., List.copyOf), esta prueba sería diferente.
                // Pero como hacemos new ArrayList<>(this.recorrido), es modificable.
            }
        }
        assertEquals(2, lineaSUT.longitudRecorrido(), "La línea original no debe ser afectada por modificaciones a su copia.");
    }


    @Test
    void getParadaPorIndiceValido() {
        lineaSUT.agregarParadaAlRecorrido(p1);
        lineaSUT.agregarParadaAlRecorrido(p2);
        lineaSUT.agregarParadaAlRecorrido(p3);

        assertEquals(p1, lineaSUT.getParadaPorIndice(0));
        assertEquals(p2, lineaSUT.getParadaPorIndice(1));
        assertEquals(p3, lineaSUT.getParadaPorIndice(2));
    }

    @Test
    void getParadaPorIndiceNegativoLanzaExcepcion() {
        lineaSUT.agregarParadaAlRecorrido(p1);
        assertThrows(IndexOutOfBoundsException.class, () -> lineaSUT.getParadaPorIndice(-1));
    }

    @Test
    void getParadaPorIndiceFueraDeRangoLanzaExcepcion() {
        lineaSUT.agregarParadaAlRecorrido(p1);
        assertThrows(IndexOutOfBoundsException.class, () -> lineaSUT.getParadaPorIndice(1)); 
        assertEquals(p1, lineaSUT.getParadaPorIndice(0)); 
        lineaSUT.agregarParadaAlRecorrido(p2);
        assertThrows(IndexOutOfBoundsException.class, () -> lineaSUT.getParadaPorIndice(2)); 
    }
    
    @Test
    void getParadaPorIndiceEnRecorridoVacioLanzaExcepcion() {
        assertTrue(lineaSUT.getRecorrido().isEmpty());
        assertThrows(IndexOutOfBoundsException.class, () -> lineaSUT.getParadaPorIndice(0));
    }

    @Test
    void getPrimeraYUltimaParada() {
        assertNull(lineaSUT.getPrimeraParada(), "Primera parada de línea vacía debería ser null.");
        assertNull(lineaSUT.getUltimaParada(), "Última parada de línea vacía debería ser null.");

        lineaSUT.agregarParadaAlRecorrido(p1);
        assertEquals(p1, lineaSUT.getPrimeraParada());
        assertEquals(p1, lineaSUT.getUltimaParada());

        lineaSUT.agregarParadaAlRecorrido(p2);
        assertEquals(p1, lineaSUT.getPrimeraParada()); 
        assertEquals(p2, lineaSUT.getUltimaParada()); 

        lineaSUT.agregarParadaAlRecorrido(p3);
        assertEquals(p1, lineaSUT.getPrimeraParada());
        assertEquals(p3, lineaSUT.getUltimaParada());
    }

    @Test
    void lineasConMismoIdSonIguales() {
        LineaColectivo linea1 = new LineaColectivo("L99", "Linea Alfa");
        LineaColectivo linea2 = new LineaColectivo("L99", "Linea Beta");
        assertEquals(linea1, linea2);
        assertEquals(linea1.hashCode(), linea2.hashCode());
    }

    @Test
    void lineasConDiferenteIdNoSonIguales() {
        LineaColectivo linea1 = new LineaColectivo("L100", "Linea Gamma");
        LineaColectivo linea2 = new LineaColectivo("L101", "Linea Gamma");
        assertNotEquals(linea1, linea2);
    }

    @Test
    void toStringMuestraInfoCorrecta() {
        String strVacia = lineaSUT.toString();
        assertTrue(strVacia.contains("id='L01'"));
        assertTrue(strVacia.contains("nombre='Linea Principal'"));
        assertTrue(strVacia.contains("recorrido=[]"));

        lineaSUT.agregarParadaAlRecorrido(p1);
        lineaSUT.agregarParadaAlRecorrido(p2);
        String strConParadas = lineaSUT.toString();
        assertTrue(strConParadas.contains("recorrido=[P01 -> P02]"));
    }

    @Test
    void testContieneParada() {
        assertFalse(lineaSUT.contieneParada(p1), "Recorrido vacío no contiene p1.");
        assertFalse(lineaSUT.contieneParada(null), "Recorrido vacío no contiene null.");

        lineaSUT.agregarParadaAlRecorrido(p1);
        assertTrue(lineaSUT.contieneParada(p1), "Recorrido debería contener p1.");
        assertFalse(lineaSUT.contieneParada(p2), "Recorrido no debería contener p2 todavía.");
        assertFalse(lineaSUT.contieneParada(null), "Recorrido no contiene null.");

        lineaSUT.agregarParadaAlRecorrido(p2);
        assertTrue(lineaSUT.contieneParada(p2), "Recorrido debería contener p2.");
    }

    @Test
    void testGetIndiceParada() {
        assertEquals(-1, lineaSUT.getIndiceParada(p1), "Índice de p1 en recorrido vacío debería ser -1.");
        assertEquals(-1, lineaSUT.getIndiceParada(null), "Índice de null en recorrido vacío debería ser -1.");

        lineaSUT.agregarParadaAlRecorrido(p1); 
        lineaSUT.agregarParadaAlRecorrido(p2); 
        lineaSUT.agregarParadaAlRecorrido(p3); 

        assertEquals(0, lineaSUT.getIndiceParada(p1), "Índice de p1 debería ser 0.");
        assertEquals(1, lineaSUT.getIndiceParada(p2), "Índice de p2 debería ser 1.");
        assertEquals(2, lineaSUT.getIndiceParada(p3), "Índice de p3 debería ser 2.");

        Parada paradaNoEnRecorrido = new Parada("P99", "Parada Fantasma");
        assertEquals(-1, lineaSUT.getIndiceParada(paradaNoEnRecorrido), "Índice de parada no existente debería ser -1.");
        assertEquals(-1, lineaSUT.getIndiceParada(null), "Índice de null debería ser -1.");
    }

    @Test
    void testGetSiguienteParada() {
        assertNull(lineaSUT.getSiguienteParada(p1), "Siguiente a p1 en recorrido vacío debería ser null.");
        assertNull(lineaSUT.getSiguienteParada(null), "Siguiente a null en recorrido vacío debería ser null.");

        lineaSUT.agregarParadaAlRecorrido(p1);
        assertNull(lineaSUT.getSiguienteParada(p1), "P1 es la única parada, su siguiente es null (es terminal).");

        lineaSUT.agregarParadaAlRecorrido(p2); 
        assertEquals(p2, lineaSUT.getSiguienteParada(p1), "Siguiente a P1 debería ser P2.");
        assertNull(lineaSUT.getSiguienteParada(p2), "P2 es la terminal, su siguiente es null.");

        lineaSUT.agregarParadaAlRecorrido(p3); 
        assertEquals(p2, lineaSUT.getSiguienteParada(p1), "Siguiente a P1 es P2.");
        assertEquals(p3, lineaSUT.getSiguienteParada(p2), "Siguiente a P2 es P3.");
        assertNull(lineaSUT.getSiguienteParada(p3), "P3 es la terminal, su siguiente es null.");
        
        Parada paradaNoEnRecorrido = new Parada("P99", "Parada Fantasma");
        assertNull(lineaSUT.getSiguienteParada(paradaNoEnRecorrido), "Siguiente a parada no existente debería ser null.");
        assertNull(lineaSUT.getSiguienteParada(null), "Siguiente a null debería ser null.");
    }

    @Test
    void testEsTerminal() {
        assertTrue(lineaSUT.getRecorrido().isEmpty(), "Precondición: El recorrido debe estar vacío para esta prueba.");
        assertFalse(lineaSUT.esTerminal(p1), "En recorrido vacío, ninguna parada es terminal.");
        assertFalse(lineaSUT.esTerminal(null), "En recorrido vacío, null no es terminal.");

        lineaSUT.agregarParadaAlRecorrido(p1);
        assertTrue(lineaSUT.esTerminal(p1), "Con una sola parada, esa parada es terminal.");
        assertFalse(lineaSUT.esTerminal(p2), "Una parada que no está (o es diferente) no es terminal.");
        assertFalse(lineaSUT.esTerminal(null), "Con una parada, null no es terminal.");

        lineaSUT.agregarParadaAlRecorrido(p2); 
        lineaSUT.agregarParadaAlRecorrido(p3); 
        
        assertTrue(lineaSUT.esTerminal(p3), "P3 debería ser la terminal.");
        assertFalse(lineaSUT.esTerminal(p1), "P1 no es la terminal.");
        assertFalse(lineaSUT.esTerminal(p2), "P2 no es la terminal.");
        
        Parada paradaNoEnRecorrido = new Parada("P99", "Parada Fantasma");
        assertFalse(lineaSUT.esTerminal(paradaNoEnRecorrido), "Una parada no presente en el recorrido no es terminal.");
        assertFalse(lineaSUT.esTerminal(null), "Con múltiples paradas, null no es terminal.");
    }
}