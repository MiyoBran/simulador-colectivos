package proyectointegrador.logica;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import proyectointegrador.modelo.Colectivo;
import proyectointegrador.modelo.Linea;
import proyectointegrador.modelo.Parada;
import proyectointegrador.modelo.Pasajero;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@DisplayName("Pruebas de la Clase Simulador")
class SimuladorTest {

    // Dependencias comunes a la mayoría de los tests
    private Map<String, Linea> lineas;
    private Map<String, Parada> paradas;
    private GestorEstadisticas gestor;
    private PlanificadorRutas planificador;
    private Properties props;

    // Entidades del modelo para los escenarios
    private Parada p1, p2, p3;
    private Linea l1;

    @BeforeEach
    void setUp() {
        // 1. Crear el mundo de la simulación
        p1 = new Parada("P1", "Origen");
        p2 = new Parada("P2", "Intermedia");
        p3 = new Parada("P3", "Destino/Terminal");

        l1 = new Linea("L1", "Linea Unica");
        l1.agregarParadaAlRecorrido(p1);
        l1.agregarParadaAlRecorrido(p2);
        l1.agregarParadaAlRecorrido(p3);

        lineas = new HashMap<>();
        lineas.put("L1", l1);

        paradas = new HashMap<>();
        paradas.put("P1", p1);
        paradas.put("P2", p2);
        paradas.put("P3", p3);

        // 2. Crear los componentes de lógica
        gestor = new GestorEstadisticas();
        planificador = new PlanificadorRutas();
        planificador.construirGrafoDesdeLineas(lineas);
        props = new Properties();
    }

    /**
     * Método de ayuda para ejecutar una simulación hasta su finalización.
     */
    private void ejecutarSimulacionCompleta(Simulador simulador) {
        while (!simulador.isSimulacionTerminada()) {
            simulador.ejecutarPasoDeSimulacion();
        }
    }

    @Nested
    @DisplayName("Pruebas de Inicialización")
    class PruebasDeInicializacion {

        @Test
        @DisplayName("Debería lanzar excepción si los datos base son nulos o vacíos")
        void constructorConDatosInvalidos() {
            // CORRECCIÓN: Se prueba el constructor correcto con sus dependencias
            List<Pasajero> pasajeros = new ArrayList<>();
            pasajeros.add(new Pasajero(p1, p2));
            
            assertThrows(IllegalArgumentException.class, () -> new Simulador(null, paradas, pasajeros, gestor, planificador, props));
            assertThrows(IllegalArgumentException.class, () -> new Simulador(new HashMap<>(), paradas, pasajeros, gestor, planificador, props));
        }

        @Test
        @DisplayName("Debería inicializar los colectivos correctamente según la configuración")
        void inicializarColectivos() {
            props.setProperty("recorridos_por_colectivo", "2");
            props.setProperty("cantidad_de_colectivos_simultaneos_por_linea", "1");
            props.setProperty("frecuencia_salida_colectivos_minutos", "10");

            Simulador simulador = new Simulador(lineas, paradas, new ArrayList<>(), gestor, planificador, props);
            simulador.inicializarColectivos(30, 20);

            List<Colectivo> colectivos = simulador.getColectivosEnSimulacion();
            assertEquals(1, colectivos.size());
            
            Colectivo c = colectivos.get(0);
            assertEquals(l1, c.getLineaAsignada());
            assertEquals(30, c.getCapacidadMaxima());
            assertEquals(20, c.getCapacidadSentados());
            assertEquals(10, c.getCapacidadParados());
            assertEquals(2, c.getRecorridosRestantes());
            assertEquals(p1, c.getParadaActual());
        }
    }

    @Nested
    @DisplayName("Pruebas de Escenarios de Simulación")
    class PruebasDeEscenarios {

        @Test
        @DisplayName("Un pasajero debería completar su viaje de origen a destino correctamente")
        void escenarioViajeExitoso() {
            Pasajero pasajero = new Pasajero(p1, p3);
            p1.agregarPasajero(pasajero);
            List<Pasajero> pasajeros = List.of(pasajero);

            Simulador simulador = new Simulador(lineas, paradas, pasajeros, gestor, planificador, props);
            simulador.inicializarColectivos(10, 5);
            
            ejecutarSimulacionCompleta(simulador);

            assertTrue(pasajero.isPudoSubir());
            assertFalse(pasajero.isBajadaForzosa());
            assertEquals(1, gestor.getPasajerosTransportados());
            assertEquals(0, simulador.getColectivosEnSimulacion().get(0).getCantidadPasajerosABordo());
        }

        @Test
        @DisplayName("Con capacidad 1, el primer pasajero sube y el segundo es dejado atrás")
        void escenarioCapacidadLimitada() {
            Pasajero pA = new Pasajero(p1, p2);
            Pasajero pB = new Pasajero(p1, p3);
            p1.agregarPasajero(pA);
            p1.agregarPasajero(pB);
            List<Pasajero> pasajeros = List.of(pA, pB);
            
            Simulador simulador = new Simulador(lineas, paradas, pasajeros, gestor, planificador, props);
            simulador.inicializarColectivos(1, 1); // Capacidad máxima de 1

            ejecutarSimulacionCompleta(simulador);
            
            // Verificamos con XOR que solo uno de los dos pudo subir
            assertTrue(pA.isPudoSubir() ^ pB.isPudoSubir(), "Solo uno de los dos pasajeros debió haber subido.");
            
            Pasajero queNoSubio = pA.isPudoSubir() ? pB : pA;
            assertTrue(queNoSubio.getColectivosEsperados() > 0, "El pasajero que no subió debe haber esperado un colectivo.");
            assertEquals(1, p1.cantidadPasajerosEsperando(), "El pasajero que no subió debe seguir en la parada.");
        }
    }
}