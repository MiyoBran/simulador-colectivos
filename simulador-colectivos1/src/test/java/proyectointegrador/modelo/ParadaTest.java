package proyectointegrador.modelo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import proyectointegrador.modelo.Parada;
import proyectointegrador.modelo.Pasajero;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la clase Parada.
 * Verifica la correcta creación, la gestión de la cola de pasajeros,
 * el manejo de estadísticas y el contrato de la clase.
 */
@DisplayName("Pruebas de la Clase Parada")
class ParadaTest {

    // SUT = System Under Test (el objeto que estamos probando)
    private Parada paradaSUT; 
    private Pasajero pasajero1, pasajero2;

    @BeforeEach
    void setUp() {
        // Creamos una parada y pasajeros nuevos para cada test,
        // asegurando que las pruebas sean independientes entre sí.
        paradaSUT = new Parada("P01", "Terminal", 45.123, 67.456);
        Parada paradaDestino1 = new Parada("P02", "Destino Lejano");
        Parada paradaDestino2 = new Parada("P03", "Otro Destino");
        pasajero1 = new Pasajero("PAX01", paradaSUT, paradaDestino1);
        pasajero2 = new Pasajero("PAX02", paradaSUT, paradaDestino2);
    }

    @Nested
    @DisplayName("Pruebas de Constructores")
    class PruebasDeConstructor {

        @Test
        @DisplayName("Debería crear una parada con todos los datos y estado inicial correcto")
        void crearParadaConDatosValidos() {
            assertEquals("P01", paradaSUT.getId());
            assertEquals("Terminal", paradaSUT.getDireccion());
            assertEquals(45.123, paradaSUT.getLatitud());
            assertEquals(67.456, paradaSUT.getLongitud());
            // Verificar estado inicial
            assertFalse(paradaSUT.hayPasajerosEsperando(), "Una nueva parada no debería tener pasajeros.");
            assertEquals(0, paradaSUT.getPasajerosAbordados(), "Pasajeros abordados debe ser 0 al inicio.");
            assertEquals(0, paradaSUT.getColectivosPasados(), "Colectivos pasados debe ser 0 al inicio.");
        }

        @Test
        @DisplayName("Debería lanzar excepción si el ID es nulo")
        void crearParadaConIdNuloLanzaExcepcion() {
            assertThrows(IllegalArgumentException.class, () -> new Parada(null, "Dirección Válida"));
        }

        @Test
        @DisplayName("Debería lanzar excepción si la dirección es nula o vacía")
        void crearParadaConDireccionVaciaLanzaExcepcion() {
            assertThrows(IllegalArgumentException.class, () -> new Parada("IDValido", "  "), "Dirección con espacios debe fallar.");
            assertThrows(IllegalArgumentException.class, () -> new Parada("IDValido", null), "Dirección nula debe fallar.");
        }
    }

    @Nested
    @DisplayName("Pruebas de Gestión de Cola de Pasajeros")
    class PruebasDeCola {

        @Test
        @DisplayName("agregarPasajero() debería añadir un pasajero a la cola")
        void agregarPasajeroACola() {
            assertTrue(paradaSUT.getPasajerosEsperando().isEmpty());
            paradaSUT.agregarPasajero(pasajero1);
            assertEquals(1, paradaSUT.cantidadPasajerosEsperando());
            // CORRECCIÓN: Usamos peekSiguientePasajero() para verificar quién está al frente.
            assertEquals(pasajero1, paradaSUT.peekSiguientePasajero(), "El pasajero al frente de la cola debe ser el que agregamos.");
        }
        
        @Test
        @DisplayName("agregarPasajero() nulo debería lanzar una excepción")
        void agregarPasajeroNuloLanzaExcepcion() {
            assertThrows(IllegalArgumentException.class, () -> paradaSUT.agregarPasajero(null));
        }

        @Test
        @DisplayName("removerSiguientePasajero() debería seguir el orden FIFO")
        void removerPasajeroDeColaRespetaFIFO() {
            paradaSUT.agregarPasajero(pasajero1);
            paradaSUT.agregarPasajero(pasajero2);

            Pasajero removido1 = paradaSUT.removerSiguientePasajero();
            assertEquals(pasajero1, removido1, "El primero en entrar (pasajero1) debe ser el primero en salir.");
            
            Pasajero removido2 = paradaSUT.removerSiguientePasajero();
            assertEquals(pasajero2, removido2, "El segundo en entrar (pasajero2) debe ser el segundo en salir.");
            
            assertTrue(paradaSUT.getPasajerosEsperando().isEmpty(), "La cola debería quedar vacía.");
        }
        
        @Test
        @DisplayName("removerSiguientePasajero() de una cola vacía debería devolver null")
        void removerDeColaVaciaDevuelveNull() {
            assertNull(paradaSUT.removerSiguientePasajero(), "Remover de una cola vacía debe ser null.");
        }

        @Test
        @DisplayName("peekSiguientePasajero() debería devolver el primer pasajero sin removerlo")
        void peekSiguientePasajero() {
            paradaSUT.agregarPasajero(pasajero1);
            paradaSUT.agregarPasajero(pasajero2);
            
            assertEquals(2, paradaSUT.cantidadPasajerosEsperando());
            assertEquals(pasajero1, paradaSUT.peekSiguientePasajero(), "Peek debe devolver el primer pasajero.");
            assertEquals(2, paradaSUT.cantidadPasajerosEsperando(), "Peek no debe modificar el tamaño de la cola.");
        }
    }

    @Nested
    @DisplayName("Pruebas de Estadísticas")
    class PruebasDeEstadisticas {

        @Test
        @DisplayName("incrementarPasajerosAbordados() debería aumentar el contador")
        void incrementarPasajerosAbordados() {
            assertEquals(0, paradaSUT.getPasajerosAbordados());
            paradaSUT.incrementarPasajerosAbordados();
            assertEquals(1, paradaSUT.getPasajerosAbordados());
            paradaSUT.incrementarPasajerosAbordados();
            assertEquals(2, paradaSUT.getPasajerosAbordados());
        }

        @Test
        @DisplayName("incrementarColectivosPasados() debería aumentar el contador")
        void incrementarColectivosPasados() {
            assertEquals(0, paradaSUT.getColectivosPasados());
            paradaSUT.incrementarColectivosPasados();
            assertEquals(1, paradaSUT.getColectivosPasados());
        }
    }
    
    @Nested
    @DisplayName("Pruebas de Contrato (equals, hashCode, toString)")
    class PruebasDeContrato {
        
        @Test
        @DisplayName("equals y hashCode deberían basarse únicamente en el ID")
        void paradasConMismoIdSonIguales() {
            Parada paradaIgual = new Parada("P01", "Otro Nombre, Mismo ID");
            Parada paradaDiferente = new Parada("P99", "Terminal");

            assertEquals(paradaSUT, paradaIgual, "Paradas con el mismo ID deben ser iguales.");
            assertEquals(paradaSUT.hashCode(), paradaIgual.hashCode(), "HashCodes de paradas iguales deben ser iguales.");
            assertNotEquals(paradaSUT, paradaDiferente, "Paradas con diferente ID no deben ser iguales.");
        }
        
        @Test
        @DisplayName("toString debería contener información relevante")
        void toStringContieneInfoRelevante() {
            String str = paradaSUT.toString();
            
            assertTrue(str.contains("id='P01'"));
            assertTrue(str.contains("direccion='Terminal'"));
            assertTrue(str.contains("esperando=0"));
    
            paradaSUT.agregarPasajero(pasajero1);
            str = paradaSUT.toString();
            assertTrue(str.contains("esperando=1"));
        }
    }
}