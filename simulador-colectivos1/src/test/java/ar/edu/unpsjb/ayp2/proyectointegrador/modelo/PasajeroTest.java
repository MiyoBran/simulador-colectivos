package ar.edu.unpsjb.ayp2.proyectointegrador.modelo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la clase Pasajero.
 * Verifica la correcta creación, el manejo de estado y los métodos de la clase.
 */
@DisplayName("Tests para la Clase Pasajero")
class PasajeroTest {

    private Parada p1, p2, p3;

    @BeforeEach
    void setUp() {
        p1 = new Parada("P01", "Parada Origen");
        p2 = new Parada("P02", "Parada Destino");
        p3 = new Parada("P03", "Otra Parada");
    }

    @Test
    @DisplayName("Constructor: debería crear un pasajero con ID autogenerado y estado inicial correcto")
    void crearPasajeroConIdAutogeneradoYDatosValidos() {
        Pasajero pasajero = new Pasajero(p1, p2);
        assertNotNull(pasajero.getId(), "El ID autogenerado no debería ser nulo.");
        assertFalse(pasajero.getId().trim().isEmpty(), "El ID autogenerado no debería estar vacío.");
        assertEquals(p1, pasajero.getParadaOrigen());
        assertEquals(p2, pasajero.getParadaDestino());
        // Verificar valores iniciales por defecto
        assertEquals(0, pasajero.getColectivosEsperados());
        assertFalse(pasajero.isPudoSubir());
    }

    @Test
    @DisplayName("Constructor: debería crear un pasajero con un ID específico")
    void crearPasajeroConIdEspecificoYDatosValidos() {
        Pasajero pasajero = new Pasajero("PASS001", p1, p2);
        assertEquals("PASS001", pasajero.getId());
        assertEquals(p1, pasajero.getParadaOrigen());
        assertEquals(p2, pasajero.getParadaDestino());
    }

    @Test
    @DisplayName("Constructor: debería lanzar excepción si el ID es nulo")
    void crearPasajeroConIdNuloLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> new Pasajero(null, p1, p2));
    }

    @Test
    @DisplayName("Constructor: debería lanzar excepción si la parada de origen es nula")
    void crearPasajeroConOrigenNuloLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> new Pasajero("PASS002", null, p2));
    }

    @Test
    @DisplayName("Constructor: debería lanzar excepción si la parada de destino es nula")
    void crearPasajeroConDestinoNuloLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> new Pasajero("PASS003", p1, null));
    }

    @Test
    @DisplayName("Constructor: debería lanzar excepción si origen y destino son iguales")
    void crearPasajeroConOrigenIgualDestinoLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> new Pasajero("PASS004", p1, p1));
    }

    @Test
    @DisplayName("Estado: debería gestionar correctamente los atributos de estado del viaje")
    void manejoDeEstadoParaIncremento2() {
        Pasajero pasajero = new Pasajero(p1, p2);
        
        pasajero.incrementarColectivosEsperados();
        assertEquals(1, pasajero.getColectivosEsperados());
        
        pasajero.setPudoSubir(true);
        assertTrue(pasajero.isPudoSubir());

        pasajero.setViajoSentado(true);
        assertTrue(pasajero.isViajoSentado());

        // Probamos el reinicio del estado
        pasajero.resetearEstadoViaje();
        assertEquals(0, pasajero.getColectivosEsperados());
        assertFalse(pasajero.isPudoSubir());
        assertFalse(pasajero.isViajoSentado());
    }

    @Test
    @DisplayName("Estado: debería gestionar correctamente atributos avanzados de simulación")
    void manejoDeAtributosAvanzados() {
        Pasajero pasajero = new Pasajero(p1, p2);
        // Métodos avanzados eliminados del modelo actual:
        // pasajero.agregarTiempoEspera(5);
        // assertEquals(5, pasajero.getTiempoEspera());
        // pasajero.agregarTiempoEspera(3);
        // assertEquals(8, pasajero.getTiempoEspera());
        // pasajero.agregarColectivoObservado("C01");
        // pasajero.agregarColectivoObservado("C02");
        // assertEquals(2, pasajero.getColectivosObservados().size());
        // assertTrue(pasajero.getColectivosObservados().contains("C01"));
        // pasajero.agregarTiempoViaje(7);
        // assertEquals(7, pasajero.getTiempoViaje());
        // pasajero.agregarTiempoViaje(2);
        // assertEquals(9, pasajero.getTiempoViaje());
        // Bajada forzosa
        assertFalse(pasajero.isBajadaForzosa());
        pasajero.setBajadaForzosa(true);
        assertTrue(pasajero.isBajadaForzosa());
    }

    @Test
    @DisplayName("Satisfacción: debería calcular correctamente el índice de satisfacción")
    void calcularSatisfaccion() {
        Pasajero pasajero = new Pasajero(p1, p2);
        // Caso ideal
        pasajero.setPudoSubir(true);
        pasajero.setViajoSentado(true);
        // pasajero.setSubioAlPrimerColectivoQuePaso(true); // Método no disponible
        assertEquals(5, pasajero.calcularSatisfaccion());
        // Caso con espera y viaje largo
        pasajero.resetearEstadoViaje();
        pasajero.setPudoSubir(true);
        pasajero.setViajoSentado(false);
        // pasajero.setSubioAlPrimerColectivoQuePaso(false); // Método no disponible
        pasajero.incrementarColectivosEsperados();
        assertEquals(3, pasajero.calcularSatisfaccion());
        // Caso no pudo subir
        pasajero.resetearEstadoViaje();
        assertEquals(1, pasajero.calcularSatisfaccion());
    }

    @Test
    @DisplayName("Equals/HashCode: deberían basarse únicamente en el ID del pasajero")
    void pasajerosConMismoIdSonIguales() {
        Pasajero pas1 = new Pasajero("ID_IGUAL", p1, p2);
        Pasajero pas2 = new Pasajero("ID_IGUAL", p1, p3); // Mismo ID, diferente destino
        
        assertEquals(pas1, pas2, "Pasajeros con el mismo ID deben ser iguales.");
        assertEquals(pas1.hashCode(), pas2.hashCode(), "El hashCode debe ser consistente con equals.");
    }

    @Test
    @DisplayName("Equals/HashCode: no deberían ser iguales si los IDs son diferentes")
    void pasajerosConDiferenteIdNoSonIguales() {
        Pasajero pas1 = new Pasajero("ID_UNO", p1, p2);
        Pasajero pas2 = new Pasajero("ID_DOS", p1, p2);
        assertNotEquals(pas1, pas2);
    }


    @Test
    @DisplayName("toString: debería contener la información relevante del pasajero")
    void toStringContieneInfoRelevante() {
        // Arrange
        Pasajero pasajero = new Pasajero("PASS_TEST", p1, p2);
        
        // Act
        String str = pasajero.toString();
        
        // Assert
        
        assertTrue(str.contains("'PASS_TEST'"), "El toString() debe contener el ID del pasajero entre comillas simples.");
        assertTrue(str.contains("Origen: " + p1.getId()), "El toString() debe contener la información del origen.");
        assertTrue(str.contains("Destino: " + p2.getId()), "El toString() debe contener la información del destino.");
    }
}