package ar.edu.unpsjb.ayp2.proyectointegrador.modelo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ParadaTest {

    private Parada paradaSUT;
    private Pasajero pasajero1;
    private Pasajero pasajero2;

    @BeforeEach
    void setUp() {
        // Constructor alternativo para simplicidad en los tests.
        paradaSUT = new Parada("P001", "Terminal");
        pasajero1 = new Pasajero("PAX01", paradaSUT, new Parada("P002", "Destino Lejano"));
        pasajero2 = new Pasajero("PAX02", paradaSUT, new Parada("P003", "Otro Destino"));
    }

    @Test
    @DisplayName("Constructor: crea una parada con datos válidos y estado inicial correcto")
    void crearParadaConDatosValidos() {
        assertEquals("P001", paradaSUT.getId());
        assertEquals("Terminal", paradaSUT.getDireccion());
        assertFalse(paradaSUT.hayPasajerosEsperando(), "Una nueva parada no debería tener pasajeros esperando.");
        assertEquals(0, paradaSUT.cantidadPasajerosEsperando());
    }

    @Test
    @DisplayName("Constructor: lanza excepción si el ID es nulo")
    void crearParadaConIdNuloLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> new Parada(null, "Nombre Valido"));
    }

    @Test
    @DisplayName("Constructor: lanza excepción si la dirección está vacía")
    void crearParadaConNombreVacioLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> new Parada("IDValido", "  "));
    }

    @Test
    @DisplayName("Cola: agregar un pasajero actualiza el estado correctamente")
    void agregarPasajeroACola() {
        paradaSUT.agregarPasajero(pasajero1);
        assertTrue(paradaSUT.hayPasajerosEsperando());
        assertEquals(1, paradaSUT.cantidadPasajerosEsperando());
        // Verificamos usando el método encapsulado en lugar de acceder a la cola directamente.
        assertTrue(paradaSUT.tienePasajeroEnCola(pasajero1), "La parada debería contener el pasajero recién agregado.");
    }

    @Test
    @DisplayName("Cola: agregar un pasajero nulo lanza una excepción")
    void agregarPasajeroNuloLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> paradaSUT.agregarPasajero(null));
    }

    @Test
    @DisplayName("Cola: remover pasajeros sigue el orden FIFO (First-In, First-Out)")
    void removerPasajeroDeCola() {
        paradaSUT.agregarPasajero(pasajero1);
        paradaSUT.agregarPasajero(pasajero2);
        assertEquals(2, paradaSUT.cantidadPasajerosEsperando());

        Pasajero removido1 = paradaSUT.removerSiguientePasajero();
        assertEquals(pasajero1, removido1, "El primer pasajero en entrar debe ser el primero en salir (FIFO).");
        assertEquals(1, paradaSUT.cantidadPasajerosEsperando());

        Pasajero removido2 = paradaSUT.removerSiguientePasajero();
        assertEquals(pasajero2, removido2);
        assertFalse(paradaSUT.hayPasajerosEsperando());
    }

    @Test
    @DisplayName("Cola: remover un pasajero de una cola vacía devuelve null")
    void removerPasajeroDeColaVaciaDevuelveNull() {
        assertFalse(paradaSUT.hayPasajerosEsperando());
        assertNull(paradaSUT.removerSiguientePasajero(), "Remover de cola vacía debería devolver null.");
    }
    
    @Test
    @DisplayName("Equals/HashCode: paradas con el mismo ID deben ser consideradas iguales")
    void paradasConMismoIdSonIguales() {
        Parada parada1 = new Parada("ID_TEST", "Parada Alpha");
        Parada parada2 = new Parada("ID_TEST", "Parada Bravo"); // Misma ID, diferente dirección
        assertEquals(parada1, parada2, "Dos paradas con el mismo ID deben ser iguales.");
        assertEquals(parada1.hashCode(), parada2.hashCode(), "El hashCode debe ser consistente con equals.");
    }

    @Test
    @DisplayName("Equals/HashCode: paradas con diferente ID no deben ser iguales")
    void paradasConDiferenteIdNoSonIguales() {
        Parada parada1 = new Parada("ID_UNO", "Parada Charlie");
        Parada parada2 = new Parada("ID_DOS", "Parada Charlie"); // Misma dirección, diferente ID
        assertNotEquals(parada1, parada2);
    }

    @Test
    @DisplayName("toString: debe mostrar la información correcta de la parada")
    void toStringMuestraInfoCorrecta() {
        String str = paradaSUT.toString();
        assertTrue(str.contains("id='P001'"));
        assertTrue(str.contains("direccion='Terminal'"));
        assertTrue(str.contains("pasajerosEsperando=0"));

        paradaSUT.agregarPasajero(pasajero1);
        str = paradaSUT.toString();
        assertTrue(str.contains("pasajerosEsperando=1"));
    }
}