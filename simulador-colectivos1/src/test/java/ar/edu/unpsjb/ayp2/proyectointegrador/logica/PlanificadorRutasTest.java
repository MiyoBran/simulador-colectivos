package ar.edu.unpsjb.ayp2.proyectointegrador.logica;

import ar.edu.unpsjb.ayp2.proyectointegrador.modelo.Linea;
import ar.edu.unpsjb.ayp2.proyectointegrador.modelo.Parada;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Pruebas de la Clase PlanificadorRutas")
public class PlanificadorRutasTest {
	
    private PlanificadorRutas planificador;
    private Parada p1, p2, p3, p4, p5;
    private Linea lineaRoja, lineaAzul;
    private Map<String, Linea> redDeTransporte;

    @BeforeEach
    public void setUp() {
        planificador = new PlanificadorRutas();

        // Creamos una red de transporte para las pruebas
        // Linea Roja: P1 -> P2 -> P3
        // Linea Azul: P4 -> P2 -> P5
        // El punto de transferencia es P2.
        p1 = new Parada("P1", "Inicio Roja");
        p2 = new Parada("P2", "Transferencia");
        p3 = new Parada("P3", "Fin Roja");
        p4 = new Parada("P4", "Inicio Azul");
        p5 = new Parada("P5", "Fin Azul");

        lineaRoja = new Linea("L-ROJA", "Línea Roja");
        lineaRoja.agregarParadaAlRecorrido(p1);
        lineaRoja.agregarParadaAlRecorrido(p2);
        lineaRoja.agregarParadaAlRecorrido(p3);
        
        lineaAzul = new Linea("L-AZUL", "Línea Azul");
        lineaAzul.agregarParadaAlRecorrido(p4);
        lineaAzul.agregarParadaAlRecorrido(p2);
        lineaAzul.agregarParadaAlRecorrido(p5);
        
        redDeTransporte = new HashMap<>();
        redDeTransporte.put(lineaRoja.getId(), lineaRoja);
        redDeTransporte.put(lineaAzul.getId(), lineaAzul);

        // Construimos el grafo usando el método público
        planificador.construirGrafoDesdeLineas(redDeTransporte);
    }

    @Nested
    @DisplayName("Pruebas con Rutas Válidas")
    class PruebasRutasValidas {
        
        @Test
        @DisplayName("Debería encontrar una ruta directa en la misma línea")
        void calcularRutaDirecta() {
            List<Parada> ruta = planificador.calcularRutaOptima(p1, p3);
            
            assertNotNull(ruta);
            assertEquals(3, ruta.size());
            assertEquals(p1, ruta.get(0));
            assertEquals(p2, ruta.get(1));
            assertEquals(p3, ruta.get(2));
        }
        
        @Test
        @DisplayName("Debería encontrar una ruta que requiere una transferencia")
        void calcularRutaConTransferencia() {
            // Para ir de P4 a P3, debe ir de P4 -> P2 (Línea Azul) y luego P2 -> P3 (Línea Roja)
            // El algoritmo de Dijkstra encontrará este camino como el más corto.
            List<Parada> ruta = planificador.calcularRutaOptima(p4, p3);
            
            assertNotNull(ruta);
            assertEquals(3, ruta.size());
            assertEquals(p4, ruta.get(0));
            assertEquals(p2, ruta.get(1)); // El punto de transferencia
            assertEquals(p3, ruta.get(2));
        }
        
        @Test
        @DisplayName("Debería devolver una ruta de una sola parada si origen y destino son iguales")
        void calcularRutaMismoOrigenDestino() {
            List<Parada> ruta = planificador.calcularRutaOptima(p1, p1);

            assertNotNull(ruta);
            assertEquals(1, ruta.size());
            assertEquals(p1, ruta.get(0));
        }
    }

    @Nested
    @DisplayName("Pruebas con Casos de Error o Rutas Inexistentes")
    class PruebasRutasInvalidas {

        @Test
        @DisplayName("Debería devolver una lista vacía si no hay ruta posible")
        void calcularRutaInexistente() {
            // No hay forma de ir de P1 a P4 en nuestra red
            List<Parada> ruta = planificador.calcularRutaOptima(p1, p4);
            assertNotNull(ruta);
            assertTrue(ruta.isEmpty(), "La lista de ruta debe estar vacía si no hay camino.");
        }

        @Test
        @DisplayName("Debería devolver una lista vacía si una de las paradas no está en el grafo")
        void calcularRutaConParadaInvalida() {
            Parada paradaFueraDeRed = new Parada("P99", "Parada Aislada");
            List<Parada> ruta = planificador.calcularRutaOptima(p1, paradaFueraDeRed);
            
            assertNotNull(ruta);
            assertTrue(ruta.isEmpty());
        }

        @Test
        @DisplayName("Debería devolver una lista vacía si los parámetros son nulos")
        void calcularRutaConParametrosNulos() {
            List<Parada> ruta1 = planificador.calcularRutaOptima(null, p1);
            List<Parada> ruta2 = planificador.calcularRutaOptima(p1, null);
            
            assertTrue(ruta1.isEmpty());
            assertTrue(ruta2.isEmpty());
        }
    }
}