package ar.edu.unpsjb.ayp2.proyectointegrador.modelo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

/**
 * Pruebas unitarias para la clase Colectivo.
 * Verifica el correcto funcionamiento de los métodos de la clase Colectivo,
 * incluyendo la gestión de pasajeros, movimiento entre paradas y el estado del colectivo.
 */
@DisplayName("Tests para la Clase Colectivo")
class ColectivoTest {

    private Linea lineaConRecorrido;
    private Linea lineaSinRecorrido;
    private Parada p1, p2, p3;
    private Pasajero pax1, pax2, pax3;
    private Colectivo colectivoSUT; // Subject Under Test

    @BeforeEach
    void setUp() {
        // Configurar Paradas
        p1 = new Parada("P01", "Parada Inicial");
        p2 = new Parada("P02", "Parada Intermedia");
        p3 = new Parada("P03", "Parada Final");

        // Configurar Línea con Recorrido
        lineaConRecorrido = new Linea("LCR", "Linea Con Recorrido");
        lineaConRecorrido.agregarParadaAlRecorrido(p1);
        lineaConRecorrido.agregarParadaAlRecorrido(p2);
        lineaConRecorrido.agregarParadaAlRecorrido(p3);

        // Configurar Línea sin Recorrido
        lineaSinRecorrido = new Linea("LSR", "Linea Sin Recorrido");

        // Configurar Pasajeros
        pax1 = new Pasajero("PAX1", p1, p2);
        pax2 = new Pasajero("PAX2", p1, p3);
        pax3 = new Pasajero("PAX3", p2, p3);

        // Colectivo SUT por defecto con capacidad para 2 pasajeros
        colectivoSUT = new Colectivo("C01", lineaConRecorrido, 2);
    }

    // --- Pruebas de Constructor ---
    @Test
    @DisplayName("Constructor: debería inicializar correctamente con datos válidos")
    void testConstructorValido() {
        assertEquals("C01", colectivoSUT.getIdColectivo());
        assertSame(lineaConRecorrido, colectivoSUT.getLineaAsignada());
        assertEquals(2, colectivoSUT.getCapacidadMaxima());
        assertTrue(colectivoSUT.getPasajerosABordo().isEmpty());
        assertEquals(p1, colectivoSUT.getParadaActual(), "Debería iniciar en la primera parada de la línea.");
        assertEquals(0, colectivoSUT.getIndiceParadaActualEnRecorrido());
    }

    @Test
    @DisplayName("Constructor: debería inicializar correctamente con una línea sin recorrido")
    void testConstructorLineaSinRecorrido() {
        Colectivo c = new Colectivo("C02", lineaSinRecorrido, 5);
        assertNull(c.getParadaActual(), "Parada actual debería ser null si la línea no tiene recorrido.");
        assertEquals(-1, c.getIndiceParadaActualEnRecorrido(), "Índice debería ser -1 si no hay recorrido.");
    }

    @Test
    @DisplayName("Constructor: debería lanzar excepción si el ID es nulo")
    void testConstructorIdNuloLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> new Colectivo(null, lineaConRecorrido, 5));
    }

    @Test
    @DisplayName("Constructor: debería lanzar excepción si el ID está vacío")
    void testConstructorIdVacioLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> new Colectivo("   ", lineaConRecorrido, 5));
    }

    @Test
    @DisplayName("Constructor: debería lanzar excepción si la línea es nula")
    void testConstructorLineaNulaLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> new Colectivo("C03", null, 5));
    }

    @Test
    @DisplayName("Constructor: debería lanzar excepción si la capacidad es negativa")
    void testConstructorCapacidadNegativaLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> new Colectivo("C04", lineaConRecorrido, -1));
    }

    // --- Pruebas de Gestión de Pasajeros ---
    @Test
    @DisplayName("Pasajeros: debería permitir subir pasajeros hasta llenar la capacidad")
    void testSubirPasajeroConCapacidad() {
        assertTrue(colectivoSUT.subirPasajero(pax1));
        assertEquals(1, colectivoSUT.getCantidadPasajerosABordo());
        assertEquals(1, colectivoSUT.getCapacidadDisponible());

        assertTrue(colectivoSUT.subirPasajero(pax2));
        assertEquals(2, colectivoSUT.getCantidadPasajerosABordo());
        assertEquals(0, colectivoSUT.getCapacidadDisponible());
    }

    @Test
    @DisplayName("Pasajeros: no debería permitir subir un pasajero nulo")
    void testSubirPasajeroNulo() {
        assertFalse(colectivoSUT.subirPasajero(null));
        assertEquals(0, colectivoSUT.getCantidadPasajerosABordo());
    }

    @Test
    @DisplayName("Pasajeros: no debería permitir subir si la capacidad está llena")
    void testSubirPasajeroSinCapacidad() {
        colectivoSUT.subirPasajero(pax1);
        colectivoSUT.subirPasajero(pax2); // Llena el colectivo
        
        assertFalse(colectivoSUT.subirPasajero(pax3), "No debería poder subir si está lleno.");
        assertEquals(2, colectivoSUT.getCantidadPasajerosABordo());
    }
    
    @Test
    @DisplayName("Pasajeros: no debería permitir subir un pasajero que ya está a bordo")
    void testSubirPasajeroYaABordo() {
        colectivoSUT.subirPasajero(pax1);
        int cantidadInicial = colectivoSUT.getCantidadPasajerosABordo();
        assertFalse(colectivoSUT.subirPasajero(pax1), "No debería subir un pasajero duplicado.");
        assertEquals(cantidadInicial, colectivoSUT.getCantidadPasajerosABordo(), "La cantidad no debería cambiar.");
    }

    @Test
    @DisplayName("Pasajeros: debería permitir bajar un pasajero existente")
    void testBajarPasajeroExistente() {
        colectivoSUT.subirPasajero(pax1);
        colectivoSUT.subirPasajero(pax2);

        assertTrue(colectivoSUT.bajarPasajero(pax1));
        assertEquals(1, colectivoSUT.getCantidadPasajerosABordo());
        assertFalse(colectivoSUT.getPasajerosABordo().contains(pax1));
    }

    @Test
    @DisplayName("Pasajeros: no debería permitir bajar un pasajero que no está a bordo")
    void testBajarPasajeroInexistente() {
        colectivoSUT.subirPasajero(pax1);
        assertFalse(colectivoSUT.bajarPasajero(pax2));
        assertEquals(1, colectivoSUT.getCantidadPasajerosABordo());
    }

    @Test
    @DisplayName("Pasajeros: no debería permitir bajar un pasajero nulo")
    void testBajarPasajeroNulo() {
        colectivoSUT.subirPasajero(pax1);
        assertFalse(colectivoSUT.bajarPasajero(null));
        assertEquals(1, colectivoSUT.getCantidadPasajerosABordo());
    }

    @Test
    @DisplayName("Encapsulamiento: getPasajerosABordo() debería devolver una copia")
    void testGetPasajerosABordoDevuelveCopia() {
        colectivoSUT.subirPasajero(pax1);
        List<Pasajero> pasajerosObtenidos = colectivoSUT.getPasajerosABordo();
        pasajerosObtenidos.add(pax2); // Modificar la copia
        
        assertEquals(1, colectivoSUT.getCantidadPasajerosABordo(), "La lista interna no debe cambiar si se modifica la copia.");
        assertNotSame(pasajerosObtenidos, colectivoSUT.getPasajerosABordo(), "Cada llamada debe devolver una nueva copia.");
    }


    // --- Pruebas de Movimiento y Estado ---
    @Test
    @DisplayName("Movimiento: debería avanzar correctamente a la siguiente parada")
    void testAvanzarAProximaParada() {
        // CORRECCIÓN: El método avanzarAProximaParada() ahora es void.
        assertEquals(p1, colectivoSUT.getParadaActual());

        colectivoSUT.avanzarAProximaParada();
        assertEquals(p2, colectivoSUT.getParadaActual());
        assertEquals(1, colectivoSUT.getIndiceParadaActualEnRecorrido());

        colectivoSUT.avanzarAProximaParada();
        assertEquals(p3, colectivoSUT.getParadaActual());
        assertEquals(2, colectivoSUT.getIndiceParadaActualEnRecorrido());
    }

    @Test
    @DisplayName("Movimiento: no debería avanzar si ya está en la parada terminal")
    void testAvanzarEstandoEnTerminal() {
        // CORRECCIÓN: Se adapta al método void.
        colectivoSUT.avanzarAProximaParada(); // a p2
        colectivoSUT.avanzarAProximaParada(); // a p3 (terminal)
        
        assertTrue(colectivoSUT.estaEnTerminal());
        assertEquals(p3, colectivoSUT.getParadaActual());
        
        colectivoSUT.avanzarAProximaParada(); // Intenta avanzar de nuevo
        assertEquals(p3, colectivoSUT.getParadaActual(), "No debería moverse de la parada terminal.");
        assertEquals(2, colectivoSUT.getIndiceParadaActualEnRecorrido(), "El índice no debería cambiar.");
    }
    
    @Test
    @DisplayName("Movimiento: no debería avanzar si la línea no tiene recorrido")
    void testAvanzarSinRecorrido() {
        // CORRECCIÓN: Se adapta al método void.
        Colectivo cSinRecorrido = new Colectivo("CSR", lineaSinRecorrido, 5);
        assertTrue(cSinRecorrido.estaEnTerminal());
        
        cSinRecorrido.avanzarAProximaParada();
        assertNull(cSinRecorrido.getParadaActual(), "No debería haber avanzado a ninguna parada.");
    }

    @Test
    @DisplayName("Estado: estaEnTerminal() debería devolver el estado correcto")
    void testEstaEnTerminal() {
        assertFalse(colectivoSUT.estaEnTerminal(), "No debería estar en terminal al inicio.");
        
        colectivoSUT.avanzarAProximaParada(); // a p2
        assertFalse(colectivoSUT.estaEnTerminal());

        colectivoSUT.avanzarAProximaParada(); // a p3 (terminal)
        assertTrue(colectivoSUT.estaEnTerminal());
    }
    
    @Test
    @DisplayName("Estado: debería considerarse en terminal si la línea no tiene recorrido")
    void testEstaEnTerminalLineaSinRecorrido() {
        Colectivo c = new Colectivo("C05", lineaSinRecorrido, 5);
        assertTrue(c.estaEnTerminal());
    }

    // --- Pruebas de equals, hashCode y toString ---
    @Test
    @DisplayName("Equals/HashCode: deberían basarse únicamente en el ID del colectivo")
    void testEqualsYHashCode() {
        Colectivo c1 = new Colectivo("ID-COLECTIVO", lineaConRecorrido, 5);
        Colectivo c2 = new Colectivo("ID-COLECTIVO", lineaSinRecorrido, 10);
        Colectivo c3 = new Colectivo("ID-OTRO", lineaConRecorrido, 5);

        assertEquals(c1, c2);
        assertEquals(c1.hashCode(), c2.hashCode());
        assertNotEquals(c1, c3);
    }

    @Test
    @DisplayName("toString: debería generar una representación en texto correcta")
    void testToString() {
        String str = colectivoSUT.toString();
        assertNotNull(str);
        assertTrue(str.contains(colectivoSUT.getIdColectivo()));
        assertTrue(str.contains("capacidadMaxima=2"));
        assertTrue(str.contains(lineaConRecorrido.getId()));
        assertTrue(str.contains(p1.getId()));
    }
}