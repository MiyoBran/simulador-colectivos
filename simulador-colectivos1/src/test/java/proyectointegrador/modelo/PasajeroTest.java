package proyectointegrador.modelo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la clase Pasajero.
 * Verifica la correcta creación, el manejo de estado, la lógica de negocio
 * y los métodos de la clase.
 */
@DisplayName("Pruebas de la Clase Pasajero")
class PasajeroTest {

    private Parada p1, p2;
    private Pasajero pasajero;

    // Se ejecuta antes de CADA test.
    @BeforeEach
    void setUp() {
        p1 = new Parada("P01", "Parada Origen");
        p2 = new Parada("P02", "Parada Destino");
        pasajero = new Pasajero(p1, p2); // Usamos un pasajero común para varios tests
    }

    @Nested
    @DisplayName("Pruebas de Constructores y Creación")
    class PruebasDeConstructor {

        @Test
        @DisplayName("Debería crear pasajero con ID autogenerado y estado inicial correcto")
        void crearPasajeroConIdAutogenerado() {
            assertNotNull(pasajero.getId(), "El ID autogenerado no debería ser nulo.");
            assertFalse(pasajero.getId().trim().isEmpty(), "El ID autogenerado no debería estar vacío.");
            assertEquals(p1, pasajero.getParadaOrigen(), "La parada de origen debe ser la correcta.");
            assertEquals(p2, pasajero.getParadaDestino(), "La parada de destino debe ser la correcta.");
            
            // Verificar estado inicial por defecto
            assertEquals(0, pasajero.getColectivosEsperados());
            assertFalse(pasajero.isPudoSubir());
            assertFalse(pasajero.isViajoSentado());
            assertFalse(pasajero.isBajadaForzosa());
            assertEquals(0, pasajero.getSatisfaccion());
        }

        @Test
        @DisplayName("Debería crear pasajero con un ID específico")
        void crearPasajeroConIdEspecifico() {
            Pasajero pasajeroConId = new Pasajero("PASS001", p1, p2);
            assertEquals("PASS001", pasajeroConId.getId());
            assertEquals(p1, pasajeroConId.getParadaOrigen());
            assertEquals(p2, pasajeroConId.getParadaDestino());
        }

        @Test
        @DisplayName("Debería lanzar excepción si ID es nulo")
        void crearPasajeroConIdNuloLanzaExcepcion() {
            assertThrows(IllegalArgumentException.class, () -> new Pasajero(null, p1, p2), "ID nulo debe lanzar excepción.");
        }

        @Test
        @DisplayName("Debería lanzar excepción si origen es nulo")
        void crearPasajeroConOrigenNuloLanzaExcepcion() {
            assertThrows(IllegalArgumentException.class, () -> new Pasajero("ID", null, p2), "Origen nulo debe lanzar excepción.");
        }

        @Test
        @DisplayName("Debería lanzar excepción si destino es nulo")
        void crearPasajeroConDestinoNuloLanzaExcepcion() {
            assertThrows(IllegalArgumentException.class, () -> new Pasajero("ID", p1, null), "Destino nulo debe lanzar excepción.");
        }

        @Test
        @DisplayName("Debería lanzar excepción si origen y destino son iguales")
        void crearPasajeroConOrigenIgualDestinoLanzaExcepcion() {
            assertThrows(IllegalArgumentException.class, () -> new Pasajero("ID", p1, p1), "Origen igual a destino debe lanzar excepción.");
        }
    }

    @Nested
    @DisplayName("Pruebas de Lógica de Satisfacción")
    class PruebasDeSatisfaccion {

        @Test
        @DisplayName("Debería ser 1 si el pasajero nunca pudo subir")
        void calcularSatisfaccionCuandoNoPudoSubir() {
            pasajero.setPudoSubir(false);
            assertEquals(1, pasajero.calcularSatisfaccion(), "Satisfacción debe ser 1 si no subió.");
        }

        @Test
        @DisplayName("Debería ser 5 si no esperó colectivos (subió al primero) y viajó sentado")
        void calcularSatisfaccionCasoIdeal() {
            pasajero.setPudoSubir(true);
            pasajero.setViajoSentado(true);
            // NO se incrementa colectivosEsperados, porque subió al primero que pasó (contador = 0).
            assertEquals(5, pasajero.calcularSatisfaccion(), "Satisfacción debe ser 5 en el caso ideal.");
        }
        
        @Test
        @DisplayName("Debería ser 4 si no esperó colectivos (subió al primero) y viajó de pie")
        void calcularSatisfaccionCasoIdealDePie() {
            pasajero.setPudoSubir(true);
            pasajero.setViajoSentado(false);
            // NO se incrementa colectivosEsperados, porque subió al primero (contador = 0).
            assertEquals(4, pasajero.calcularSatisfaccion(), "Satisfacción debe ser 4 si subió al primero pero fue de pie.");
        }

        @Test
        @DisplayName("Debería ser 3 si esperó 1 colectivo")
        void calcularSatisfaccionCuandoEsperaUnColectivo() {
            pasajero.setPudoSubir(true);
            pasajero.incrementarColectivosEsperados(); // Pasa 1 colectivo de largo.
            // Sube al siguiente. El contador es 1.
            assertEquals(3, pasajero.calcularSatisfaccion(), "Satisfacción debe ser 3 si esperó 1 colectivo.");
        }

        @Test
        @DisplayName("Debería ser 2 si esperó 2 o más colectivos")
        void calcularSatisfaccionCuandoEsperaVariosColectivos() {
            pasajero.setPudoSubir(true);
            pasajero.incrementarColectivosEsperados(); // Pasa el 1ro.
            pasajero.incrementarColectivosEsperados(); // Pasa el 2do.
            // Sube al siguiente. El contador es 2.
            assertEquals(2, pasajero.calcularSatisfaccion(), "Satisfacción debe ser 2 si esperó 2 colectivos.");
        }
        
        @Test
        @DisplayName("Debería devolver valor forzado si se usa setSatisfaccion")
        void calcularSatisfaccionConValorForzado() {
            // Este caso simula una bajada forzosa en una terminal, por ejemplo.
            pasajero.setSatisfaccion(1); 
            assertEquals(1, pasajero.calcularSatisfaccion(), "Debe devolver el valor seteado, ignorando otros cálculos.");
        }
    }

    @Nested
    @DisplayName("Pruebas de Estado y Comportamiento")
    class PruebasDeEstado {

        @Test
        @DisplayName("incrementarColectivosEsperados debería aumentar el contador en uno")
        void incrementarColectivosEsperados() {
            assertEquals(0, pasajero.getColectivosEsperados());
            pasajero.incrementarColectivosEsperados();
            assertEquals(1, pasajero.getColectivosEsperados());
            pasajero.incrementarColectivosEsperados();
            assertEquals(2, pasajero.getColectivosEsperados());
        }

        @Test
        @DisplayName("resetearEstadoViaje debería restaurar todos los valores por defecto")
        void resetearEstadoViaje() {
            // Modificamos el estado
            pasajero.incrementarColectivosEsperados();
            pasajero.setPudoSubir(true);
            pasajero.setViajoSentado(true);
            pasajero.setSatisfaccion(5);
            pasajero.setBajadaForzosa(true);

            // Reseteamos
            pasajero.resetearEstadoViaje();

            // Verificamos
            assertEquals(0, pasajero.getColectivosEsperados());
            assertFalse(pasajero.isPudoSubir());
            assertFalse(pasajero.isViajoSentado());
            assertFalse(pasajero.isBajadaForzosa());
            assertEquals(0, pasajero.getSatisfaccion());
        }
    }

    @Nested
    @DisplayName("Pruebas de Contrato (equals, hashCode, toString)")
    class PruebasDeContrato {
        
        @Test
        @DisplayName("equals y hashCode: deberían basarse únicamente en el ID")
        void pasajerosConMismoIdSonIguales() {
            Parada otraParada = new Parada("P03", "Otra");
            Pasajero pas1 = new Pasajero("ID_IGUAL", p1, p2);
            Pasajero pas2 = new Pasajero("ID_IGUAL", p1, otraParada); // Mismo ID, diferente destino
            
            assertEquals(pas1, pas2, "Pasajeros con el mismo ID deben ser iguales.");
            assertEquals(pas1.hashCode(), pas2.hashCode(), "El hashCode debe ser consistente con equals.");
        }
    
        @Test
        @DisplayName("equals: no deberían ser iguales si los IDs son diferentes")
        void pasajerosConDiferenteIdNoSonIguales() {
            Pasajero pas1 = new Pasajero("ID_UNO", p1, p2);
            Pasajero pas2 = new Pasajero("ID_DOS", p1, p2);
            assertNotEquals(pas1, pas2);
        }
    
        @Test
        @DisplayName("toString: debería contener la información relevante")
        void toStringContieneInfoRelevante() {
            Pasajero pasajeroConId = new Pasajero("PASS_TEST", p1, p2);
            String str = pasajeroConId.toString();
            
            assertTrue(str.contains("'PASS_TEST'"), "toString() debe contener el ID del pasajero.");
            assertTrue(str.contains("Origen: " + p1.getId()), "toString() debe contener el ID de origen.");
            assertTrue(str.contains("Destino: " + p2.getId()), "toString() debe contener el ID de destino.");
        }
    }
}