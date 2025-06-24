package proyectointegrador.logica;

import proyectointegrador.logica.GestorEstadisticas;
import proyectointegrador.modelo.Parada;
import proyectointegrador.modelo.Pasajero;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Pruebas de la Clase GestorEstadisticas")
public class GestorEstadisticasTest {
    private GestorEstadisticas gestor;
    private Pasajero pasajeroSatisfecho;
    private Pasajero pasajeroInsatisfecho;
    private Pasajero pasajeroNeutral;

    @BeforeEach
    public void setUp() {
        gestor = new GestorEstadisticas();
        Parada origen = new Parada("P1", "Origen");
        Parada destino = new Parada("P2", "Destino");
        
        // Creamos pasajeros que resultarán en distintas calificaciones
        pasajeroSatisfecho = new Pasajero("PAX1", origen, destino); // Satisfacción 5 (viaja sentado en el 1er colectivo)
        pasajeroSatisfecho.setPudoSubir(true);
        pasajeroSatisfecho.setViajoSentado(true);

        pasajeroInsatisfecho = new Pasajero("PAX2", origen, destino); // Satisfacción 1 (no pudo subir)
        pasajeroInsatisfecho.setPudoSubir(false);

        pasajeroNeutral = new Pasajero("PAX3", origen, destino); // Satisfacción 3 (esperó 1 colectivo)
        pasajeroNeutral.setPudoSubir(true);
        pasajeroNeutral.incrementarColectivosEsperados();
    }

    @Nested
    @DisplayName("Pruebas de Registro y Reset")
    class PruebasDeRegistro {
        @Test
        @DisplayName("Debería registrar un pasajero e incrementar el total")
        void registrarPasajero() {
            assertEquals(0, gestor.getPasajerosTotales());
            gestor.registrarPasajero(pasajeroSatisfecho);
            assertEquals(1, gestor.getPasajerosTotales());
        }

        @Test
        @DisplayName("reset() debería reiniciar todas las estadísticas a cero")
        void reset() {
            gestor.registrarPasajero(pasajeroSatisfecho);
            gestor.registrarTransporte(pasajeroSatisfecho);
            gestor.registrarCapacidadColectivo("C1", 10);
            gestor.registrarOcupacionTramo("C1", 5);

            assertNotEquals(0, gestor.getPasajerosTotales());

            gestor.reset();

            assertEquals(0, gestor.getPasajerosTotales());
            assertEquals(0, gestor.getPasajerosTransportados());
            assertTrue(gestor.getDesgloseCalificaciones().isEmpty());
            assertTrue(gestor.getOcupacionPromedioPorColectivo().isEmpty());
        }
    }

    @Nested
    @DisplayName("Pruebas de Estadísticas de Satisfacción")
    class PruebasDeSatisfaccion {
        @Test
        @DisplayName("registrarTransporte() debería actualizar contadores y calificaciones")
        void registrarTransporte() {
            gestor.registrarTransporte(pasajeroSatisfecho); // Calificación 5
            gestor.registrarTransporte(pasajeroInsatisfecho); // Calificación 1
            gestor.registrarTransporte(pasajeroNeutral); // Calificación 3
            
            assertEquals(3, gestor.getPasajerosTransportados());
            
            Map<Integer, Integer> desglose = gestor.getDesgloseCalificaciones();
            assertEquals(1, desglose.get(5));
            assertEquals(1, desglose.get(1));
            assertEquals(1, desglose.get(3));
            
            // 2 satisfechos (>=3) de 3 total -> 66.6%
            assertEquals(200.0/3.0, gestor.getPorcentajeSatisfechos(), 0.01);
        }

        @Test
        @DisplayName("getIndiceSatisfaccion() debería calcular el índice normalizado correctamente")
        void getIndiceSatisfaccion() {
            gestor.registrarTransporte(pasajeroSatisfecho); // Calificación 5
            gestor.registrarTransporte(pasajeroInsatisfecho); // Calificación 1
            
            // Suma de calificaciones: 5 + 1 = 6
            // Máximo posible: 2 pasajeros * 5 = 10
            // Índice: 6 / 10 = 0.6
            assertEquals(0.6, gestor.getIndiceSatisfaccion(), 0.001);
        }
    }

    @Nested
    @DisplayName("Pruebas de Estadísticas de Ocupación y Desglose")
    class PruebasDeOcupacionYDesglose {
        @Test
        @DisplayName("Debería calcular correctamente el promedio de ocupación de un colectivo")
        void ocupacionPromedioPorColectivo() {
            gestor.registrarCapacidadColectivo("C1", 30);
            gestor.registrarOcupacionTramo("C1", 15); // 50%
            gestor.registrarOcupacionTramo("C1", 30); // 100%
            gestor.registrarOcupacionTramo("C1", 0);  // 0%
            
            Map<String, Double> promedios = gestor.getOcupacionPromedioPorColectivo();
            
            assertTrue(promedios.containsKey("C1"));
            assertEquals((0.5 + 1.0 + 0.0) / 3.0, promedios.get("C1"), 0.0001);
        }

        @Test
        @DisplayName("Debería devolver el desglose correcto del estado final de los pasajeros")
        void desglosePasajeros() {
            Pasajero pTransportado = new Pasajero("P1", new Parada("P1", "O"), new Parada("P2", "D"));
            pTransportado.setPudoSubir(true);

            Pasajero pNoSubio = new Pasajero("P2", new Parada("P1", "O"), new Parada("P2", "D"));
            pNoSubio.setPudoSubir(false);

            Pasajero pBajadoForzosamente = new Pasajero("P3", new Parada("P1", "O"), new Parada("P2", "D"));
            pBajadoForzosamente.setPudoSubir(true);

            pBajadoForzosamente.setBajadaForzosa(true);

            gestor.registrarPasajero(pTransportado);
            gestor.registrarPasajero(pNoSubio);
            gestor.registrarPasajero(pBajadoForzosamente);
            
            Map<String, Integer> desglose = gestor.getDesglosePasajeros();
            
            // CORRECCIÓN: Se usa la clave correcta del mapa.
            assertEquals(1, desglose.get("transportadosConExito"));
            assertEquals(1, desglose.get("bajadosForzosamente"));
            assertEquals(1, desglose.get("nuncaSubieron"));
        }
    }
}