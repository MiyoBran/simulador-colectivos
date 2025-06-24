package ar.edu.unpsjb.ayp2.proyectointegrador.logica;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import ar.edu.unpsjb.ayp2.proyectointegrador.modelo.Linea;
import ar.edu.unpsjb.ayp2.proyectointegrador.modelo.Parada;
import ar.edu.unpsjb.ayp2.proyectointegrador.modelo.Pasajero;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Tests para la clase GeneradorPasajeros.
 * Verifica la correcta inicialización, la generación de pasajeros válidos y
 * el manejo de casos de error como la falta de líneas válidas.
 */
@DisplayName("Pruebas de la Clase GeneradorPasajeros")
class GeneradorPasajerosTest {

    private Map<String, Linea> lineasDePrueba;
    private Properties configDePrueba;
    private GestorEstadisticasStub gestorEstadisticasStub;

    private Parada p1, p2, p3, p4, p5;
    private Linea lValida, lDemasiadoCorta;

    @BeforeEach
    void setUp() {
        // Inicializar paradas
        p1 = new Parada("P1", "Calle A");
        p2 = new Parada("P2", "Calle B");
        p3 = new Parada("P3", "Calle C");
        p4 = new Parada("P4", "Calle D");
        p5 = new Parada("P5", "Calle E");

        // Limpiamos las colas por si un test anterior las dejó con datos
        for(Parada p : List.of(p1,p2,p3,p4,p5)){
            while(p.hayPasajerosEsperando()) p.removerSiguientePasajero();
        }

        // Línea válida para generar pasajeros
        lValida = new Linea("LV", "Linea Valida");
        lValida.agregarParadaAlRecorrido(p1);
        lValida.agregarParadaAlRecorrido(p2);
        lValida.agregarParadaAlRecorrido(p3);

        // Línea inválida (demasiado corta)
        lDemasiadoCorta = new Linea("LC", "Linea Corta");
        lDemasiadoCorta.agregarParadaAlRecorrido(p4); // Solo 1 parada

        lineasDePrueba = new HashMap<>();
        lineasDePrueba.put("LV", lValida);
        lineasDePrueba.put("LC", lDemasiadoCorta);
        
        configDePrueba = new Properties();
        gestorEstadisticasStub = new GestorEstadisticasStub();
    }

    @Nested
    @DisplayName("Pruebas de Constructor")
    class PruebasDeConstructor {

        @Test
        @DisplayName("Debería inicializar correctamente con argumentos válidos")
        void constructorExitoso() {
            configDePrueba.setProperty("cantidadPasajeros", "5");
            // CORRECCIÓN: Se usa el nuevo constructor sin el mapa de paradas.
            assertDoesNotThrow(() -> new GeneradorPasajeros(lineasDePrueba, configDePrueba, gestorEstadisticasStub));
        }

        @Test
        @DisplayName("Debería lanzar excepción si las líneas son nulas o vacías")
        void constructorConLineasInvalidas() {
            assertThrows(IllegalArgumentException.class, () -> new GeneradorPasajeros(null, configDePrueba, gestorEstadisticasStub));
            assertThrows(IllegalArgumentException.class, () -> new GeneradorPasajeros(new HashMap<>(), configDePrueba, gestorEstadisticasStub));
        }

        @Test
        @DisplayName("Debería lanzar excepción si las propiedades o el gestor son nulos")
        void constructorConOtrasDependenciasNulas() {
            assertThrows(IllegalArgumentException.class, () -> new GeneradorPasajeros(lineasDePrueba, null, gestorEstadisticasStub));
            assertThrows(IllegalArgumentException.class, () -> new GeneradorPasajeros(lineasDePrueba, configDePrueba, null));
        }
    }

    @Nested
    @DisplayName("Pruebas de Generación de Pasajeros")
    class PruebasDeGeneracion {

        @Test
        @DisplayName("Debería generar la cantidad correcta de pasajeros")
        void generarPasajerosCantidadCorrecta() {
            configDePrueba.setProperty("cantidadPasajeros", "10");
            GeneradorPasajeros generador = new GeneradorPasajeros(lineasDePrueba, configDePrueba, gestorEstadisticasStub);
            
            List<Pasajero> pasajerosGenerados = generador.generarPasajeros();
            
            assertEquals(10, pasajerosGenerados.size());
            assertEquals(10, gestorEstadisticasStub.getPasajerosRegistrados(), "Todos los pasajeros deben registrarse en estadísticas.");
        }

        @Test
        @DisplayName("Debería lanzar excepción si no hay líneas válidas para generar viajes")
        void generarPasajerosSinLineasValidas() {
            Map<String, Linea> lineasInvalidas = new HashMap<>();
            lineasInvalidas.put("LC", lDemasiadoCorta); // Solo proveemos la línea que tiene < 2 paradas

            configDePrueba.setProperty("cantidadPasajeros", "5");
            GeneradorPasajeros generador = new GeneradorPasajeros(lineasInvalidas, configDePrueba, gestorEstadisticasStub);
            
            assertThrows(IllegalStateException.class, generador::generarPasajeros, "Debe lanzar excepción si no hay líneas con al menos 2 paradas.");
        }
        
        @Test
        @DisplayName("Debería generar pasajeros con origen y destino válidos (destino > origen)")
        void generarPasajerosConOrigenDestinoValidos() {
            configDePrueba.setProperty("cantidadPasajeros", "50");
            GeneradorPasajeros generador = new GeneradorPasajeros(lineasDePrueba, configDePrueba, gestorEstadisticasStub);

            List<Pasajero> pasajerosGenerados = generador.generarPasajeros();

            for (Pasajero p : pasajerosGenerados) {
                assertNotNull(p.getParadaOrigen());
                assertNotNull(p.getParadaDestino());
                assertNotEquals(p.getParadaOrigen(), p.getParadaDestino());

                // Solo pueden ser de la línea válida, no de la corta
                int indexOrigen = lValida.getIndiceParada(p.getParadaOrigen());
                int indexDestino = lValida.getIndiceParada(p.getParadaDestino());
                
                assertTrue(indexOrigen != -1 && indexDestino != -1, "Las paradas deben pertenecer a la línea válida.");
                assertTrue(indexDestino > indexOrigen, "El índice de destino debe ser mayor que el de origen.");
            }
        }

        @Test
        @DisplayName("Debería añadir a cada pasajero generado a la cola de su parada de origen")
        void generarPasajerosLosEncolaEnSuOrigen() {
            configDePrueba.setProperty("cantidadPasajeros", "20");
            GeneradorPasajeros generador = new GeneradorPasajeros(lineasDePrueba, configDePrueba, gestorEstadisticasStub);
            
            List<Pasajero> pasajerosGenerados = generador.generarPasajeros();
            
            int totalPasajerosEnColas = p1.cantidadPasajerosEsperando() + p2.cantidadPasajerosEsperando() + p3.cantidadPasajerosEsperando();
            assertEquals(20, totalPasajerosEnColas, "La suma de pasajeros en todas las colas debe ser igual al total generado.");

            // Verificación individual
            for (Pasajero p : pasajerosGenerados) {
                // CORRECCIÓN: Como no hay método "tienePasajeroEnCola", verificamos que el primero en salir sea uno de los generados.
                // Esta no es una prueba perfecta, pero es una aproximación razonable sin romper el encapsulamiento.
                // La prueba de la suma total es más robusta en este caso.
                assertTrue(pasajerosGenerados.contains(p.getParadaOrigen().peekSiguientePasajero()));
            }
        }
    }

    /**
     * Stub de GestorEstadisticas para verificar interacciones sin depender de la clase real.
     */
    static class GestorEstadisticasStub extends GestorEstadisticas {
        private int pasajerosRegistrados = 0;
        
        @Override
        public void registrarPasajero(Pasajero pasajero) {
            pasajerosRegistrados++;
        }
        
        public int getPasajerosRegistrados() {
            return pasajerosRegistrados;
        }
    }
}