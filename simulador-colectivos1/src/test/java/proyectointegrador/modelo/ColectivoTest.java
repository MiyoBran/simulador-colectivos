package proyectointegrador.modelo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

/**
 * Pruebas unitarias para la clase Colectivo.
 * Verifica la correcta inicialización, gestión de pasajeros (incluyendo asientos),
 * movimiento, manejo de recorridos y el contrato de la clase.
 */
@DisplayName("Pruebas de la Clase Colectivo")
class ColectivoTest {

    private Linea linea;
    private Parada p1, p2, p3;
    private Pasajero pax1, pax2, pax3, pax4;
    private Colectivo colectivoSUT; // System Under Test

    @BeforeEach
    void setUp() {
        // --- Configuración Común ---
        p1 = new Parada("P01", "Parada Inicial");
        p2 = new Parada("P02", "Parada Intermedia");
        p3 = new Parada("P03", "Parada Final");

        linea = new Linea("LCR", "Linea Con Recorrido");
        linea.agregarParadaAlRecorrido(p1);
        linea.agregarParadaAlRecorrido(p2);
        linea.agregarParadaAlRecorrido(p3);

        // Pasajeros para distintos escenarios
        pax1 = new Pasajero(p1, p2);
        pax2 = new Pasajero(p1, p3);
        pax3 = new Pasajero(p2, p3);
        pax4 = new Pasajero(p1, p2); // Otro pasajero para llenar

        // Colectivo SUT: Capacidad total 3 (2 sentados, 1 de pie)
        // CORRECCIÓN: Se usa el único constructor público de 7 argumentos.
        colectivoSUT = new Colectivo("C01", linea, 3, 2, 1, 1, 0);
    }

    @Nested
    @DisplayName("Pruebas de Constructor e Inicialización")
    class PruebasDeConstructor {
        
        @Test
        @DisplayName("Debería inicializar correctamente con datos válidos")
        void constructorConDatosValidos() {
            assertEquals("C01", colectivoSUT.getIdColectivo());
            assertSame(linea, colectivoSUT.getLineaAsignada());
            assertEquals(3, colectivoSUT.getCapacidadMaxima());
            assertEquals(2, colectivoSUT.getCapacidadSentados());
            assertEquals(1, colectivoSUT.getCapacidadParados());
            assertEquals(1, colectivoSUT.getRecorridosRestantes());
            assertEquals(0, colectivoSUT.getPasoDeSalida());
            assertTrue(colectivoSUT.getPasajerosABordo().isEmpty());
            assertEquals(p1, colectivoSUT.getParadaActual(), "Debería iniciar en la primera parada.");
            assertEquals(0, colectivoSUT.getIndiceParadaActualEnRecorrido());
        }

        @Test
        @DisplayName("Debería inicializar en estado nulo si la línea no tiene recorrido")
        void constructorConLineaVacia() {
            Linea lineaVacia = new Linea("LV", "Linea Vacia");
            Colectivo c = new Colectivo("C02", lineaVacia, 5, 3, 2, 1, 0);
            assertNull(c.getParadaActual(), "Parada actual debería ser null si la línea está vacía.");
            assertEquals(-1, c.getIndiceParadaActualEnRecorrido(), "Índice debería ser -1.");
        }

        @Test
        @DisplayName("Debería lanzar excepción para parámetros inválidos")
        void constructorConParametrosInvalidos() {
            assertThrows(IllegalArgumentException.class, () -> new Colectivo(null, linea, 5, 3, 2, 1, 0), "ID nulo.");
            assertThrows(IllegalArgumentException.class, () -> new Colectivo("C03", null, 5, 3, 2, 1, 0), "Línea nula.");
            assertThrows(IllegalArgumentException.class, () -> new Colectivo("C04", linea, -1, 0, 0, 1, 0), "Capacidad negativa.");
        }
    }

    @Nested
    @DisplayName("Pruebas de Gestión de Pasajeros")
    class PruebasDePasajeros {

        @Test
        @DisplayName("subirPasajero() debería asignar asiento si hay disponible")
        void subirPasajeroOcupaAsiento() {
            assertTrue(colectivoSUT.subirPasajero(pax1));
            assertTrue(pax1.isViajoSentado(), "El primer pasajero debería conseguir asiento.");
            assertEquals(1, colectivoSUT.getAsientosDisponibles());

            assertTrue(colectivoSUT.subirPasajero(pax2));
            assertTrue(pax2.isViajoSentado(), "El segundo pasajero también debería sentarse.");
            assertEquals(0, colectivoSUT.getAsientosDisponibles());
        }

        @Test
        @DisplayName("subirPasajero() debería asignarlo de pie si no hay asientos")
        void subirPasajeroVaDePie() {
            colectivoSUT.subirPasajero(pax1); // Ocupa asiento 1
            colectivoSUT.subirPasajero(pax2); // Ocupa asiento 2
            
            assertTrue(colectivoSUT.subirPasajero(pax3)); // Debería ir de pie
            assertFalse(pax3.isViajoSentado(), "El tercer pasajero debería ir de pie.");
            assertEquals(0, colectivoSUT.getLugaresDePieDisponibles());
        }

        @Test
        @DisplayName("subirPasajero() debería fallar si el colectivo está lleno")
        void subirPasajeroFallaSiLleno() {
            colectivoSUT.subirPasajero(pax1); // Sentado
            colectivoSUT.subirPasajero(pax2); // Sentado
            colectivoSUT.subirPasajero(pax3); // De pie
            
            assertEquals(0, colectivoSUT.getCapacidadDisponible());
            assertFalse(colectivoSUT.subirPasajero(pax4), "No debería poder subir un cuarto pasajero.");
        }
        
        @Test
        @DisplayName("bajarPasajero() sentado debería liberar un asiento")
        void bajarPasajeroSentadoLiberaAsiento() {
            colectivoSUT.subirPasajero(pax1); // pax1 sentado
            assertEquals(1, colectivoSUT.getAsientosDisponibles());
            
            assertTrue(colectivoSUT.bajarPasajero(pax1));
            assertEquals(2, colectivoSUT.getAsientosDisponibles(), "Al bajar un pasajero sentado, un asiento debe quedar libre.");
        }

        @Test
        @DisplayName("getPasajerosABordo() debería devolver una copia defensiva")
        void getPasajerosDevuelveCopia() {
            colectivoSUT.subirPasajero(pax1);
            List<Pasajero> copia = colectivoSUT.getPasajerosABordo();
            copia.add(pax2); // Modificar la copia
            assertEquals(1, colectivoSUT.getCantidadPasajerosABordo(), "La lista interna no debe cambiar.");
        }
    }

    @Nested
    @DisplayName("Pruebas de Movimiento y Recorrido")
    class PruebasDeMovimiento {

        @Test
        @DisplayName("avanzarAProximaParada() debería moverse secuencialmente por el recorrido")
        void avanzarParada() {
            assertEquals(p1, colectivoSUT.getParadaActual());
            colectivoSUT.avanzarAProximaParada();
            assertEquals(p2, colectivoSUT.getParadaActual());
            colectivoSUT.avanzarAProximaParada();
            assertEquals(p3, colectivoSUT.getParadaActual());
        }

        @Test
        @DisplayName("avanzarAProximaParada() no debería hacer nada si está en la terminal")
        void avanzarEnTerminal() {
            colectivoSUT.avanzarAProximaParada(); // a p2
            colectivoSUT.avanzarAProximaParada(); // a p3 (terminal)
            
            assertTrue(colectivoSUT.estaEnTerminal());
            colectivoSUT.avanzarAProximaParada(); // Intento de avanzar
            assertEquals(p3, colectivoSUT.getParadaActual(), "No debería moverse de la terminal.");
        }

        @Test
        @DisplayName("reiniciarParaNuevoRecorrido() debería volver a la primera parada")
        void reiniciarRecorrido() {
            colectivoSUT.avanzarAProximaParada();
            colectivoSUT.avanzarAProximaParada(); // Llega a p3
            
            colectivoSUT.reiniciarParaNuevoRecorrido();
            assertEquals(p1, colectivoSUT.getParadaActual(), "Debería estar en la primera parada después de reiniciar.");
            assertEquals(0, colectivoSUT.getIndiceParadaActualEnRecorrido());
        }

        @Test
        @DisplayName("actualizarRecorridosRestantes() debería decrementar el contador")
        void actualizarRecorridos() {
            int recorridosAntes = colectivoSUT.getRecorridosRestantes();
            colectivoSUT.actualizarRecorridosRestantes();
            assertEquals(recorridosAntes - 1, colectivoSUT.getRecorridosRestantes());
        }
    }

    @Nested
    @DisplayName("Pruebas de Contrato y Reportes")
    class PruebasDeContrato {
        
        @Test
        @DisplayName("equals y hashCode deberían basarse solo en el ID")
        void equalsYHashCode() {
            Colectivo colectivoIgual = new Colectivo("C01", linea, 10, 5, 5, 5, 5);
            Colectivo colectivoDiferente = new Colectivo("C99", linea, 3, 2, 1, 1, 0);

            assertEquals(colectivoSUT, colectivoIgual);
            assertEquals(colectivoSUT.hashCode(), colectivoIgual.hashCode());
            assertNotEquals(colectivoSUT, colectivoDiferente);
        }

        @Test
        @DisplayName("toString debería generar una representación correcta")
        void testToString() {
            colectivoSUT.subirPasajero(pax1);
            String str = colectivoSUT.toString();
            // CORRECCIÓN: Se ajusta a la nueva salida del toString()
            assertTrue(str.contains("id='C01'"));
            assertTrue(str.contains("linea='LCR'"));
            assertTrue(str.contains("pasajeros=1"));
            assertTrue(str.contains("parada='P01'"));
        }

        @Test
        @DisplayName("getReporteDeEstado() debería generar un reporte completo")
        void getReporte() {
            colectivoSUT.subirPasajero(pax1);
            String reporte = colectivoSUT.getReporteDeEstado();
            assertTrue(reporte.contains("Estado del Colectivo: C01 (Linea Con Recorrido)"));
            assertTrue(reporte.contains("> Ocupación: 1 / 3 pasajeros"));
            assertTrue(reporte.contains("- Sentados: 1 / 2"));
        }
    }
}