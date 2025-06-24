package ar.edu.unpsjb.ayp2.proyectointegrador.interfaz;

import ar.edu.unpsjb.ayp2.proyectointegrador.logica.Simulador;
import ar.edu.unpsjb.ayp2.proyectointegrador.modelo.Colectivo;
import ar.edu.unpsjb.ayp2.proyectointegrador.modelo.Linea;
import ar.edu.unpsjb.ayp2.proyectointegrador.modelo.Parada;
import ar.edu.unpsjb.ayp2.proyectointegrador.modelo.Pasajero;
import org.junit.jupiter.api.*;
import java.io.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la clase SimuladorUI.
 * Se verifica la correcta inicialización y la interacción básica del menú.
 * Se usan stubs para las dependencias y se simula la entrada/salida por consola.
 */
@DisplayName("Pruebas de la Clase SimuladorUI")
class SimuladorUITest {
    private SimuladorControllerStub controllerStub;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final InputStream originalIn = System.in;

    @BeforeEach
    void setUp() {
        controllerStub = new SimuladorControllerStub();
        System.setOut(new PrintStream(outContent)); // Redirige la salida estándar
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut); // Restaura la salida
        System.setIn(originalIn);   // Restaura la entrada
    }
    
    /**
     * Simula la entrada del usuario por consola.
     * @param data La secuencia de entradas del usuario, separadas por saltos de línea.
     */
    private void simularEntradaUsuario(String data) {
        System.setIn(new ByteArrayInputStream(data.getBytes()));
    }

    @Nested
    @DisplayName("Pruebas de Arranque y Menú")
    class PruebasDeArranque {

        @Test
        @DisplayName("start() debería imprimir bienvenida, datos iniciales y menú principal")
        void startImprimeTodoCorrectamente() {
            // Simulamos que el usuario ingresa "0" para salir inmediatamente.
            simularEntradaUsuario("0\n");
            
            SimuladorUI ui = new SimuladorUI(controllerStub);
            ui.start();
            
            String salida = outContent.toString();

            // Verificamos que se imprima toda la información esperada al arrancar.
            assertTrue(salida.contains("Simulador de Colectivos Urbanos"), "Debe mostrar la bienvenida.");
            assertTrue(salida.contains("Simulación lista para comenzar con 3 pasajeros."), "Debe mostrar el conteo de pasajeros.");
            assertTrue(salida.contains("Se han inicializado 2 colectivos."), "Debe mostrar el conteo de colectivos.");
            assertTrue(salida.contains("--- MENÚ PRINCIPAL ---"), "Debe mostrar el menú.");
            assertTrue(salida.contains("Saliendo del simulador. ¡Hasta pronto!"), "Debe mostrar el mensaje de salida.");
        }

        @Test
        @DisplayName("Debería mostrar un mensaje de error para una opción no válida")
        void opcionNoValidaMuestraError() {
            // Simulamos que el usuario ingresa una opción inválida (9) y luego sale (0).
            simularEntradaUsuario("9\n0\n");
            
            SimuladorUI ui = new SimuladorUI(controllerStub);
            ui.start();
            
            String salida = outContent.toString();
            assertTrue(salida.contains("Opción no válida. Intente nuevamente."));
        }

        // El test para la ruta óptima se comenta porque la funcionalidad está deshabilitada en SimuladorUI.
        // Si se reactiva en el futuro, este test puede ser descomentado y actualizado.
        /*
        @Test
        @DisplayName("Debería mostrar mensaje de parada no encontrada en cálculo de ruta")
        void testCalcularRutaParadaNoEncontrada() {
            // Se necesitaría la opción "4" en el menú para que este test funcione.
            simularEntradaUsuario("4\nX\nY\n0\n");
            SimuladorUI ui = new SimuladorUI(controllerStub);
            ui.start();
            String salida = outContent.toString();
            assertTrue(salida.contains("Parada origen o destino no encontrada."));
        }
        */
    }
}

// =================================================================================
// STUBS (Dobles de Prueba para aislar la UI)
// =================================================================================

/**
 * Stub para SimuladorController que simula la inicialización y datos mínimos.
 */
class SimuladorControllerStub extends SimuladorController {
    @Override
    public void inicializar() {
        // No hace nada, simula una inicialización exitosa e instantánea.
    }

    @Override
    public List<Pasajero> getPasajerosGenerados() {
        // CORRECCIÓN: Creamos paradas válidas para poder construir Pasajeros válidos.
        Parada origenStub = new Parada("P_STUB_O", "Origen Stub");
        Parada destinoStub = new Parada("P_STUB_D", "Destino Stub");
        
        return Arrays.asList(
            new Pasajero(origenStub, destinoStub),
            new Pasajero(origenStub, destinoStub),
            new Pasajero(origenStub, destinoStub)
        );
    }

    @Override
    public Simulador getSimulador() {
        return new SimuladorStub();
    }
}
/**
 * Stub para Simulador que cumple con la interfaz esperada por la UI.
 */
class SimuladorStub extends Simulador {
    public SimuladorStub() {
        // CORRECCIÓN: Se proveen datos mínimos válidos para cumplir con las
        // validaciones del constructor de la clase padre (Simulador).
        super(crearLineasStub(), crearParadasStub(), crearPasajerosStub(), null, null, new Properties());
    }

    // --- Métodos de ayuda para crear datos mínimos para el constructor ---

    private static Map<String, Linea> crearLineasStub() {
        Map<String, Linea> lineas = new HashMap<>();
        lineas.put("L1-STUB", new Linea("L1-STUB", "Linea Stub"));
        return lineas;
    }

    private static Map<String, Parada> crearParadasStub() {
        Map<String, Parada> paradas = new HashMap<>();
        paradas.put("P1-STUB", new Parada("P1-STUB", "Parada Stub"));
        return paradas;
    }

    private static List<Pasajero> crearPasajerosStub() {
        Parada p1 = new Parada("P1-STUB", "Origen");
        Parada p2 = new Parada("P2-STUB", "Destino");
        return Collections.singletonList(new Pasajero(p1, p2));
    }
    
    // --- Métodos sobreescritos para el comportamiento del stub ---

    @Override
    public List<Colectivo> getColectivosEnSimulacion() {
        Linea l1 = new Linea("L1", "Linea 1");
        Linea l2 = new Linea("L2", "Linea 2");
        
        return Arrays.asList(
            new Colectivo("C1", l1, 10, 5, 5, 1, 0),
            new Colectivo("C2", l2, 10, 5, 5, 1, 0)
        );
    }
}